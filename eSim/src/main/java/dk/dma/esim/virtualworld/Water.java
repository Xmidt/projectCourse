package dk.dma.esim.virtualworld;

import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Node;
import com.jme3.water.WaterFilter;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Plane;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Team Echo
 */
public class Water {

    private Vector3f defaultLightDir = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);
    private WaterFilter water;
    private FilterPostProcessor fpp;
    private ViewPort viewPort;
    private Boolean enabled = false;
    private Node rootNode;
    private Geometry floorGeometry;
    private Material material;
    private Box floorBox;
    private Plane plane;

    /**
     * All objects used here is predefined in JME, only the blue colored box is not.
     * 
     * @param rootNode		The main Node in JME, which contains all objects/elements in the 3d rendered world
     * @param viewPort		Contains special elements in the 3d rendered world, in this case its the rendedred water
     * @param assetManager	Default library of JME models, textures and etc. etc.
     * @param 
     */
    public Water(Node rootNode, ViewPort viewPort, AssetManager assetManager) {
        this.rootNode = rootNode;
        this.viewPort = viewPort;
        this.water = new WaterFilter(this.rootNode, defaultLightDir);
        this.fpp = new FilterPostProcessor(assetManager);

        // Blue box representing water
        material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Blender/2.4x/textures/Grass_256.png", false)));
        material.setColor("Color", ColorRGBA.Blue);

        floorBox = new Box(140000, 0.25f, 1400000);
        plane = new Plane();
        plane.setOriginNormal(new Vector3f(0, -0.25f, 0), Vector3f.UNIT_Y);

        floorGeometry = new Geometry("Floor", floorBox);
        floorGeometry.setMaterial(material);
        floorGeometry.setLocalTranslation(0, -0.25f, 0);
        floorGeometry.addControl(new RigidBodyControl(new PlaneCollisionShape(plane), 0));

        rootNode.attachChild(floorGeometry);
    }

    /*
     * Define light source direction, for water reflection
     */
    public void setLightDirection(Vector3f direction) {
        this.water.setLightDirection(direction);
    }

    /*
     * Toggle how to draw the water:
     * 	Option 1: show a blue colored box which represents water, for better CPU performance.
     * 	Option 2: Rendered realistic water with waves.
     */
    public boolean toggleWater() {
        if (enabled) {
            viewPort.removeProcessor(fpp);
            rootNode.attachChild(floorGeometry);
        } else {
            rootNode.detachChild(floorGeometry);
            fpp.addFilter(water);
            viewPort.addProcessor(fpp);
        }
        enabled = !enabled;
        return enabled;
    }
}
