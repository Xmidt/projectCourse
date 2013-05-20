package dk.dma.esim.ais;

import com.jme3.scene.Node;

import dk.dma.esim.virtualship.AbstractVirtualShip;

public class AisShip extends AbstractVirtualShip {
	
	public AisShip(){
		super();
	}
	
	public void setNode(String mmsi){
		this.mmsi = Integer.parseInt(mmsi);
		this.node = new Node(mmsi);
	}


}
