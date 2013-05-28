package dk.dma.esim.virtualship;

import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import dk.dma.ais.message.AisMessage;

public interface IVirtualShip {

    //public void setPos(Position pos);

	/**
	 * Set the speed of the ship
	 * 
	 * @param speed
	 */
    public void setSpeed(int speed);

    /**
     * Current heading for, the dock.
     * 
     * @param cog
     */
    public void setCog(int cog);

    /**
     * Length of ship
     * 
     * @param length
     */
    public void setLength(int length);

    /**
     * Width of ship
     * 
     * @param width
     */
    public void setWidth(int width);

    //public void setType(ShipType type);

    /**
     * Mass of ship
     * 
     * @param mass
     */
    public void setMass(int mass);

    /**
     * Current AisMessage of ship.
     * 	For java library visit following link:
     * 		https://github.com/dma-dk/AisLib
     * 
     * 	AisMessage explanation:
     * 		http://en.wikipedia.org/wiki/Automatic_Identification_System
     * 		http://www.navcen.uscg.gov/?pageName=AISMessages
     * 
     * @param aisMessage	Obtained trough the dma proxy
     */
    public void setShipAisMessage(AisMessage aisMessage);

    /**
     * ship bow (forward part of the hull)
     * 
     * @param bow
     */
    public void setShipBow(int bow);

    /**
     * Port (left) of ship
     * 
     * @param port
     */
    public void setShipPort(int port);

    /**
     * Starboard (right) of ship
     * 
     * @param starboard
     */
    public void setShipStarboard(int starboard);

    /**
     * The stern is the rear or aft-most part of a ship
     * 
     * @param stern
     */
    public void setSHipStern(int stern);

    /**
     * Geographic latitude of ship
     * 
     * @param latitude
     */
    public void setShipLatitude(double latitude);

    /**
     * Geographic longitude of ship
     * 
     * @param longitude
     */
    public void setShipLongitude(double longitude);

    /**
     * Current rudder angle
     * 
     * @param angle
     */
    public void setRudderAngle(double angle);
    
    /**
     * New rudder angle
     * 
     * @param rateOfTurn
     */
    public void setRudderTurnSpeed(double rateOfTurn);
    
    /**
     * Incrementing the current rudder angle for sharper turns
     */
    public void incrementRudder();
    
    /**
     * Decrementing the current rudder angle for lesser turns
     */
    public void decrementRudder();
    
    /**
     * Setting the material of the ship, also known as the model
     * 
     * @param material
     */
    public void setMaterial(Material material);
    
    /**
     * The three-dimensional space of the ship, controlling the scale of the ship
     * 
     * @param spatial
     */
    public void setSpatial(Spatial spatial);
    
    /**
     * Set the speed of the ship
     * 
     * @param speed
     */
    public void setForwardSpeed(double speed);
    
    /**
     * Show the ship in the rendered world or not.
     * 
     * @param valid
     */
    public void setValid(boolean valid);
    
    //public Position getPos();

    /**
     * 
     * @return speed of ship
     */
    public int getSpeed();

    /**
     * 
     * @return Dock ID, for ship heading/destination
     */
    public int getCog();

    /**
     * 
     * @return length of ship
     */
    public int getLength();

    /**
     * 
     * @return width of ship
     */
    public int getWidth();

    //public ShipType getType();

    /**
     * 
     * @return mass of ship
     */
    public int getMass();

    /**
     * Current AisMessage of ship.
     * 	For java library visit following link:
     * 		https://github.com/dma-dk/AisLib
     * 
     * 	AisMessage explanation:
     * 		http://en.wikipedia.org/wiki/Automatic_Identification_System
     * 		http://www.navcen.uscg.gov/?pageName=AISMessages
     * 
     * @return AisMessage
     */
    public AisMessage getShipAisMessage();

    /**
     * 
     * @return forward part of the hull
     */
    public int getShipBow();

    /**
     * 
     * @return Port (left) of ship
     */
    public int getShipPort();

    /**
     * 
     * @return Starboard (right) of ship
     */
    public int getShipStarboard();

    /**
     * 
     * @return The stern is the rear or aft-most part of a ship
     */
    public int getShipStern();

    /**
     * 
     * @return Geographic Latitude of ship
     */
    public double getShipLatitude();

    /**
     * 
     * @return Geographic Longitude of ship
     */
    public double getShipLongitude();

    /**
     * 
     * @return Unique number ID for current ship
     */
    public int getShipMmsi();
    
    /**
     * 
     * @return rudder angle of ship
     */
    public double getRudderAngle();
    
    /**
     * 
     * @return The three-dimensional space of the ship, scaling
     */
    public Spatial getSpatial();
    
    /**
     * 
     * @return Current model of the ship
     */
    public Material getMaterial();
    
    /**
     * 
     * @return current nodes the object contains
     */
    public Node getNode();
    
    /**
     * 
     * @return Current speed of ship
     */
    public double getForwardSpeed();
    
    /**
     * Updates the ships location in the 3D rendered world.
     * 
     * Also increment or decrement the ship speed, and current rudder angle.
     */
    public void update();
    
    /**
     * 
     * @return true if ship is shown in the 3D rendered world
     */
    public boolean isValid();
}