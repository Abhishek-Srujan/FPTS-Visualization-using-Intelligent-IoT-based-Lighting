#!/usr/bin/env python

# Usage - python algo.py "task1 c1 t1 d1 s1 p1 th1" "task2 c2 t2 d2 s2 p2 th2" "task3 c3 t3 d3 s3 p3 th3"
# ci - computation time, ti - period, di - deadline, si - stack size
# Program returns/prints:
# "Priority = [i k j]"
# "Threshold = [j k i]"    
# The above value of priority and threshold should be displayed on the user interface 
# and the user should be able to change threshold values and call the program again to generate grasp file
# Add efficiency part, check if executionTime and responseTime can be shown in GRASP 

import os
import shutil
import thread
import time
import threading
import datetime
import math
import sys, getopt

taskList = list()
computationList = list()
periodList = list()
deadlineList = list()
priorityList = list()
thresholdList = list()
arrivalList = list()
instanceList = list()
stackList = list()
schedulableList = list()
maxIntValue = 10000

thresholdFlag = 0

argumentList = sys.argv
argumentList.pop(0)


# Get arguments from input. Check if priority and threshold are given
for argument in argumentList:
	valueList = argument.split(" ")
	taskList.append(valueList[0])
	computationList.append(float(valueList[1]))
	periodList.append(float(valueList[2]))
	deadlineList.append(float(valueList[3]))
	stackList.append(float(valueList[4]))
	if(len(valueList) >= 6):
		arrivalList.append(float(valueList[5]))
	else:
		arrivalList.append(0)
	if(len(valueList) == 8):
		priorityList.append(int(valueList[6]))
		thresholdList.append(int(valueList[7]))
		thresholdFlag = 1
	else:
		thresholdFlag = 0
		priorityList = list()
		thresholdList = list()


f = open('test.grasp','w')

# Function to calculate start time
def calStartTime(index, ci, priority, q):
	#print priorityList
	#print index, priority, q
	bi = 0
	cjSum = 0
	numInstSum = 0
	rhs = 0
	flag = 1
	tempList = list()
	numInstList = list()
	for i in range(len(taskList)):
		if(priority <= thresholdList[i] and priority > priorityList[i] and i != index):
			temp = computationList[i]
			if(temp > bi):
				bi = temp
	rhs = bi + (q-1)*ci
	#print "rhs = %s" %(rhs)
	for i in range(len(taskList)):
		if(priorityList[i] > priority):
			cjSum += computationList[i]
			tempList.append(taskList[i])
			numInstList.append(0)
	rhs += cjSum
	#print "rhs = %s" %(rhs)
	rhsTemp1 = rhs
	count = 0
	while(flag == 1):
		count += 1
		rhsTemp2 = rhsTemp1
		rhsTemp1 = rhs
		for i in range(len(tempList)):
			numInstList[i] = int(rhsTemp2/periodList[taskList.index(tempList[i])])
			#print "num = %s" %(numInstList[i]) 
			#print "startTime = %s" %(numInstList[i])
			rhsTemp1 += numInstList[i] * computationList[taskList.index(tempList[i])]
		if(rhsTemp1 == rhsTemp2 or rhsTemp1 >= maxIntValue or count > 100):
			flag = 0
	#print "returnValue = %s" %(rhsTemp1)		
	return rhsTemp1
		
# Function to calculate finish time
def calFinishTime(index, ci, priority, si, q):
	#print priorityList
	#print thresholdList
	#print index, ci, priority, si, q
	tempList = list()
	numInstList = list()
	rhs = si + ci
	#print rhs
	flag = 1
	subValue = 0
	for i in range(len(taskList)):
		if(priorityList[i] > thresholdList[index]):
			tempList.append(taskList[i])
			numInstList.append(0)
	#print tempList
	rhsTemp1 = rhs
	count = 0
	while(flag == 1):
		rhsTemp2 = rhsTemp1
		rhsTemp1 = rhs
		count += 1
		for i in range(len(tempList)):
			if(math.ceil(rhsTemp1 % periodList[taskList.index(tempList[i])]) == 0): #and int((si/periodList[taskList.index(tempList[i])])) == 0):
				subValue = 1 
			else:
				subValue = 0
			#print subValue	
			numInstList[i] = (int(rhsTemp2/periodList[taskList.index(tempList[i])]) - subValue) - int((si/periodList[taskList.index(tempList[i])])) #changed equation
			#print "finishTime = %s" %(numInstList[i])
			rhsTemp1 += numInstList[i] * computationList[taskList.index(tempList[i])]
		if(rhsTemp1 == rhsTemp2 or rhsTemp1 >= maxIntValue or count > 100):
			flag = 0
		#print count	
	#print "FinishTime = %s" %(rhsTemp1)		
	return rhsTemp1

	
