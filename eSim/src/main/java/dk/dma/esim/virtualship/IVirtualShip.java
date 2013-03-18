/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.dma.esim.virtualship;

import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.ship.ShipType;

public interface IVirtualShip {
	
    public void setPos(Position pos);
    public void setSpeed(int speed);
    public void setCog(int cog);
    public void setLength(int length);
    public void setWidth(int width);
    public void setType(ShipType type);
    public void setMass(int mass);
    
    public Position getPos();
    public int getSpeed();
    public int getCog();
    public int getLength();
    public int getWidth();
    public ShipType getType();
    public int getMass();

    //public void loadAtributes(String path);
    
    //public double getTrueSpeed();
    //public int getTrueCog();
    
}