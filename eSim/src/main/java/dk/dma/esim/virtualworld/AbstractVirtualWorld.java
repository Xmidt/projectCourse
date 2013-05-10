package dk.dma.esim.virtualworld;

import dk.dma.ais.message.AisPositionMessage;
import dk.dma.enav.model.geometry.Position;

public abstract class AbstractVirtualWorld implements IVirtualWorld {
    
    protected AisPositionMessage[] fullShipList;
    protected Position position;
    protected int heading;
    protected Water water;
    protected Sky sky;
    
//    public void updatePos(Position pos){
//        this.position = pos;
//    }
//    
//    public void updateHeading(int heading){
//        this.heading = heading;
//    }
//    
//    public void setFullShipList(AisPositionMessage[] list){
//        this.fullShipList = list;
//    }
//    
//    public void updateVisible(){
//        
//    }
    
    public Sky getSky(){
        return this.sky;
    }
    
    public Water getWater(){
        return this.water;
    }
}
