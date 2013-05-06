package virtual.world;

public class Ship {

	private int drawDistance;		// How far to draw elements
	
	private float latitude;			// "Origin" North-South
	private float longitude;		// "Origin" West-East
	
	private float currentLatitude;	// The virtual ships current latitude
	private float currentLongitude;	// The virtual ships current longitude
	
	private int scale;				// Scale of the virtual world
	
	public Ship(){
		
	}
	
	public Ship(float latitude, float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public void setCoordinates(float latitude, float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		
		currentLatitude = latitude;
		currentLongitude = longitude;
	}
	
	public void setCurrentCooridnates(float latitude, float longitude) {
		this.currentLatitude = latitude;
		this.currentLongitude = longitude;
	}
	
	public void setDrawDistance(int arcMinute) {
		this.drawDistance = arcMinute;
	}
	
	public void setScale(int scale) {
		this.scale = scale;
	}
	
	public float getCurrentLatitude() {
		return this.currentLatitude;
	}
	
	public float getCurrentLongitude() {
		return this.currentLongitude;
	}
	
	public float getLatitude() {
		return this.latitude;
	}
	
	public float getLongitude() {
		return this.longitude;
	}
	
	public float getDrawDistance() {
		return this.drawDistance;
	}
	
	public int getScale() {
		return this.scale;
	}
	
}
