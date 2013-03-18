package virtual.world;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

/**
 * test
 *
 * @author normenhansen
 */
public class Water extends SimpleApplication {

    private Vector3f lightDir = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);
    private WaterFilter water;
	
	private Node ship;

    public static void main(String[] args) {
    	/*
    	 * Graphical settings
    	 */
    	AppSettings settings = new AppSettings(true);
    	settings.setResolution(640,480);
    	settings.setFrameRate(30);
    	settings.setSamples(0);
    	
        Water app = new Water();
        app.setSettings(settings);
        app.setShowSettings(false);		// Disable the settings menu at start
        app.start();
    }

    @Override
    public void simpleInitApp() {
    	/*
    	 * Camera settings
    	 */
    	flyCam.setMoveSpeed(50);
    	cam.setFrustumFar(4000);
        cam.setLocation(new Vector3f(0f, 21f, 22f));
        cam.setRotation(new Quaternion(0.003f, 0.94f, -0.35f, 0f));

        /*
         * Turn off status window
         */
        setDisplayFps(false);        
        setDisplayStatView(false);
     
        /*
         * Creates node for water and skybox
         */
        Node mainScene = new Node("Main Scene");
        rootNode.attachChild(mainScene);
        
        /*
         * Creates private node to handle ship local envoriment
         */
        ship = new Node("Virtual ship");
        rootNode.attachChild(ship);
            
        /*
         * Load custom keybindings
         */
        initKeys();
            
        /*
         * Skybox settings
         */
        Spatial sky = SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", false);
        sky.setLocalScale(350);
        mainScene.attachChild(sky);
            
        /*
         * Draws the water
         */
        water = new WaterFilter(rootNode, lightDir);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(water);
        viewPort.addProcessor(fpp);
        
        /*
         * Creates a cubic model to represent the ship
         */
		Geometry shipBlock = new Geometry("ship", new Box(Vector3f.ZERO, 7, 2, 3));
        Material shipBlockColor = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        shipBlockColor.setColor("Color", ColorRGBA.Blue);
        shipBlock.setMaterial(shipBlockColor);
        shipBlock.move(1, 1, 1);
        
        Geometry starboard = new Geometry("starboard", new Box(Vector3f.ZERO, 1, 1, 1));
        Material starboardColor = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        starboardColor.setColor("Color", ColorRGBA.Green);
        starboard.setMaterial(starboardColor);
        starboard.move(3, 4, 4);
        
        Geometry port = new Geometry("port", new Box(Vector3f.ZERO, 1, 1, 1));
        Material portColor = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        portColor.setColor("Color", ColorRGBA.Red);
        port.setMaterial(portColor);
        port.move(3, 4, -2);
        
        ship.attachChild(shipBlock);
        ship.attachChild(starboard);
        ship.attachChild(port);
    }
    
    /*
     * Automatic updater
     */
    @Override
    public void simpleUpdate(float tpf) {
    	
    }
        
    /* 
     * Custom Keybinding:
     * 	Map named actions to inputs.
     */
    private void initKeys() {
    	/*
    	 * Labels the keybindings
    	 */
        inputManager.addMapping("Up",  new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("Down",  new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Left",   new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Right",  new KeyTrigger(KeyInput.KEY_L));

        /*
         * Group the keybindings together in a string array
         */
        inputManager.addListener(analogListener, new String[]{"Up", "Down", "Left", "Right"});
       
    }
    
    /*
     * Controls the node "ship", according to user input.
     * 
     * TODO
     * 	When rotating, the ship only moves along 1 axes.
     */
    private AnalogListener analogListener = new AnalogListener() {
      	public void onAnalog(String name, float value, float tpf) {
       		if (name.equals("Up")) {
       			Vector3f v = ship.getLocalTranslation();
       			ship.setLocalTranslation(v.x + value*speed, v.y, v.z);
       		}
       		if (name.equals("Down")) {
       			Vector3f v = ship.getLocalTranslation();
       			ship.setLocalTranslation(v.x - value*speed, v.y, v.z);
       		}
       		if (name.equals("Right")) {
       			ship.rotate(0, value*speed, 0);
       	    }
       	    if (name.equals("Left")) {
       	    	ship.rotate(0, -value*speed, 0);
       	    }
       	}
    };
}