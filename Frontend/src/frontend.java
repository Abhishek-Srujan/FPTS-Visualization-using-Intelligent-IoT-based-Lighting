/**
* This program implements the front end of the GRASP - HUE system.
*
* The main objective of this code is to
*	- Get the information pertaining to the tasks (from the submit of index.html)
*	- Generate the trace file with the information obtained (by calling the Python program with appropriate parameters obtained)
*	- Run Grasp with the trace file
*	- Parse the trace file (Generation of trace and Parsing the file are independent. Hence it can be decoupled based on requirement)
*	- Send GET messages to the bulb using its own IP address
*/
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class frontend
 */
@WebServlet("/frontend")
public class frontend extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public frontend() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	int efficiency = 0;// to hue
	int efficiencyto = 0;// to website
	int effmax = 0;
	int budget = 0;
	int task1Priority, task2Priority, task3Priority, task1Threshold, task2Threshold, task3Threshold = 0;
	String[] statusofbulbs = { "jobArrived", "jobArrived", "jobArrived" };
	int presentbulb = 0;
	String tip = "abc";

	/* This function will be called when the user presses the "View Simulation" button in index.html*/
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		/** Call the Python program with the incoming parameters of the 
			task. Threshold will be accepted as an input only on the second run of the program.
		  */
		if (request.getParameter("task1Computation") != null) {
			if (request.getParameter("task1Priority") == null) {
				String command = "C:/python27/python.exe C:/SAN/algo.py \"task1 "
						+ request.getParameter("task1Computation") + " " + request.getParameter("task1Period") + " "
						+ request.getParameter("task1Deadline") + " " + request.getParameter("task1Stack") + " "
						+ request.getParameter("task1Arrival") + "\" " + "\"task2 "
						+ request.getParameter("task2Computation") + " " + request.getParameter("task2Period") + " "
						+ request.getParameter("task2Deadline") + " " + request.getParameter("task2Stack") + " "
						+ request.getParameter("task2Arrival") + "\" " + "\"task3 "
						+ request.getParameter("task3Computation") + " " + request.getParameter("task3Period") + " "
						+ request.getParameter("task3Deadline") + " " + request.getParameter("task3Stack") + " "
						+ request.getParameter("task3Arrival") + "\"";
				System.out.println(command);
				String output = executeCommand(command);
			} else {
				String command = "C:/python27/python.exe C:/SAN/algo.py \"task1 "
						+ request.getParameter("task1Computation") + " " + request.getParameter("task1Period") + " "
						+ request.getParameter("task1Deadline") + " " + request.getParameter("task1Stack") + " "
						+ request.getParameter("task1Arrival") + " " + request.getParameter("task1Priority") + " "
						+ request.getParameter("task1Throshold") + "\" " + "\"task2 "
						+ request.getParameter("task2Computation") + " " + request.getParameter("task2Period") + " "
						+ request.getParameter("task2Deadline") + " " + request.getParameter("task2Stack") + " "
						+ request.getParameter("task2Arrival") + " " + request.getParameter("task2Priority") + " "
						+ request.getParameter("task2Throshold") + "\" " + "\"task3 "
						+ request.getParameter("task3Computation") + " " + request.getParameter("task3Period") + " "
						+ request.getParameter("task3Deadline") + " " + request.getParameter("task3Stack") + " "
						+ request.getParameter("task3Arrival") + " " + request.getParameter("task3Priority") + " "
						+ request.getParameter("task3Throshold") + "\"";
				System.out.println(command);
				String output = executeCommand(command);
			}

			/*By now the trace file test.grasp will be generated, we can read the file for the commands*/
			printWriter.println("<h2>Execution Success</h2>");
			String status;
			try {

				status = readFile("C:/SAN/test.grasp");
				printWriter.println("<h2>" + status + "</h2>");

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// if ((status).equals("success")){
			// printWriter.println("<h2>Mission Success</h2>");
			// }
			// else{

			// }

			// printWriter.println(output.toString());
			System.out.println("Simulation Over");
			/* Redirect the page to throshold.jsp so that all the parameters are once again populated. Also, options for varying the thresholds are provided on this page*/
			if(tip.equals("abc")){
				response.sendRedirect("http://192.168.0.102:8080/Frontend/throshold.jsp?efficiency=" + efficiencyto
					+ "&task1Priority=" + task1Priority + "&task2Priority=" + task2Priority + "&task3Priority="
					+ task3Priority + "&task1Throshold=" + task1Threshold + "&task2Throshold=" + task2Threshold
					+ "&task3Throshold=" + task3Threshold + "&task1Computation="
					+ request.getParameter("task1Computation") + "&task1Period=" + request.getParameter("task1Period")
					+ "&task1Deadline=" + request.getParameter("task1Deadline") + "&task1Stack="
					+ request.getParameter("task1Stack") + "&task1Arrival=" + request.getParameter("task1Arrival")
					+ "&task2Computation=" + request.getParameter("task2Computation") + "&task2Period="
					+ request.getParameter("task2Period") + "&task2Deadline=" + request.getParameter("task2Deadline")
					+ "&task2Stack=" + request.getParameter("task2Stack") + "&task2Arrival="
					+ request.getParameter("task2Arrival") + "&task3Computation="
					+ request.getParameter("task3Computation") + "&task3Period=" + request.getParameter("task3Period")
					+ "&task3Deadline=" + request.getParameter("task3Deadline") + "&task3Stack="
					+ request.getParameter("task3Stack") + "&task3Arrival=" + request.getParameter("task3Arrival")+"&tip=abc");
			
			
		     }
			else{
				response.sendRedirect("http://192.168.0.102:8080/Frontend/throshold.jsp?efficiency=" + efficiencyto
						+ "&task1Priority=" + task1Priority + "&task2Priority=" + task2Priority + "&task3Priority="
						+ task3Priority + "&task1Throshold=" + task1Threshold + "&task2Throshold=" + task2Threshold
						+ "&task3Throshold=" + task3Threshold + "&task1Computation="
						+ request.getParameter("task1Computation") + "&task1Period=" + request.getParameter("task1Period")
						+ "&task1Deadline=" + request.getParameter("task1Deadline") + "&task1Stack="
						+ request.getParameter("task1Stack") + "&task1Arrival=" + request.getParameter("task1Arrival")
						+ "&task2Computation=" + request.getParameter("task2Computation") + "&task2Period="
						+ request.getParameter("task2Period") + "&task2Deadline=" + request.getParameter("task2Deadline")
						+ "&task2Stack=" + request.getParameter("task2Stack") + "&task2Arrival="
						+ request.getParameter("task2Arrival") + "&task3Computation="
						+ request.getParameter("task3Computation") + "&task3Period=" + request.getParameter("task3Period")
						+ "&task3Deadline=" + request.getParameter("task3Deadline") + "&task3Stack="
						+ request.getParameter("task3Stack") + "&task3Arrival=" + request.getParameter("task3Arrival")+"&tip="+tip);
				
				
			}
		}
	}

	private String readFile(String fileName) throws IOException, InterruptedException {
		String graspRun = "C:/SAN/grasp_windows/grasp.exe C:/SAN/test.grasp";
		Process child = Runtime.getRuntime().exec(graspRun);

		String print = "";// This will reference one line at a time
		
		String line = null;
		String bulb1 = "http://192.168.0.191:8080/RestServer/RestTest?";
		String bulb2 = "http://192.168.0.192:8080/RestServer/RestTest?";
		String bulb3 = "http://192.168.0.193:8080/RestServer/RestTest?";
		String temp = "";

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);
			// Wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int previousTime = 0;
			int presentTime = 0;
			int effmax = 0;
			int iterator = 0;
			temp="";
			while ((line = bufferedReader.readLine()) != null) {
				/* This loop runs once for every line in the grasp trace, hence for each line with an relevant information, a message is transfered to the bulb */
				if (line.contains("#Tip:")) {
					
					temp=(tipfinal(line).substring(8))+"%0A";
					tip+=temp; 
					
					System.out.println(tip);
					continue;
				} else if (line.contains("newTask")) {
					if (line.contains("task1")) {
						print += bulb1;
					} else if (line.contains("task2")) {
						print += bulb2;
					} else {
						print += bulb3;
					}
					print += "efficiency=" + efficiency + "&";
					iterator += 1;
					int initdisp = line.indexOf("-deadlineColor", 0);
					int dcolorstart = line.indexOf(" ", initdisp);
					int dcolorend = line.indexOf(" ", dcolorstart + 1);
					// System.out.println(line+" " +dcolorstart+" "+dcolorend);

					// line.substring(dcolorstart, dcolorend
					if (line.substring(dcolorstart + 1, dcolorend).equals("green")) {
						print += "status=jobResumed";
					} else {
						print += "status=jobMissedDeadline";
					}
					if (iterator >= 3) {
						iterator = 0;
						sendGET(print);
						TimeUnit.SECONDS.sleep(10);
						sendGET(bulb1 + "efficiency=" + efficiency + "&" + "status=jobCompleted");
						sendGET(bulb2 + "efficiency=" + efficiency + "&" + "status=jobCompleted");
						sendGET(bulb3 + "efficiency=" + efficiency + "&" + "status=jobCompleted");

						TimeUnit.SECONDS.sleep(5);
					} else {
						sendGET(print);
						System.out.println(print);
						print = "";
					}
					continue;
				} else if (line.contains("newServer")) {
					int initmaxeff = line.indexOf("= ", 0);
					int maxeffstart = line.indexOf(" ", initmaxeff + 1);
					int maxeffend = line.indexOf(".", maxeffstart + 1);
					// int bud = line.indexOf("-budget");

					// System.out.println(line+" " +dcolorstart+" "+dcolorend);
					effmax = Integer.parseInt(line.substring(maxeffstart + 1, maxeffend));

					initmaxeff = line.indexOf("budget", 0);
					maxeffstart = line.indexOf(" ", initmaxeff + 1);
					maxeffend = line.indexOf(".", maxeffstart + 1);
					budget = Integer.parseInt(line.substring(maxeffstart + 1, maxeffend));

					efficiency = (int) ((effmax * 254) / 100);
					System.out.println("System Efficiency " + efficiency);
					efficiencyto = effmax;
					continue;
				} else if (line.contains("#Priority") | line.contains("#Threshold")) {
					if (line.contains("Priority")) {
						int initpriority = line.indexOf("= ");
						task1Priority = Integer.parseInt(String.valueOf(line.charAt(initpriority + 3)));
						task2Priority = Integer.parseInt(String.valueOf(line.charAt(initpriority + 6)));
						task3Priority = Integer.parseInt(String.valueOf(line.charAt(initpriority + 9)));
					}
					if (line.contains("Threshold")) {
						int initthreshold = line.indexOf("= ");
						task1Threshold = Integer.parseInt(String.valueOf(line.charAt(initthreshold + 3)));
						task2Threshold = Integer.parseInt(String.valueOf(line.charAt(initthreshold + 6)));
						task3Threshold = Integer.parseInt(String.valueOf(line.charAt(initthreshold + 9)));
					}
					continue;
				} else if (line.contains("serverReplenished")) {
					int initmaxeff = line.indexOf("server1", 0);
					int maxeffstart = line.indexOf(" ", initmaxeff + 1);
					int maxeffend = line.indexOf(".", maxeffstart + 1);
					int f = Integer.parseInt(line.substring(maxeffstart + 1, maxeffend));
					efficiency = (int) ((f * 254) / budget);
					System.out.println("Normal efficiency" + efficiency);
					sendGET(bulb3 + "efficiency=" + efficiency + "&" + "status=" + statusofbulbs[2]);
					// TimeUnit.MILLISECONDS.sleep(1);
					sendGET(bulb1 + "efficiency=" + efficiency + "&" + "status=" + statusofbulbs[0]);
					// TimeUnit.MILLISECONDS.sleep(1);
					sendGET(bulb2 + "efficiency=" + efficiency + "&" + "status=" + statusofbulbs[1]);
					// TimeUnit.MILLISECONDS.sleep(1);
					continue;

				} else if (line.contains("serverDepleted")) {
					continue;
				}

				temp = line;
				int m = line.indexOf(" ", 0);// Index Of first space- m
				// System.out.println("m: " + m);
				int n = line.indexOf(" ", m + 1);// Index of second space -n
				// System.out.println("n: " + n);
				int o = line.indexOf(" ", n + 1);// Index of third space - o
				// System.out.println("o: " + o);
				int p = line.indexOf(" ", o + 1);// Index of fourth space -p
				if (p != -1) {
					temp = line.substring(o + 1, p);// To Identify which Job
				}
				System.out.println(line + "-" + (m + 1) + "-" + n);
				presentTime = (int) Float.parseFloat(line.substring(m + 1, n));
				if (temp.contains("job1")) {
					print += bulb1;
					presentbulb = 0;
				} else if (temp.contains("job2")) {
					print += bulb2;
					presentbulb = 1;
				} else {
					print += bulb3;
					presentbulb = 2;
				}
				print += "efficiency=" + efficiency + "&";
				// Deadline Missed or About to Miss Deadline
				if (line.substring(n + 1, o).equals("jobDeadline")) {
					int arrowcolorindex = line.indexOf("-arrowColor", 0);
					int colorstart = line.indexOf(" ", arrowcolorindex);
					int colorend = line.indexOf(" ", colorstart + 1);
					if (line.substring(colorstart + 1, colorend).equals("red")) {
						print += "status=jobMissedDeadline";
						statusofbulbs[presentbulb] = "jobMissedDeadline";
					} else {
						print += "status=jobAboutMiss";
						statusofbulbs[presentbulb] = "jobAboutMiss";
					}

				} else {
					print += "status=" + line.substring(n + 1, o);
					statusofbulbs[presentbulb] = line.substring(n + 1, o);
				}

				TimeUnit.SECONDS.sleep((presentTime - previousTime));
				// With the populated URL the request to the server is made..  Sleeping time ensures that the command is sent at the appropriate time to the server
				sendGET(print);
				previousTime = presentTime;
				print = "";
			}

			// Close the file
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
			return "failure";
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
			return "failure";
		}
		// sendGET("http://192.168.0.191:8080/RestServer/RestTest?time=5&status=jobArrived");
		return print;
	}

	private static final String USER_AGENT = "Mozilla/5.0";

	/*Function to send GET message to the server*/

	private static void sendGET(String command) throws IOException {
		URL obj = new URL(command);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		// System.out.println("GET Response Code :: " + responseCode);
	}

	/*Function that executes the Python code, on behalf of the command prompt terminal*/

	private String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;

		try {
			p = Runtime.getRuntime().exec(command);
			new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(output.toString());

		return output.toString();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	//Function that removes all the spaces and replaces it with %20.
	public static String tipfinal(String s) {
		int i;
		if(s.equals("null")){
			return s;
		}
		for (i = 0; i < s.length(); i++) {
			if (s.charAt(i) == (int) 32) {
				s = s.substring(0, i) + "%20" + s.substring(i + 1, s.length());
				i = i + 2;
			}
		}
		return s;

	}

}
