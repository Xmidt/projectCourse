package dk.dma.esim.virtualworld;

import dk.dma.ais.message.AisPositionMessage;
import dk.dma.esim.virtualship.AbstractVirtualShip;

public interface IVirtualWorld {

    //public void updatePos();
    //public void updateHeading();
    //public void setFullShipList(AisPositionMessage[] list);
    //public void addShip(AbstractVirtualShip ship);
    public Sky getSky();
    public Water getWater();
    //more
}
