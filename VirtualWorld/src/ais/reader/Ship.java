package ais.reader;

import dk.dma.ais.message.AisMessage;

public class Ship {

	private int mmsi;
	
	private int bow;
	private int port;
	private int starboard;
	private int stern;
	
	private double latitude;
	private double longitude;
	
	private AisMessage aisMessage;
	
	Ship(int mmsi) {
		this.mmsi = mmsi;
	}
	
	Ship(int mmsi, AisMessage aisMessage) {
		this.mmsi = mmsi;
		this.aisMessage = aisMessage;
	}
	
	Ship(int mmsi, double latitude, double longitude) {
		this.mmsi = mmsi;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	Ship(int mmsi, double latitude, double longitude, AisMessage aisMessage) {
		this.mmsi = mmsi;
		this.latitude = latitude;
		this.longitude = longitude;
		this.aisMessage = aisMessage;
	}
	
	public AisMessage getShipAisMessage() {
		return aisMessage;
	}
	
	public int getShipBow() {
		return bow;
	}
	
	public int getShipPort() {
		return port;
	}
	
	public int getShipStarboard() {
		return starboard;
	}
	
	public int getShipStern() {
		return stern;
	}
	
	public double getShipLatitude() {
		return latitude;
	}
	
	public double getShipLongitude() {
		return longitude;
	}
	
	public int getShipMmsi() {
		return mmsi;
	}
	
	public void setShipAisMessage(AisMessage aisMessage) {
		this.aisMessage = aisMessage;
	}
	
	public void setShipBow(int bow) {
		this.bow = bow;
	}
	
	public void setShipPort(int port) {
		this.port = port;
	}
	
	public void setShipStarboard(int starboard) {
		this.starboard = starboard;
	}
	
	public void setSHipStern(int stern) {
		this.stern = stern;
	}
	
	public void setShipLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public void setShipLongitude(double longitude) {
		this.longitude = longitude;
	}
}
