package dk.dma.esim.ais;

import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import dk.dma.ais.message.AisMessage;
import dk.dma.esim.virtualship.IVirtualShip;

public class AisShip implements IVirtualShip {

	private int length;
	private int width;
	
	private double latitude;
	private double longitude;
	
	private AisMessage aisMessage;
	
	AisShip(AisMessage aisMessage) {
	this.aisMessage = aisMessage;
	}
	
	public void setSpeed(int speed) {
		// TODO Auto-generated method stub
		
	}

	public void setCog(int cog) {
		// TODO Auto-generated method stub
		
	}

	public void setLength(int length) {
		this.length = length;		
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setMass(int mass) {
		// TODO Auto-generated method stub
		
	}

	public void setShipAisMessage(AisMessage aisMessage) {
		this.aisMessage = aisMessage;
	}

	public void setShipBow(int bow) {
		// TODO Auto-generated method stub
		
	}

	public void setShipPort(int port) {
		// TODO Auto-generated method stub
		
	}

	public void setShipStarboard(int starboard) {
		// TODO Auto-generated method stub
		
	}

	public void setSHipStern(int stern) {
		// TODO Auto-generated method stub
		
	}

	public void setShipLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setShipLongitude(double longitude) {
		this.longitude = longitude;
		
	}

	public void setRudderAngle(double angle) {
		// TODO Auto-generated method stub
		
	}

	public void setRudderTurnSpeed(double rateOfTurn) {
		// TODO Auto-generated method stub
		
	}

	public void incrementRudder() {
		// TODO Auto-generated method stub
		
	}

	public void decrementRudder() {
		// TODO Auto-generated method stub
		
	}

	public void setMaterial(Material material) {
		// TODO Auto-generated method stub
		
	}

	public void setSpatial(Spatial spatial) {
		// TODO Auto-generated method stub
		
	}

	public void setForwardSpeed(double speed) {
		// TODO Auto-generated method stub
		
	}

	public void setValid(boolean valid) {
		// TODO Auto-generated method stub
		
	}

	public int getSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getCog() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getLength() {
		return this.length;
	}

	public int getWidth() {
		return this.width;
	}

	public int getMass() {
		// TODO Auto-generated method stub
		return 0;
	}

	public AisMessage getShipAisMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getShipBow() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getShipPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getShipStarboard() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getShipStern() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getShipLatitude() {
		return this.latitude;
	}

	public double getShipLongitude() {
		return this.longitude;
	}

	public int getShipMmsi() {
		return this.aisMessage.getMsgId();
	}

	public double getRudderAngle() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Spatial getSpatial() {
		// TODO Auto-generated method stub
		return null;
	}

	public Material getMaterial() {
		// TODO Auto-generated method stub
		return null;
	}

	public Node getNode() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getForwardSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}

	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}
}
