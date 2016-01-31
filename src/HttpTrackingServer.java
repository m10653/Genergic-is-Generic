import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
/**
 * 
 * @author Michael Combs
 *
 */
public class HttpTrackingServer{
	private int port;
	private String adress;
	private static List<HttpWebHandler> handler = new ArrayList<HttpWebHandler>();
	
	public HttpTrackingServer(int num, String ip) {
		port = num;
		adress = ip;

	}
	

	public void start() { // Starts A HTTP Server

		try {
			ResponseHandler responseHandle = new ResponseHandler();
			HttpServer server = HttpServer.create(new InetSocketAddress(adress, port), 500);
			server.createContext("/tracknewpackage", responseHandle);
			server.createContext("/packagetrackupdate", responseHandle);
			server.createContext("/", responseHandle);

			server.setExecutor(null); // creates a default executor
			server.start();
			Main.window.sendInfo("Server Hosting on " + server.getAddress());
		} catch (Exception e) {
			Main.window.sendError("Error Creating Http Server With Port " + port + " and ip " + adress + " \n Error: " + e);
			System.exit(0);
			
		}
		
	}
	
    public void addWebHandler(HttpWebHandler toAdd) {
    	handler.add(toAdd);
    }
    
	

	class ResponseHandler implements HttpHandler{
		String websitePath = Config.read("websitePath"); //TODO:Add Website not found Error
		boolean doWebCaching = Boolean.parseBoolean(Config.read("webCaching"));
		String encoding = Config.read("encoding");
		@Override
		
		public void handle(HttpExchange t) throws IOException {
			String response = "";
			String qry ="";
			Headers headers = t.getResponseHeaders();
			String url = t.getHttpContext().getPath();
			Main.window.sendInfo(url);
			
			if(url.equals("/")){ //Catch all Display GUI
				
				URI uri = t.getRequestURI();
				File file;
				if(uri.getPath().equals("/CSS/main.css")){ // Return CSS Content
					file = new File(websitePath +"\\CSS\\main.css");
					headers.add("Content-Type", "text/css");
				}else{
					file = new File(websitePath+"\\Tacker.html"); //Catch all other Uri's and Send to User-interface 
					headers.add("Content-Type", "text/html");
				}
				FileInputStream fin = new FileInputStream(file);
				OutputStream os = t.getResponseBody();
				
			
				t.sendResponseHeaders(200, 0);
				byte buf[] = new byte[4096];
				for (int n = fin.read(buf); n > 0; n = fin.read(buf)) { // Send File Bytes TODO: Add Caching of pages save HD reading time
					os.write(buf, 0, n); 
				}
				
				os.close();
				fin.close();
				
			}else if(url.equals("/tracknewpackage")){  //Track new package;
				
				response = "{ \"ackUUID\":\"["+Parser.parseUrl(t.getRequestURI().getQuery(), "uuid")+"]\" }"; // Send Response 
				//response = PackageHandler.add(t.getRequestURI().getQuery());
				headers.add("Content-Type", "application/json;charset="+encoding);
				t.sendResponseHeaders(200, 0);
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());
				os.close();
				
			}else{ // Package Updates
				InputStream in = t.getRequestBody();
				
				try {
					ByteArrayOutputStream out = new ByteArrayOutputStream(); // Get Post Body and sets qry to it;
					byte buf[] = new byte[4096];
					for (int n = in.read(buf); n > 0; n = in.read(buf)) {
						out.write(buf, 0, n);
					}
					qry = new String(out.toByteArray(), encoding);
				}finally{
						in.close();
				}
				t.sendResponseHeaders(200, -1);
				
			}
			

			if(!url.equals("/")){ // ONLY UPDATE IF HAS PACKAGE INFORMATION!!!!!!!!! Ignore CSS and HTML Page Loads
				for(HttpWebHandler wh:handler){ // Update Handlers
					wh.Update(t.getRequestURI(),qry);
				}
			}
			
			
		};
		
	}
}