# Function to calculate the range of instances for a task (level-i busy period analysis)	
def calLi(index, ci, priority):
	#print index, ci, priority;
	tempList = list()
	numInstList = list()
	temp = 0
	bi = 0
	flag = 1
	numInst = 0
	rhsTemp1 = 0
	for i in range(len(taskList)):
		if(priority <= thresholdList[i] and priority > priorityList[i] and i != index):
			temp = computationList[i]
			if(temp > bi):
				bi = temp		
		if(priorityList[i] >= priority):
			tempList.append(taskList[i])
			numInstList.append(0)
			rhsTemp1 += computationList[i]
	bi += rhsTemp1
	#print bi
	while(flag == 1 and rhsTemp1/periodList[index] <= 100):
		rhsTemp2 = rhsTemp1
		rhsTemp1 = bi
		for i in range(len(tempList)):
			numInstList[i] = int(rhsTemp2/periodList[taskList.index(tempList[i])])
			#print "LiTime = %s" %(numInstList[i])
			rhsTemp1 += numInstList[i] * computationList[taskList.index(tempList[i])]
		if(rhsTemp1 == rhsTemp2):
			flag = 0
		
		#print rhsTemp2
	#print "Li = %s" %(rhsTemp1)		
	return rhsTemp1

# Function to get worst case response time of a task	
def taskWCRT(taskName):
	fList = list()
	bi = 0
	index = taskList.index(taskName)
	ci = computationList[index]
	priority = priorityList[index]
				
	li = calLi(index, ci, priority)
	q = 1
	
	while(q-1 <= li/periodList[index]):
		si = calStartTime(index, ci, priority, q)
		if(si >= maxIntValue):
			fi = si
			fList.append(fi)
			break
		else:
			fi = calFinishTime(index, ci, priority, si, q)
		fList.append(fi)
		q += 1	
	rList = list()
	for f in fList:
		rList.append(f - fList.index(f)*periodList[index])
	#print rList.index(max(rList))
	#print rList
	#print max(rList)
	#return max(rList)
	return [max(rList), rList.index(max(rList))]

# Function to assign priorities for a task - deadline monotonic ordering
def findPriority():  
	priorityDict = {}
	tempList = list()
	tempMax = -1
	priority = 1
	for i in range(len(deadlineList)):
		tempList.append(deadlineList[i])
	for i in range(len(taskList)):
		if(i+1 == priority):
			maxList = [j for j, x in enumerate(deadlineList) if x == max(tempList)]
			for k in maxList:
				priorityDict[taskList[k]] = priority
				priority += 1
				tempList.pop(tempList.index(max(tempList)))
	for task in taskList:
		priorityList.append(priorityDict[task])
	#print priorityList

# Function to assign preemption thresholds for tasks	
def findThreshold():
	orderedList = list()
	tempList = list()
	wcrtTh = 0
	for i in range(len(priorityList)):
		tempList.append(priorityList[i])
		thresholdList.append(len(priorityList))
	for i in range(len(taskList)):
		orderedList.append(taskList[priorityList.index(min(tempList))])
		tempList.remove(min(tempList))
	#print orderedList
	for task in orderedList:
		for i in range(priorityList[taskList.index(task)], len(priorityList)+1):
			thresholdList[taskList.index(task)] = i
			wcrtTh, indexWCRT = taskWCRT(task)
			if(wcrtTh <= deadlineList[taskList.index(task)]):
				#print taskWCRT(task)
				break

# function to find stack size for the task set with assigned/given priority and threshold values				
def findStackSize(task, stackSize, threshold, accList):
	premptList = [j for j, x in enumerate(priorityList) if x > threshold]
	for k in premptList:
		if(thresholdList[k] == max(priorityList)):
			accList.append(stackSize + stackList[k])
		else:
			accList = findStackSize(taskList[k], stackSize + stackList[k], thresholdList[k], accList)
	if(len(premptList) == 0):
		accList.append(stackSize)
	#print accList	
	return accList			

# function to calculate efficiency	
def calEffeciency():
	tempList = list()
	stackSizeList = list()
	thresholdFlag = 0
	FPPSStack = 0
	FPNPStack = max(stackList)
	for i in range(len(deadlineList)):
		tempList.append(thresholdList[i])
		FPPSStack += stackList[i]
	for task in taskList:
		stackSize = stackList[taskList.index(task)]
		threshold = thresholdList[taskList.index(task)]
		tempList2 = list()
		tempList2 = findStackSize(task, stackSize, threshold, tempList2)
		stackSizeList += tempList2
	#print stackSizeList
	effPercent = (1 - (max(stackSizeList) - FPNPStack) / float(FPPSStack - FPNPStack))*100
	return max(stackSizeList),effPercent		
		
