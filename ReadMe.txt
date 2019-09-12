In this project, the folder "Frontend" is implementation of the first Webserver and the folder "RestServer" represents the second WebServer. The Python code "Algo.py" is the implementation of the scheduling algorithm that produces the grasp trace.

-> Frontend
This folder implements the front end of the GRASP - HUE system. The main objective of this code is to
* Get the information pertaining to the tasks (from the submit of index.html)
* Generate the trace file with the information obtained (by calling the Python program with appropriate parameters obtained)
* Run Grasp with the trace file
* Parse the trace file (Generation of trace and Parsing the file are independent. Hence it can be decoupled based on requirement)
* Send GET messages to the bulb using its own IP address

-> RestServer
 This folder implements the backend architecture of the GRASP - HUE lighting System.
 * GOAL:
 - The Bulbs will be addressed in terms of their IP address. 
 - Re-combine and send appropriate messages to the HUE Bridge
 * PREREQUESITIES:
 - The IP address of the bridge and the bulbs are known before.
 - The Network adapter is configured such that the IP's will be redirected to the same computer.
 - The incoming request always comes in the format : http://hostaddress:portnumber/RestTest?effeciency=(int)&status=job(Resumed|.....|Completed
 * ASSUMPTION:
 - The IP address of the bridge is 192.168.0.190
 - Three IP's for three different bulbs are 192.168.0.191-193.

-> Algo.py
This python script implements the following functionalities:
- Calculate priority and threshold for each task
- Calculate schedulability and efficiency for the given task set
- Act as Grasp recorder to generate Grasp trace

-> Test.grasp
An example grasp file generated from the python script
