<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>SAN Seminar</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="apple-touch-icon" href="http://i.imgur.com/8v2owzh.png">
	<link rel="icon" sizes="192x192" href="http://i.imgur.com/8v2owzh.png"> 
	<link rel="manifest" href="manifest.json">
	<link rel="shortcut" href="http://i.imgur.com/8v2owzh.png" type="image/x-icon" />
	<link rel="stylesheet" href="index.css"/>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
</head>

<body>
	
	<form action="/Frontend/frontend" method="GET">
		<center><img src="http://i.imgur.com/8v2owzh.png"></center>
		
		<%
			if(!request.getParameter("tip").equals("abc")){
				out.println("<center><br><p style=\"color: blueviolet;\">"+request.getParameter("tip") +"</p><br></center>" );
	
			}
    		if(request.getParameter("efficiency")!=null){
    			out.println( "<br><br>Efficiency of the system : <input type=\"number\" value=\""+request.getParameter("efficiency")+"\" disabled><br>" );

    		}
		%>
		<h1>Task 1:</h1> <br>
		<%
			String task1Arrival="0";
    		if(request.getParameter("task1Arrival")!=null){
    			task1Arrival=request.getParameter("task1Arrival");
    		}
		%>
		Arrival Time:<input type="text" name="task1Arrival" value="<%=task1Arrival%>"><br>
		<%
			String task1Computation="0";
    		if(request.getParameter("task1Computation")!=null){
    			task1Computation=request.getParameter("task1Computation");
    		}
		%>
		Computation Time:<input type="number" name="task1Computation" value="<%=task1Computation%>"><br>
		<%
			String task1Period="0";
    		if(request.getParameter("task1Period")!=null){
    			task1Period=request.getParameter("task1Period");
    		}
		%>
		Period:<input type="number" name="task1Period" value="<%=task1Period%>"><br>
		<%
			String task1Deadline="0";
    		if(request.getParameter("task1Deadline")!=null){
    			task1Deadline=request.getParameter("task1Deadline");
    		}
		%>
		Deadline:<input type="number" name="task1Deadline" value="<%=task1Deadline%>"><br>
		<%
			String task1Stack="0";
    		if(request.getParameter("task1Stack")!=null){
    			task1Stack=request.getParameter("task1Stack");
    		}
		%>
		Stack Required:<input type="number" name="task1Stack" value="<%=task1Stack%>"><br>
		<%
    		if(request.getParameter("task1Priority")!=null){
    			out.println( "Priority : <input type=\"number\" name=\"task1Priority\" value=\""+request.getParameter("task1Priority")+"\" disabled><br>" );
    			out.println( "<input type=\"hidden\" name=\"task1Priority\" value=\""+request.getParameter("task1Priority")+ "\">");
    			out.println("Threshold: &nbsp;&nbsp; <select name=\"task1Throshold\">");
    			for(int i=Integer.parseInt(request.getParameter("task1Priority")); i<=3;i++){
    				out.println("<option value=\""+i+"\">"+i+"</option>");
    			}
    			out.println("</select>");
    		}
		%>

		<br>
		<h1>Task 2:</h1> <br>
		<%
			String task2Arrival="0";
    		if(request.getParameter("task2Arrival")!=null){
    			 task2Arrival=request.getParameter("task2Arrival");
    		}
		%>
		Arrival Time:<input type="text" name="task2Arrival" value="<%=task2Arrival%>"><br>
		<%
			String task2Computation="0";
    		if(request.getParameter("task2Computation")!=null){
    			task2Computation=request.getParameter("task2Computation");
    		}
		%>
		Computation Time:<input type="number" name="task2Computation" value="<%=task2Computation%>"><br>
		<%
			String task2Period="0";
    		if(request.getParameter("task2Period")!=null){
    			task2Period=request.getParameter("task2Period");
    		}
		%>
		Period:<input type="number" name="task2Period" value="<%=task2Period%>"><br>
		<%
			String task2Deadline="0";
    		if(request.getParameter("task2Deadline")!=null){
    			task2Deadline=request.getParameter("task2Deadline");
    		}
		%>
		
		Deadline:<input type="number" name="task2Deadline" value="<%=task2Deadline%>" ><br>
		<%
			String task2Stack="0";
    		if(request.getParameter("task2Stack")!=null){
    			task2Stack=request.getParameter("task2Stack");
    		}
		%>
		Stack Required:<input type="number" name="task2Stack" value="<%=task2Stack%>"><br>
		<%
    		if(request.getParameter("task2Priority")!=null){
    			out.println( "Priority : <input type=\"number\"name=\"task2Priority\" value=\""+request.getParameter("task2Priority")+"\" disabled><br>" );
    			out.println( "<input type=\"hidden\" name=\"task2Priority\" value=\""+request.getParameter("task2Priority")+ "\">");
    			out.println("Threshold: &nbsp;&nbsp; <select name=\"task2Throshold\">");
    			for(int i=Integer.parseInt(request.getParameter("task2Priority")); i<=3;i++){
    				out.println("<option value=\""+i+"\">"+i+"</option>");
    			}
    			out.println("</select>");
    		}
		%>
		<br>
		<h1>Task 3:</h1> <br>
		<%
			String task3Arrival="0";
    		if(request.getParameter("task3Arrival")!=null){
    			task3Arrival=request.getParameter("task3Arrival");
    		}
		%>
		Arrival Time:<input type="text" name="task3Arrival" value="<%=task3Arrival%>"><br>
		<%
			String task3Computation="0";
    		if(request.getParameter("task3Computation")!=null){
    			task3Computation=request.getParameter("task3Computation");
    		}
		%>
		Computation Time:<input type="number" name="task3Computation" value="<%=task3Computation%>"><br>
		<%
			String task3Period="0";
    		if(request.getParameter("task3Period")!=null){
    			task3Period=request.getParameter("task3Period");
    		}
		%>
		Period:<input type="number" name="task3Period" value="<%=task3Period%>"><br>
		<%
			String task3Deadline="0";
    		if(request.getParameter("task3Deadline")!=null){
    			task3Deadline=request.getParameter("task3Deadline");
    		}
		%>
		Deadline:<input type="number" name="task3Deadline" value="<%=task3Deadline%>"><br>
		<%
			String task3Stack="0";
    		if(request.getParameter("task3Stack")!=null){
    			task3Stack=request.getParameter("task3Stack");
    		}
		%>
		Stack Required:<input type="number" name="task3Stack" value="<%=task3Stack%>"><br>
		<%
    		if(request.getParameter("task3Priority")!=null){
    			out.println( "Priority : <input type=\"number\" name=\"task3Priority\" value=\""+request.getParameter("task3Priority")+"\" disabled><br>" );
    			out.println( "<input type=\"hidden\" name=\"task3Priority\" value=\""+request.getParameter("task3Priority")+ "\">");
    			out.println("Threshold: &nbsp;&nbsp; <select name=\"task3Throshold\"> ");
    			for(int i=Integer.parseInt(request.getParameter("task3Priority")); i<=3;i++){
    				out.println("<option value=\""+i+"\">"+i+"</option>");
    			}
    			out.println("</select>");
    		}
		%>
		<br><br><center><button type="submit">View Simulation</button></center>
	</form>
</body>
</html>