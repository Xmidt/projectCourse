package dk.dma.esim.virtualworld;

import dk.dma.ais.message.AisPositionMessage;
import dk.dma.esim.virtualship.AbstractVirtualShip;

public interface IVirtualWorld {

    //public void updatePos();
    //public void updateHeading();
    //public void setFullShipList(AisPositionMessage[] list);
    //public void addShip(AbstractVirtualShip ship);
	
	/**
	 * Contains:
	 * 	Light source
	 * 	Ambient light
	 * 	Skybox
	 * 
	 * @return	Returns the sky object
	 */
    public Sky getSky();
    
    /**
     * Contains:
     * 	3d rendered water or blue square representing water
     * 	3d rendered water light effects
     * 
     * @return	Returns the water object
     */
    public Water getWater();
}
