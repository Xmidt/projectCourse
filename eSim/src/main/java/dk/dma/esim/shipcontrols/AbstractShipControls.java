package dk.dma.esim.shipcontrols;

import dk.dma.esim.virtualship.AbstractVirtualShip;

public abstract class AbstractShipControls implements IShipControls{
	AbstractVirtualShip ship;
	
	public AbstractShipControls(AbstractVirtualShip ship){
		this.ship = ship;
	}

    public void setSpeed(int speed){
    	ship.setSpeed(speed);
    }

    public void setHeading(int heading){
    	ship.setCog(heading);
    }
    	
    public int getCurrentSpeed(){
    	return ship.getSpeed();
    }

    public int getCurrentHeading(){
    	return ship.getCog();
    }
}
