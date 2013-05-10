package dk.dma.esim.virtualworld;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

/**
 *
 * @author Team Echo
 */
public class VirtualWorld extends AbstractVirtualWorld {
    
	/*
	 * Initiate the virtual world, with the skybox and water.
	 */
    public VirtualWorld(Node rootNode, AssetManager assetManager, ViewPort viewPort){
        sky = new Sky(rootNode, assetManager);
        water = new Water(rootNode, viewPort, assetManager);
    }

}
