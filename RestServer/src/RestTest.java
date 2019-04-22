/*
 * This program implements the backend architecture of the GRASP - HUE lighting System.
 * 
 * GOAL:
 * 		- The Bulbs will be addressed in terms of their IP address. 
 * 		- Re-combine and send appropriate messages to the HUE Bridge
 * PREREQUESITIES:
 * 		- The IP address of the bridge and the bulbs are known before.
 * 		- The Network adapter is configured such that the IP's will be redirected to the same computer.
 * 		- The incoming request always comes in the format 
 * 			http://hostaddress:portnumber/RestTest?effeciency=(int)&status=job(Resumed|.....|Completed
 * ASSUMPTION:
 * 		- The IP address of the bridge is 192.168.0.190
 * 		- Three IP's for three different bulbs are 192.168.0.191-193.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RestTest
 */
@WebServlet("/RestTest")
public class RestTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public RestTest() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// This function will be called when (http://url:port/RestTest) is called
		response.setContentType("text/html"); //Setting the output as HTML
		PrintWriter out =response.getWriter();
		out.println("Incoming request:  ");
		int bulb = 0;
		int efficiency =0;
		String eff = request.getParameter("efficiency");
		if(eff!=null){
			efficiency = Integer.parseInt(eff);
		}
		
		String bulbAddress=request.getRequestURL().substring(7, 20); // Gets the incoming IP address
		
		boolean allok = true;
		//Get Parameters
		
		String status = request.getParameter("status");
		if(status==null){
			allok=false;
		}
		//Check the IP of the incoming request and decide on the bulb
		if(bulbAddress.equals("192.168.0.191")){
			bulb=1;
		} else if(bulbAddress.equals("192.168.0.192")) {
			bulb=2;
		} else if(bulbAddress.equals("192.168.0.193")) {
			bulb=3;
		}
		else{
			allok=false;
		}
		
		out.println("bulb:"+bulb +" @ Status =" + status +" effeciency = "+efficiency+"all OK = " + allok);
		//Prints the above line if the request is processed successfully
		
		String Payload = null;
		
		//Build the payload for the Philips HUE
		if (allok){
			if(status.equals("jobArrived")){
				Payload ="{\"on\":true, \"sat\":254, \"bri\":"+efficiency+ ",\"hue\":46920}";
			}else if(status.equals("jobAboutMiss")){
				Payload = "{\"on\":true, \"sat\":254, \"bri\":"+efficiency+",\"hue\":8000}";
			}else if(status.equals("jobCompleted")){
				Payload = "{\"on\":true, \"sat\":0, \"bri\":"+efficiency+",\"hue\":0}";
			}else if(status.equals("jobResumed")){
				Payload = "{\"on\":true, \"sat\":255, \"bri\":"+efficiency+",\"hue\":25500}";
			}else if(status.equals("jobMissedDeadline")){
				Payload = "{\"on\":true, \"sat\":255, \"bri\":"+efficiency+",\"hue\":0}";
			}else if(status.equals("jobPreempted")){
				Payload = "{\"on\":true, \"sat\":255, \"bri\":"+efficiency+",\"hue\":56100 }";
			}else{
				System.out.println(" INVALID"+status+" INVALID");
				out.println("Invalid Command  ");
			}
			//System.out.println(bulb +"\n"+ Payload);
			sendPUT(bulb,Payload);//Sends the appropriate request to the HUE Bridge
			
		}
		System.out.println(request.getRequestURL());
		//sendPUT();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	//Not used FUNCTION <- Intentionaly kept for future extensions.. Can be used to get the present status of the bulbs
	private static final String USER_AGENT = "Mozilla/5.0";
	 private static void sendGET() throws IOException {
	        URL obj = new URL("http://192.168.0.190/api/3155b2e1160b129727051e1577090ab/lights/1/state");
	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	        con.setRequestMethod("GET");
	        con.setRequestProperty("User-Agent", USER_AGENT);
	        int responseCode = con.getResponseCode();
	        //System.out.println("GET Response Code :: " + responseCode);
	        if (responseCode == HttpURLConnection.HTTP_OK) { // success
	            BufferedReader in = new BufferedReader(new InputStreamReader(
	                    con.getInputStream()));
	            String inputLine;
	            StringBuffer response = new StringBuffer();
	 
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	 
	            // print result
	            //System.out.println(response.toString());
	        } else {
	            System.out.println("GET request not worked");
	        }
	 
	    }
	 //Sends the PUT request to the HUE with the appropriate Payload.
	 private static void sendPUT(int bulb, String payload) throws IOException {
	        URL obj = new URL("http://192.168.0.190/api/3155b2e1160b129727051e1577090ab/lights/"+bulb+"/state");
	       // System.out.println("Command to"+bulb);
	        //String payload = "	{\"on\":true, \"sat\":254, \"bri\":0,\"hue\":10000}";
	        Random random = new Random();
	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	        con.setRequestMethod("PUT");
	        con.setDoOutput(true);
	        con.setRequestProperty("Content-Type", "application/json");
	        con.setRequestProperty("Accept", "application/json");
	        con.setRequestProperty("User-Agent", USER_AGENT);
	        
	        OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream());
	        osw.write(String.format(payload, random.nextInt(30), random.nextInt(20)));
	        osw.flush();
	        osw.close();
	        System.err.println(con.getResponseCode());
	    
	 
	        int responseCode = con.getResponseCode();
	     //   System.out.println("GET Response Code :: " + responseCode);
	        if (responseCode == HttpURLConnection.HTTP_OK) { // success
	            BufferedReader in = new BufferedReader(new InputStreamReader(
	                    con.getInputStream()));
	            String inputLine;
	            StringBuffer response = new StringBuffer();
	 
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	 
	            // print result
	           // System.out.println(response.toString());
	        } else {
	            System.out.println("GET request not worked");
	        }
	 
	    }

}
