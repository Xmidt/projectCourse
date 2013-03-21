package virtual.world;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
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

    private Vector3f lightDir = new Vector3f(-4.9236743f,
-1.27054665f, 5.896916f);
    private WaterFilter water;
    private Node ship;
    private CameraNode camNode;
    private boolean useWater;
    private boolean fixedCamera;
    Geometry FloorBlock;

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
        app.setShowSettings(false);     // Disable the settings menu at start
        app.start();
    }

    @Override
    public void simpleInitApp() {

    /*
     * Variables for toggling camera and water
     */

    useWater = true;
    fixedCamera = false;

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
        Spatial sky = SkyFactory.createSky(assetManager,
"Scenes/Beach/FullskiesSunset0068.dds", false);
        sky.setLocalScale(350);
        mainScene.attachChild(sky);

        /*
         * Draws the water
         */
        water = new WaterFilter(rootNode, lightDir);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(water);
        viewPort.addProcessor(fpp);
        
        //define the waterreplacement box        
		FloorBlock = new Geometry("Floor", new Box(Vector3f.ZERO, 1000, 10, 1000));
        Material shipBlockColor = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        shipBlockColor.setColor("Color", ColorRGBA.Blue);
        FloorBlock.setMaterial(shipBlockColor);
        FloorBlock.move(0f, -9.3f, 0f);
        
        // Load a model from test_data (OgreXML + material + texture)
        Spatial boat = assetManager.loadModel("Models/Boat/boat.mesh.xml");

        Material wood = new Material(
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            wood.setTexture("ColorMap",
                assetManager.loadTexture("Models/Boat/boat.png"));
            boat.setMaterial(wood);

        boat.scale(1.5f, 1.5f, 1.5f);
        boat.setLocalTranslation(0.0f, 1.5f, 0.0f);
        ship.attachChild(boat);


        // Disable the default flyby cam
//        flyCam.setEnabled(false);
//        //create the camera Node
//        camNode = new CameraNode("Camera Node", cam);
//        //This mode means that camera copies the movements of the target:
//        camNode.setControlDir(ControlDirection.SpatialToCamera);
//        //Attach the camNode to the target:
//        ship.attachChild(camNode);
//        //Move camNode, e.g. behind and above the target:
//        camNode.setLocalTranslation(new Vector3f(0, 5, -10));
        //Rotate the camNode to look at the target:
//      camNode.lookAt(ship.getLocalTranslation(), Vector3f.UNIT_Y);



    }

    /*
     * Automatic updater
     */
    @Override
    public void simpleUpdate(float tpf) {

    }

    /*
     * Custom Keybinding:
     * Map named actions to inputs.
     */
    private void initKeys() {
    /*
    * Labels the keybindings
    */
        inputManager.addMapping("Up",  new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("Down",  new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Left",   new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Right",  new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("ToggleCamera",  new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping("ToggleWater",  new KeyTrigger(KeyInput.KEY_V));
        

        /*
         * Group the keybindings together in a string array
         */
        inputManager.addListener(analogListener, new String[]{"Up",
"Down", "Left", "Right",});
        inputManager.addListener(actionListener, new String[]{"ToggleCamera", "ToggleWater"});

    }

    /*
     * Controls the node "ship", according to user input.
     *
     * TODO
     * When rotating, the ship only moves along 1 axes.
     */
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
          if (name.equals("ToggleCamera") && !keyPressed) {
        	if (fixedCamera){
        		//toggle to free camera
        		camNode.setEnabled(false);
        		inputManager.setCursorVisible(false);
        		System.out.println("freecam");
        		flyCam.setEnabled(true);
        	    flyCam.setMoveSpeed(50);
        	    cam.setFrustumFar(4000);

        	    Vector3f shipTranslation        = ship.getLocalTranslation();
        	    Vector3f constantTranslation    = new Vector3f(0f, 20f, 0f);
        	    Vector3f orientationTranslation = ship.getLocalRotation().getRotationColumn(2).mult(-50);
        	    Vector3f totalTranslation       = shipTranslation.add(constantTranslation).add(orientationTranslation);

        	    cam.setLocation(totalTranslation);
        	    cam.lookAt(ship.getLocalTranslation(), Vector3f.UNIT_Y);
        	}
        	else{
        		//toggle to fixed camera
        		System.out.println("fixedcam");
                flyCam.setEnabled(false);
                //create the camera Node
                camNode = new CameraNode("Camera Node", cam);
                //This mode means that camera copies the movements of the target:
                camNode.setControlDir(ControlDirection.SpatialToCamera);
                //Attach the camNode to the target:
                ship.attachChild(camNode);
                //Move camNode, e.g. behind and above the target:
                camNode.setLocalTranslation(new Vector3f(0, 5, -10));    
        	}
        	fixedCamera = !fixedCamera;
        }
        if (name.equals("ToggleWater") && !keyPressed){
        	if (useWater){
                water.setEnabled(false);
                rootNode.attachChild(FloorBlock);
        	}
            else {
                water.setEnabled(true);            	
                rootNode.detachChild(FloorBlock);
            }
        	useWater = !useWater;
          }
        }
      };

    
    private AnalogListener analogListener = new AnalogListener() {
      public void onAnalog(String name, float value, float tpf) {
        if (name.equals("Up")) {
        Vector3f v = ship.getLocalTranslation();
        Vector3f o = ship.getLocalRotation().getRotationColumn(2);
        ship.setLocalTranslation(v.add(o));
        }
        if (name.equals("Down")) {
        Vector3f v = ship.getLocalTranslation();
        Vector3f o = ship.getLocalRotation().getRotationColumn(2);
        ship.setLocalTranslation(v.subtract(o));
        }
        if (name.equals("Right")) {
        ship.rotate(0, -value*speed, 0);
        }
        if (name.equals("Left")) {
        	ship.rotate(0, value*speed, 0);
        }
      }
    };
    
    
    
    
//    
//    
//    
//    };
}