

//2015-12-08T08:42:33.188-05:00
/**
 * 
 * @author Michael Combs
 *
 */
public class Package {
	private double desLat, desLon, curLat, curLon, dist;
	private String name;

	public Package(double destinationLat, double destinationLon, String n){
		name = n;
		desLat =destinationLat; // Parse Double from Lat lon
		desLon = destinationLon;

	}
	public void update(double lat, double lon){
		curLat = lat;
		curLon = lon;
		double theta = curLon - desLon;
		dist = Math.sin(Math.toRadians(curLat)) * Math.sin(Math.toRadians(desLat)) + Math.cos(Math.toRadians(curLat)) * Math.cos(Math.toRadians(desLat)) * Math.cos(Math.toRadians(theta));
		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);
		dist = dist * 60 * 1.1515;
		
	}
	public double[] getLocation(){
		return new double [] {curLat,curLon};
	}
	public double[] getDestination(){
		return new double [] {desLat, desLon};
		
	}
	public String getName(){
		return name;
	}
	public double getDis(){
		return dist;
	}
	

}
