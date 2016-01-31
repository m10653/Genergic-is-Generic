import java.awt.Desktop;
import java.net.InetSocketAddress;
import java.net.URI;
/**
 * 
 * @author Michael Combs
 *
 */
public class Main {
	public static HttpTrackingServer server;
	public static Console window = new Console();
	public static void main(String[] args){ 
	
		
		
		//--------Test for valid config options ----------------//
		try{
			InetSocketAddress test = new InetSocketAddress(Config.read("address"), Integer.parseInt(Config.read("port")));
			if(test.isUnresolved()){
				throw new IllegalArgumentException();
			}
			test = null;
			System.gc();
			window.sendInfo("Config Loaded");
		}catch(Exception e){
			window.sendError("Invalid Config File Error: "+ e);
			System.exit(1);
		}
		

		//Start HTTP Server//
		server = new HttpTrackingServer(Integer.parseInt(Config.read("port")),Config.read("address"));
		server.start();
		
		//Create and bind PackageHandler to HTTP Server//
		@SuppressWarnings("unused")
		PackageHandler packageHandler = new PackageHandler(server);
		
		//Open Web UI if Designated in config file
		if(Desktop.isDesktopSupported() && Boolean.parseBoolean(Config.read("openWebUIOnStartup"))) { //open web Gui if in single usermode TODO: better made for single usermode
		  try {
			Desktop.getDesktop().browse(new URI("Http://"+ Config.read("address")+":" + Config.read("port"))); // Open Web GUI in default browser.
			window.sendInfo("Web UI Opened");
		  }catch(Exception e){
			  window.sendWarning("Unable to open web GUI, Error: " + e);
		  }
		}
	}

}
