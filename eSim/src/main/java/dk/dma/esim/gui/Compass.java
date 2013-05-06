/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.dma.esim.gui;

import com.jme3.asset.AssetManager;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

/**
 *
 * @author mads
 */
public class Compass {

    private Node compassNode;
    private Node guiNode;

    public Compass(AssetManager assetManager, Node guiNode, int windowHeight, int windowWidth) {
        /*
         * Creates a compass with needle, in JME gui interface
         */
        this.guiNode = guiNode;
        Picture pic = new Picture("Compass HUD");
        pic.setImage(assetManager, "Interface/compassWithBackground.png", true);
        pic.setWidth(windowHeight / 4);
        pic.setHeight(windowHeight / 4);
        pic.move(windowWidth - (windowHeight / 4), windowHeight - (windowHeight / 4), 0);
        guiNode.attachChild(pic);

        compassNode = new Node("Compass needle");
        guiNode.attachChild(compassNode);
        pic = new Picture("Compass needle HUD");
        pic.setImage(assetManager, "Interface/compassNeedle.png", true);
        pic.setWidth(windowHeight / 10);
        pic.setHeight(windowHeight / 10);
        pic.move(-windowHeight / 20, -windowHeight / 20, 0);
        compassNode.attachChild(pic);
        compassNode.move(windowWidth - (windowHeight / 7.7f), windowHeight - (windowHeight / 7.7f), 0);

    }

    public void rotate(Quaternion rotation) {    
        compassNode.setLocalRotation(new Quaternion(0,0,rotation.getY(), rotation.getW()));
    }
    
    
}
