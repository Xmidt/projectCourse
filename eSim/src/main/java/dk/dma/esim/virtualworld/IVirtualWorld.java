package dk.dma.esim.virtualworld;

import dk.dma.ais.message.AisPositionMessage;
import dk.dma.esim.virtualship.VirtualShip;

public interface IVirtualWorld {

    public void updatePos();
    public void updateHeading();
    public void setFullShipList(AisPositionMessage[] list);
    public void addShip(VirtualShip ship);
    //more
}
