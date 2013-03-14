/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.dma.esim.virtualship;

import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.ship.ShipType;

public abstract class VirtualShip implements IVirtualShip {

    protected Position position;
    protected int length; 
    protected int width;
    protected ShipType type;
    protected int mass;
    protected int speed;    
    protected int cog;
    
    public VirtualShip(){
        this.position = Position.create(0.0, 0.0);
        this.length = 0;
        this.length = 0;
        this.type = ShipType.MILITARY;
        this.mass = 0;
        this.speed = 0;
        this.cog = 0;
    }
    
    public Position getPos(){
        return this.position;
    }
    
    public void setPos(Position pos){
        this.position = pos;
    }
            
    public void setSpeed(int speed){
        this.speed = speed;
    }
    
    public void setCog(int cog){
        this.cog = cog;
    }
    
    public void setLength(int length){
        this.length = length;
    }
    
    public void setWidth(int width){
        this.width = width;
    }
    
    public void setType(ShipType type){
        this.type = type;
    }
    public void setMass(int mass){
        this.mass = mass;
    }
    
    public int getSpeed(){
        return this.speed;
    }
    
    public int getCog(){
        return this.cog;
    }
    
    public int getLength(){
        return this.length;
    }
    public int getWidth(){
        return this.width;
    }
    public ShipType getType(){
        return this.type;
    }
    
    public int getMass(){
        return this.mass;
    }
    
    //public double getTrueSpeed();
    //public int getTrueCog();
}
