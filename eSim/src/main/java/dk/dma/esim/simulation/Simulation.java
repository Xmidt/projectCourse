package dk.dma.esim.simulation;

import java.util.ArrayList;
import java.util.HashMap;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import dk.dma.esim.ais.AisShip;
import dk.dma.esim.ais.ReadMessage;
import dk.dma.esim.gui.Compass;
import dk.dma.esim.virtualship.VirtualShip;
import dk.dma.esim.virtualworld.Sky;
import dk.dma.esim.virtualworld.VirtualWorld;
import dk.dma.esim.virtualworld.Water;

public class Simulation extends SimpleApplication implements ActionListener, ScreenController {

    private float latitude;
    private float longitude;
    private float diff;
    private boolean fixedCamera = false;
    private VirtualWorld world;
    private CameraNode camNode;
    private VirtualShip actor = null;
    private Compass compass;
    
    private ReadMessage readAisMessage;
    private boolean readingAis = false;

    public static void main(String[] args) {
        Simulation app = new Simulation();
        AppSettings aps = new AppSettings(true);
        aps.setFrameRate(60);
        aps.setResolution(1024, 768);
        app.setSettings(aps);
        app.showSettings = false;
        app.start();
    }

    /**
     * This method is invoked app start in main.
     */
    @Override
    public void simpleInitApp() {

        try {
            /*
             * Locally variables used for testing
             * 
             * Latitude and longitude variables are used to define origin.
             * 	The length of 1 minute of latitude is 1.853 km (a nautical mile)
             */
            latitude = 55.4149920f;
            longitude = 12.3649320f;
            diff = 0.2f;

            camNode = new CameraNode("Camera Node", cam);

            /*
             * Turn off status window
             */
            setDisplayFps(false);
            setDisplayStatView(false);

            readAisMessage = new ReadMessage();
            readAisMessage.start();

            buildWorld();
            setupKeys();
            toggleCamera();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    
    /*
     * Initializes the virtual World:
     * 
     */
    public void buildWorld() {
        try {
            
            world = new VirtualWorld(rootNode,assetManager,viewPort); 
            
            compass = new Compass(assetManager, guiNode, settings.getHeight(), settings.getWidth());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
     * Draws the boat in the virtual world:
     * This could probably be handles a little nicer. Possibly make some methods in
     * virtualShip to ease future creation of objects in the world.
     * 
     * 	Hotkey: Space
     */
    public void buildBoat() {
        try {
            actor = new VirtualShip();

            actor.setSpatial(assetManager.loadModel("Shipmodels/josy/josy.j3o"));
//            actor.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));
//            actor.getMaterial().setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Models/Boat/boat.png", false)));
            actor.getSpatial().scale(1.5f, 1.5f, 1.5f);
            actor.getSpatial().setLocalTranslation(0.0f, -2.0f, 0.0f);
            actor.getSpatial().rotate(0.0f, -1.5f, 0.0f);
            actor.getNode().attachChild(actor.getSpatial());

            rootNode.attachChild(actor.getNode());
            actor.setValid(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Fixes the camera to the boat, or turn it into a freecam.
     * The camera is easiest to have here, for now, as the "cam" is part of the
     * simpleApplication
     */
    private void toggleCamera() {
        try {
            if (fixedCamera) {
                //toggle to free camera
                camNode.setEnabled(false);
                inputManager.setCursorVisible(false);
                flyCam.setEnabled(true);
                flyCam.setMoveSpeed(100);
                cam.setFrustumFar(4000);

                Vector3f shipTranslation = actor.getNode().getLocalTranslation();
                Vector3f constantTranslation = new Vector3f(0f, 20f, 0f);
                Vector3f orientationTranslation = actor.getNode().getLocalRotation().getRotationColumn(2).mult(-50);
                Vector3f totalTranslation = shipTranslation.add(constantTranslation).add(orientationTranslation);

                cam.setLocation(totalTranslation);
                cam.lookAt(actor.getNode().getLocalTranslation(), Vector3f.UNIT_Y);
            } else {
                //toggle to fixed camera
                flyCam.setEnabled(false);
                //create the camera Node
                camNode = new CameraNode("Camera Node", cam);
                //This mode means that camera copies the movements of the target:
                camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
                //Attach the camNode to the target:
                if (actor != null) {
                    actor.getNode().attachChild(camNode);
                }
                //Move camNode, e.g. behind and above the target:
                camNode.setLocalTranslation(new Vector3f(0, 5, -15));
            }
            fixedCamera = !fixedCamera;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
    	if (!readingAis) {
    		drawAisShip();
    	}
    	
        try {

            if (actor != null && actor.isValid()) {
                actor.update();
                compass.rotate(actor.getNode().getLocalRotation());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void drawAisShip() {
    	readingAis = true;
    	
    	ArrayList<Integer> shipMMSI = readAisMessage.getShipMmsi();
		HashMap<Integer,AisShip> ship = readAisMessage.getShipHashMap();
		
		for (Integer mmsi : shipMMSI) {
			// TODO: add ais ships to virtual world
			System.out.print(mmsi + "\n");
		}
		
    	
    	readingAis = false;
    }

    /**
     * Hotkey: I
     */
    private void printShipInfo() {
        String dump = "";

        dump += "Local translation (position): " + actor.getNode().getLocalTranslation() + "\n";
        dump += "Local rotation: " + actor.getNode().getLocalRotation() + "\n";
        dump += "Speed (forwardSpeed): " + actor.getForwardSpeed() + "\n";
        dump += "RudderAngle: " + actor.getRudderAngle() + "\n";

        System.out.println(dump);
    }

    /*
     * Key listener
     */
    private void setupKeys() {
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("ToggleCamera", new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping("ToggleWater", new KeyTrigger(KeyInput.KEY_V));
        inputManager.addMapping("InfoDump", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("InfoDumpAisMessage", new KeyTrigger(KeyInput.KEY_O));
        inputManager.addListener(this, "Lefts");
        inputManager.addListener(this, "Rights");
        inputManager.addListener(this, "Ups");
        inputManager.addListener(this, "Downs");
        inputManager.addListener(this, "Space");
        inputManager.addListener(this, "ToggleCamera");
        inputManager.addListener(this, "ToggleWater");
        inputManager.addListener(this, "InfoDump");
        inputManager.addListener(this, "InfoDumpAisMessage");
    }

    public void onAction(String binding, boolean value, float tpf) {
        if (value) {
            if (binding.equals("Lefts")) {

                actor.incrementRudder();

            } else if (binding.equals("Rights")) {

                actor.decrementRudder();

            } else if (binding.equals("Ups")) {

                actor.setForwardSpeed(actor.getForwardSpeed() + 1); //make proper method

            } else if (binding.equals("Downs")) {

                actor.setForwardSpeed(actor.getForwardSpeed() - 1);

            } else if (binding.equals("Space")) {

                if (actor == null) {
                    System.out.println("Build Boat");
                    buildBoat();
                }

            } else if (binding.equals("ToggleWater")) {
                System.out.println("Water " + world.getWater().toggleWater());
            } else if (binding.equals("ToggleCamera")) {
                toggleCamera();
            } else if (binding.equals("InfoDump")) {
                printShipInfo();
            } else if (binding.equals("InfoDumpAisMessage")) {
                //aisMessageHandler();
            }
        }
    }

    public void bind(Nifty nifty, Screen screen) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onStartScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEndScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