# GCD and LCM to find runtime for Grasp trace		
def findGCD(a, b):
	temp = 0
	if(a < b):
		temp = a
		a = b
		b = temp
	while(b > 0):
		temp = b
		b = a%b
		a = temp
	return a

def findLCM(a, b):
		return (a * b/findGCD(a,b))

# Function to find execution time and minimum time increments during Grasp trace		
def timeUnits():
	executionTime = periodList[0]
	for i in (range(len(periodList) - 1)):
		executionTime = findLCM(executionTime, periodList[i+1])
	
	gcdList = computationList + deadlineList + periodList + arrivalList
	gcd1 = gcdList[0]
	for i in (range(len(gcdList) - 1)):
		gcd2 = gcdList[i+1]
		gcd1 = findGCD(gcd1, gcd2)
	timeIncrement = gcd1
	return executionTime, timeIncrement

# Initialize grasp recorder and start the grasp trace file	
def initialize():
	f.write("#Priority: %s = %s\n" %(taskList, priorityList))
	f.write("#Threshold: %s = %s\n" %(taskList, thresholdList))
	#f.write("#Schedulable: %s = %s\n" %(taskList, schedulableList))
	#f.write("#Stack size: %s\n" %(stackSize))
	#f.write("#Efficiency: %s\n" %(efficiency))
	for task in taskList:
		if(schedulableList[taskList.index(task)] == 0):
			f.write("newTask %s -name \"%s\" -arrivalColor blue -arrivalStem dotted -deadlineColor red -deadlineStem dotted -color red\n" %(task,task))
		else:
			f.write("newTask %s -name \"%s\" -arrivalColor blue -arrivalStem dotted -deadlineColor green -deadlineStem dotted -color green\n" %(task,task))
		instanceList.append(1)
	f.write("newServer server1 -name \"Stack Size (Efficiency = %s percent)\" -capacity %s -budget %s\n" %(efficiency, stackSize, stackSize))

