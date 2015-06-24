package au.com.yourcompany;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPServer {
	private Status status;
	private String msg;
	private ServerSocket ss;
	private String PHPPath;
	private String url;
	

	public void start(HTTPServerListener back) {
		
		try {
			if (ss == null || ss.isClosed()) {
				// Creating the Service
				ss = new ServerSocket(8001);
				back.onActionStart(Status.BEFORE_RUN);
				while (true) {
					Socket sock = ss.accept();
					
					// Receiving request
					back.onActionStart(Status.ON_REQUEST);
					BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					String line;
					
					// Getting the very first line to start the Framework (FacilMVC) properly
					line = reader.readLine();
					if (line != null && !line.isEmpty()) {
						Pattern p = Pattern.compile("(GET|POST) (.*?) HTTP/1.[01]");
						Matcher m = p.matcher(line);
						if (m.matches()) {
							// Getting the URL on second group
							url = m.group(2);
							url = url.substring(1); // Initial slash is not used 
						} else {
							break;
						}
					}
					while (line != null && !line.isEmpty()) {
						System.out.println(line); // Just printing out client's headers
						line = reader.readLine();
					}
					
					// Starting writer
					PrintWriter writer = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
					writer.println("HTTP/1.1 200 Ok");
					writer.println("Server: Jose's Server");
					writer.println("Date: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
					writer.println("Content-type: text/html; charset=utf8");
					
					// Calling PHP interpreter
					File f = new File(".");
					System.out.println();
					String command = String.format("%s %s/php/index.php %s", getPHPPath(), f.getAbsolutePath(), url);
					Process p = Runtime.getRuntime().exec(command);
					BufferedReader commandReader = new BufferedReader(new InputStreamReader(p.getInputStream()));
					
					// Flushing its results
					StringBuilder sb = new StringBuilder();
				    line = "";
				    while ((line = commandReader.readLine())!= null) {
						sb.append(line + "\n");
				    }				
					writer.println("Content-length: " + sb.length() + "\n");
					writer.print(sb);
					writer.flush();
					writer.close();
				}
			}
		} catch (Exception e) {
			back.onActionStart(Status.ON_SOCKET_FAILURE);
			setMsg(e.getMessage());
			e.printStackTrace();
		}
	}

	public void stop(HTTPServerListener back) {
		back.onActionStop(Status.BEFORE_STOP);
		try {
			if (ss != null && !ss.isClosed()) {
				ss.close();
				ss = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Status getRunning() {
		return status;
	}

	public void setRunning(Status status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getPHPPath() {
		return PHPPath;
	}

	public void setPHPPath(String pHPPath) {
		PHPPath = pHPPath;
	}

	public static enum Status {
		BEFORE_RUN, BEFORE_STOP, ON_REQUEST, ON_SOCKET_FAILURE,
		BEFORE_EXECUTE
	}
}
