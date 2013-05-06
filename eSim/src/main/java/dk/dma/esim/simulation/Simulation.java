package dk.dma.esim.simulation;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Plane;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import dk.dma.esim.virtualship.VirtualBasicShip;

public class Simulation extends SimpleApplication implements ActionListener, ScreenController {

    private BulletAppState bulletAppState;
    private float latitude;
    private float longitude;
    private float diff;
    private WaterFilter water;
    private Vector3f lightDir = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);
    private FilterPostProcessor fpp;
    private Geometry floorGeometry;
    private Node vehicleNode;
    private boolean fixedCamera = false;
    private CameraNode camNode;
    private VirtualBasicShip actor = null;

    public static void main(String[] args) {
        Simulation app = new Simulation();
        AppSettings aps = new AppSettings(true);
        aps.setFrameRate(60);
        aps.setResolution(1024, 768);
        app.setSettings(aps);
        app.showSettings = false;

        app.start();
    }

    @Override
    public void simpleInitApp() {

        /*
         * Locally variables used for testing
         * 
         * Latitude and longitude variables are used to define origin.
         * 	The length of 1 minute of latitude is 1.853 km
         */
        latitude = 55.4149920f;
        longitude = 12.3649320f;
        diff = 0.2f;

        /*
         * Creates node for water and skybox
         */
        Node mainScene = new Node("Main Scene");
        rootNode.attachChild(mainScene);

        /*
         * Skybox settings
         */
        Spatial sky = SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", false);
        sky.setLocalScale(350);
        mainScene.attachChild(sky);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f).normalizeLocal());
        rootNode.addLight(sun);

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1000.3f));
        rootNode.addLight(al);

        camNode = new CameraNode("Camera Node", cam);

        /*
         * Turn off status window
         */
        setDisplayFps(false);
        setDisplayStatView(false);

        /*
         * Water
         */
        water = new WaterFilter(rootNode, lightDir);
        fpp = new FilterPostProcessor(assetManager);

        setupKeys();
        buildWorld();
        toggleWater();
        toggleCamera();
        //initiateBoats();

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

    /*
     * Method which builds the virtual world
     */
    private void buildWorld() {

        Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Red);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Blender/2.4x/textures/Grass_256.png", false)));
        material.setColor("Color", ColorRGBA.Blue);

        Box floorBox = new Box(140000, 0.25f, 1400000);
        Plane plane = new Plane();
        plane.setOriginNormal(new Vector3f(0, -0.25f, 0), Vector3f.UNIT_Y);

        floorGeometry = new Geometry("Floor", floorBox);
        floorGeometry.setMaterial(material);
        floorGeometry.setLocalTranslation(0, -0.25f, 0);
        floorGeometry.addControl(new RigidBodyControl(new PlaneCollisionShape(plane), 0));

        rootNode.attachChild(floorGeometry);
    }

    /*
     * Used to draw boats with information obtained from ais messages
     */
//    public void buildBoats(String mmsi, float latitude, float longitude, int length, int width) {
//
//        if (!aislibMMSI.contains(mmsi)) {
//
//            Material wood = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//            wood.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Models/Boat/boat.png", false)));
//
//            ship = assetManager.loadModel("Models/Boat/boat.mesh.xml");
//            ship.setMaterial(wood);
//
//	        ship.scale(width/2, 1.5f, length/2);
//            ship.scale(1.5f, 100.5f, 1.5f);
//            ship.setLocalTranslation(latitude, 1.5f, longitude);
//
//            ship.setName(mmsi);
//
//            rootNode.attachChild(ship);
//            aislibMMSI.add(mmsi);
//
//        }
//    }
//
//    private void initiateBoats() {
//        Material wood = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        wood.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Models/Boat/boat.png", false)));
//
//        ship = assetManager.loadModel("Models/Boat/boat.mesh.xml");
//        ship.setMaterial(wood);
//    }

    /*
     * Draws the boat in the virtual world:
     * 
     * 	Hotkey: Space
     */
    public void buildBoat() {
        try {
            actor = new VirtualBasicShip();

            actor.setSpatial(assetManager.loadModel("Models/Boat/boat.mesh.xml"));
            actor.setMaterial(new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"));
            actor.getMaterial().setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Models/Boat/boat.png", false)));
            actor.getSpatial().scale(1.5f, 1.5f, 1.5f);
            actor.getSpatial().setLocalTranslation(0.0f, 1.5f, 0.0f);
            actor.getNode().attachChild(actor.getSpatial());


            rootNode.attachChild(actor.getNode());
            actor.setValid(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Toogle water on and off for better framerate:
     * 	
     * 	Hotkey: v
     */
    private void toggleWater() {

        if (water.isEnabled()) {
            water.setEnabled(false);
            viewPort.removeProcessor(fpp);
            rootNode.attachChild(floorGeometry);
        } else {
            water.setEnabled(true);
            rootNode.detachChild(floorGeometry);
            fpp.addFilter(water);
            viewPort.addProcessor(fpp);
        }
    }

    /*
     * Fixes the camera to the boat, or turn it into a freecam
     */
    private void toggleCamera() {
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
    }

    @Override
    public void simpleUpdate(float tpf) {

        if (actor != null && actor.isValid()) {
            actor.update();
        }
        //rotateCompassNeedle(vehicleNode.getLocalRotation());
        //convertLatLon();

    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        if (value) {
            if (binding.equals("Lefts")) {

                actor.incrementRudder();

            } else if (binding.equals("Rights")) {

                actor.decrementRudder();

            } else if (binding.equals("Ups")) {

                actor.setForwardSpeed(actor.getForwardSpeed() + 1);

            } else if (binding.equals("Downs")) {

                actor.setForwardSpeed(actor.getForwardSpeed() - 1);

            } else if (binding.equals("Space")) {

                if (actor == null) {
                    System.out.println("Build Boat");
                    buildBoat();
                }

            } else if (binding.equals("ToggleWater")) {
                toggleWater();
                System.out.println("Water");

            } else if (binding.equals("ToggleCamera")) {

                toggleCamera();
            } else if (binding.equals("InfoDump")) {
                //printShipInfo(vehicleNode);
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
