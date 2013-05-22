package dk.dma.esim.virtualworld;

public interface IVirtualWorld {
	
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
