package dk.dma.esim.ais;

public class AisShipPosition {

	/**
	 * Geographic coordinates where the boat were spawned
	 */
	private double latitude;
	private double longitude;
	
	/**
	 * Current geographic coordinates, which are used to keep track 
	 */
	private double currentLatitude;
	private double currentLongitude;
	
	/**
	 * Parameters to controlling:
	 * 	scale => Controlling the scale conversions between real coordinates, and the virtual spatial coordinates
	 * 	draw  => How far away from the current ship to draw real ships (given in arc minutes)
	 * 
	 * For a real world perspective:
	 * Set scale to 10000.
	 */
	private int scale = 1000;
	private double drawDistance = 0.2;
	
	/**
	 * Initiate the virtual ships geographical coordinates, later used as a reference point.
	 * 
	 * @param latitude
	 * @param longitude
	 */
	public AisShipPosition(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		
		this.currentLatitude = latitude;
		this.currentLongitude = longitude;
	}
	
	/**
	 * Check if the current real world ship, is "close enough" to the virtual ship.
	 * 
	 * @param aisLatitude
	 * @param aisLongitude
	 * @return if the ship should be drawn ´true´, otherwise ´false´
	 */
	public boolean checkAis(double aisLatitude, double aisLongitude) {
		double diffZ = Math.abs(aisLatitude - currentLatitude);
		double diffX = Math.abs(aisLongitude - currentLongitude);
		if (diffX < drawDistance && diffZ < drawDistance) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Calculates the virtual ship "new" geographical coordinates,
	 * by using the initiated geographical coordinates defined in the constructor as a reference point,
	 * and the current spatial values from JME.
	 * 
	 * @param virtualSpatialX virtual ship JME spatial x axe value
	 * @param virtualSpatialZ virtual ship JME spatial z axe value
	 */
	public void newCurrentCoordinates(float virtualSpatialX, float virtualSpatialZ) {
		currentLatitude = latitude + (virtualSpatialZ / scale);
		currentLongitude = longitude + (-1 * (virtualSpatialX / scale));
	}
	
	/**
	 * 
	 * @return virtual ship current geographical longitude coordinate,
	 * calculated from the initiated longitude coordinate, and current JME spatial z axe value.
	 */
	public double getCurrentLatitude() {
		return currentLatitude;
	}
	
	/**
	 * 
	 * @return virtual ship current geographical longitude coordinate,
	 * calculated from the initiated longitude coordinate, and current JME spatial z axe value.
	 */
	public double getCurrentLongitude() {
		return currentLongitude;
	}
	
	/**
	 * 
	 * @return virtual ship initiated geographical latitude coordinate
	 */
	public double getLatitude(){
		return latitude;
	}
	
	/**
	 * 
	 * @return virtual ship initiated geographical longitude coordinate
	 */
	public double getLongitude(){
		return longitude;
	}
	
	/**
	 * 
	 * @param longitude
	 * @return float value for the spatial x axe, to place the ais ship in JME
	 */
	public float getAisSpatialX(double longitude) {
		float spatialX = (float) (this.longitude - longitude);
		return spatialX * scale;
	}
	
	/**
	 * 
	 * @param latitude
	 * @return float value for the spatial z axe, to place the ais ship in JME
	 */
	public float getAisSpatialZ(double latitude) {
		float spatialZ = (float) (-1 * (this.latitude - latitude));
		return spatialZ * scale;
	}

}
