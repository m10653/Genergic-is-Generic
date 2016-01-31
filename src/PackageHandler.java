import java.net.URI;
import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author Michael Combs
 *
 */


public class PackageHandler {
	public Map<String, Package> packages = new HashMap<String, Package>();
	
	public PackageHandler(HttpTrackingServer server){
		server.addWebHandler(new PackageUpdates(this));
	}
	public String add(String qry){
		Map<String, String> values = Parser.parseUrl(qry);
		
		if(values.get("uuid")!= null && values.get("name") != null && values.get("destinationLat") != null && values.get("destinationLon") != null) { // makes sure all values are not null
			packages.put(values.get("uuid"), new Package(Double.parseDouble(values.get("destinationLat")), Double.parseDouble(values.get("destinationLon")),values.get("name")));
			return "{ \"ackUUID\":\"["+ values.get("uuid") +"]\" }";
		}else{
			System.out.println("(WARNING) Malformed Url input for New package: " + qry); 
			return "Error Malformed Request: " + qry;
		}
		
	
	}
	public Package get(String uuid){
		return packages.get(uuid);
	}
	public String[] getUUIDs(){
		return (String[]) packages.keySet().toArray();
	}
	public void close (String uuid){ // Cleans Memory When packages arrive
		packages.remove(uuid);
	}
	
	
	public static class PackageUpdates implements HttpWebHandler{
		private PackageHandler packHanlder;
		public PackageUpdates(PackageHandler handle) {
			packHanlder = handle;
		}

		@Override
		public void Update(URI requestURI, String qry) {
			if(qry == ""){// New Packages
				packHanlder.add(requestURI.getQuery());
			}else{// Package Updates
				Map<String, String> postTags =  Parser.parseBody(qry);
				String uuid = requestURI.getPath().substring(20);
				if(postTags.get("delivered").equals("true")){
					
					System.out.println(uuid);
					packHanlder.close(uuid);
				}
			}
		}
		
	}

}
