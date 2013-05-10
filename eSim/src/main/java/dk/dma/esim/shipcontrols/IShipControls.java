package dk.dma.esim.shipcontrols;

public interface IShipControls {
    
	/**
	 * Set the speed of ship
	 * 
	 * @param speed
	 */
    public void setSpeed(int speed);
    
    /**
     * Set current heading (dock) for ship
     * 
     * @param heading
     */
    public void setHeading(int heading);
    
    /**
     * 
     * @return current speed of ship
     */
    public int getCurrentSpeed();
    
    /**
     * 
     * @return current heading (dock)
     */
    public int getCurrentHeading();
}
