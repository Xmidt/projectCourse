package dk.dma.esim.ais;

import com.jme3.scene.Node;

import dk.dma.esim.virtualship.AbstractVirtualShip;

public class AisShip extends AbstractVirtualShip {
	
	private Integer shipHeading;
	private Integer shipType;
	
	private String shipName;
	
	public AisShip(){
		super();
	}
	
	public void setNode(String mmsi){
		this.mmsi = Integer.parseInt(mmsi);
		this.node = new Node(mmsi);
	}
	
	public void setName(String name){
		this.shipName = name;
	}
	
	public void setShipHeading(Integer heading){
		this.shipHeading = heading;
	}
	
	public void setShipType(Integer type){
		this.shipType = type;
	}

	public String getName(){
		return this.shipName;
	}
	
	public Integer getShipHeading(){
		return this.shipHeading;
	}
	
	public Integer getShipType(){
		return this.shipType;
	}
	
}
