package dk.dma.esim.worldview;

public interface IWorldView {
    
	/**
	 * List of current ships object
	 * 
	 * @param list
	 */
    public void setShips(Object[] list);
    
    /**
     * Field of view, currently not added
     * Thoughts here is limiting the FOV, when camera is fixed on the bridge.
     * 
     * @param fov
     */
    public void setFOV(int fov);
    
    /**
     * Current maximum distance to draw real world ships
     * 
     * @param dist
     */
    public void setViewDistance(int dist);
    
    
}