# Grasp recorder to generate the trace for given task set	
def createGrasp():		
	arrivedDict = {}
	arrivedDict2 = {}
	arrivedList = list()
	arrivedDictTime = {}
	executeDict = {}
	deadlineJobs = list()
	runFlag = 0
	maxPriority = -1
	preempt = -1
	currentJob = "null"
	nextJob = "null"
	countdownTimer = -1.0
	preemptJobList = list()
	preemptCalTimeList = list()
	preemptThresholdList = list()
	preemptMax = -1
	t = 0
	previousStackSize = -1
	stackSizeGrasp = 0
	
	executionTime, timeIncrement = timeUnits()
	#print timeIncrement
	initialize()
	
	while(t <= executionTime):
		maxPriority = -1
		preemptMax = -1
		preemptMaxIndex = -1
		
		
		for task in taskList:
			if(t%periodList[taskList.index(task)] == arrivalList[taskList.index(task)]):
				jobArrived = "job"+str(taskList.index(task)+1)+"."+str(instanceList[taskList.index(task)])
				instanceList[taskList.index(task)] = instanceList[taskList.index(task)]+1
				arrivedDict[jobArrived] = task
				arrivedDict2[jobArrived] = task
				arrivedList.append(jobArrived)
				arrivedDictTime[jobArrived] = t
				f.write("plot %s jobArrived %s %s\n" %(t, jobArrived, task))
				#if(t+deadlineList[taskList.index(task)] <= executionTime):
					#f.write("plot %s jobDeadline %s\n" %(t+deadlineList[taskList.index(task)], jobArrived))
		if(countdownTimer == 0):
			f.write("plot %s jobCompleted %s\n" %(t, currentJob))
			#if(t - arrivedDictTime[currentJob] > deadlineList[taskList.index(executeDict[currentJob])]):
				#f.write("plot %s taskAnnotation %s %s -arrow both -color red -stem solid\n" %(arrivedDictTime[currentJob], executeDict[currentJob], t))
			stackSizeGrasp -= stackList[taskList.index(executeDict[currentJob])]
			currentJob = "null"
			preempt = -1
			
			if(len(preemptJobList) != 0):
				preemptMaxIndex = preemptThresholdList.index(max(preemptThresholdList))
				preemptMax = max(preemptThresholdList)
		
		for job in arrivedList:
			if(maxPriority < priorityList[taskList.index(arrivedDict[job])]):
				maxPriority = priorityList[taskList.index(arrivedDict[job])]
				runTask = arrivedDict[job]
				runJob = job
		if(maxPriority > preemptMax):
			if(maxPriority > preempt):
				nextJob = runJob
				if(currentJob != "null"):
					f.write("plot %s jobPreempted %s -target %s\n" %(t, currentJob, nextJob))
					preemptJobList.append(currentJob)
					preemptCalTimeList.append(countdownTimer)
					preemptThresholdList.append(preempt)
				f.write("plot %s jobResumed %s\n" %(t, nextJob))
				preempt = thresholdList[taskList.index(runTask)]
				currentJob = nextJob
				countdownTimer = computationList[taskList.index(runTask)]
				executeDict[nextJob] = runTask
				stackSizeGrasp += stackList[taskList.index(runTask)]
				del arrivedDict[nextJob]
				arrivedList.remove(nextJob)
		elif(len(preemptJobList) != 0 and preemptMaxIndex > preempt):
			nextJob = preemptJobList[preemptMaxIndex]
			f.write("plot %s jobResumed %s\n" %(t, nextJob))
			#stackSizeGrasp += stackList[taskList.index(executeDict[nextJob])]
			preempt = preemptThresholdList[preemptMaxIndex]
			currentJob = nextJob
			countdownTimer = preemptCalTimeList[preemptMaxIndex]
			preemptJobList.pop(preemptMaxIndex)
			preemptCalTimeList.pop(preemptMaxIndex)
			preemptThresholdList.pop(preemptMaxIndex)
		
		for job in arrivedList+preemptJobList:
			if((t - arrivedDictTime[job] >= deadlineList[taskList.index(arrivedDict2[job])]) and (not(job in deadlineJobs))):
				f.write("plot %s jobDeadline %s -arrowColor red -arrowStem solid\n" %(t, job))
				deadlineJobs.append(job)
			warningTime = deadlineList[taskList.index(arrivedDict2[job])] - (0.25 * deadlineList[taskList.index(arrivedDict2[job])])
			if(t - arrivedDictTime[job] >= warningTime and (not(job in deadlineJobs))):
				f.write("plot %s jobDeadline %s -arrowColor orange -arrowStem dotted\n" %(t, job))
		if(currentJob != "null"):
			if((t - arrivedDictTime[currentJob] >= deadlineList[taskList.index(arrivedDict2[currentJob])]) and (not(currentJob in deadlineJobs))):
				f.write("plot %s jobDeadline %s -arrowColor red -arrowStem solid\n" %(t, currentJob))
				deadlineJobs.append(currentJob)
			warningTime = deadlineList[taskList.index(arrivedDict2[currentJob])] - (0.25 * deadlineList[taskList.index(arrivedDict2[currentJob])])
			if(t - arrivedDictTime[currentJob] >= warningTime and (not(currentJob in deadlineJobs))):
				f.write("plot %s jobDeadline %s -arrowColor orange -arrowStem dotted\n" %(t, currentJob))
		if(previousStackSize != stackSizeGrasp):
			f.write("plot %s serverDepleted server1\n" %(t))
			f.write("plot %s serverReplenished server1 %s\n" %(t, stackSizeGrasp))
		previousStackSize = stackSizeGrasp
		
		t += timeIncrement
		countdownTimer -= timeIncrement 

# Get blocking time due to lower priority tasks		
def getBi(task):
	index = taskList.index(task)
	priority = priorityList[index]
	temp = 0
	bi = 0
	biReturn = ""
	for i in range(len(taskList)):
		if(priority <= thresholdList[i] and priority > priorityList[i] and i != index):
			temp = computationList[i]
			if(temp > bi):
				bi = temp
				biReturn = taskList[i]
	return biReturn
	
if(thresholdFlag == 0):		
	findPriority()
	findThreshold()
	
stackSize,efficiency =  calEffeciency()

# schedulableList.append(1)
# schedulableList.append(1)
# schedulableList.append(1)
# createGrasp()

#  check if arrival time needs to be adjusted to observe deadline miss (Tip)
for task in taskList:
	wcrt, iwcrt = taskWCRT(task)
	#print wcrt, iwcrt
	if wcrt > deadlineList[taskList.index(task)]:
		schedulableList.append(0)
		if(iwcrt == 0 and wcrt < maxIntValue):
			taskBi = getBi(task)
			f.write("#Tip: To observe deadline miss for %s, set arrival time of %s before %s\n" %(task, taskBi, task))
	else:
		schedulableList.append(1)

createGrasp()
print "Schedulabe = %s" %(schedulableList)
print "Priority = %s" %(priorityList)
print "Threshold = %s" %(thresholdList)
print "Stack Size = %s"	%(stackSize)
print "Efficiency = %s"	%(efficiency)


	
	
