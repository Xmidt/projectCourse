/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.dma.esim.virtualworld;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

/**
 *
 * @author mads
 */
public class VirtualWorld extends AbstractVirtualWorld {
    
    public VirtualWorld(Node rootNode, AssetManager assetManager, ViewPort viewPort){
        sky = new Sky(rootNode, assetManager);
        water = new Water(rootNode, viewPort, assetManager);
    }

}
