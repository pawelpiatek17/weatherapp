package dataStructure;

/** Class used with Gson to obtain city list from json */
public class City {
	private int id;
	private String name;
	private String country;
	private LatLng coord;
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getCountry() {
		return country;
	}
	public LatLng getCoord() {
		return coord;
	}
	@Override
	public String toString() {
		return name + "," + country;
	}
	
}
