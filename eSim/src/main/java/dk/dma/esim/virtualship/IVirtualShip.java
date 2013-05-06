/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.dma.esim.virtualship;

import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import dk.dma.ais.message.AisMessage;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.ship.ShipType;

public interface IVirtualShip {

    //public void setPos(Position pos);

    public void setSpeed(int speed);

    public void setCog(int cog);

    public void setLength(int length);

    public void setWidth(int width);

    //public void setType(ShipType type);

    public void setMass(int mass);

    public void setShipAisMessage(AisMessage aisMessage);

    public void setShipBow(int bow);

    public void setShipPort(int port);

    public void setShipStarboard(int starboard);

    public void setSHipStern(int stern);

    public void setShipLatitude(double latitude);

    public void setShipLongitude(double longitude);

    public void setRudderAngle(double angle);
    
    public void setRudderTurnSpeed(double rateOfTurn);
    
    public void incrementRudder();
    public void decrementRudder();
    
    public void setMaterial(Material material);
    
    public void setSpatial(Spatial spatial);
    
    public void setForwardSpeed(double speed);
    
    public void setValid(boolean valid);
    
    //public Position getPos();

    public int getSpeed();

    public int getCog();

    public int getLength();

    public int getWidth();

    //public ShipType getType();

    public int getMass();

    public AisMessage getShipAisMessage();

    public int getShipBow();

    public int getShipPort();

    public int getShipStarboard();

    public int getShipStern();

    public double getShipLatitude();

    public double getShipLongitude();

    public int getShipMmsi();
    
    public double getRudderAngle();
    
    public Spatial getSpatial();
    
    public Material getMaterial();
    
    public Node getNode();
    
    public double getForwardSpeed();
    
    public void update();
    
    public boolean isValid();
}