/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package virtual.world;

import java.util.ArrayList;
import java.util.HashMap;

import ais.reader.ReadMessage;
import ais.reader.Ship;

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
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class Water extends SimpleApplication implements ActionListener, ScreenController {
	
    private BulletAppState bulletAppState;
    private CameraNode camNode;				// The freecam node
    private Node vehicleNode;				// The node which contain the controllable ship
    
    private Spatial ship;					// Used to draw the ships from ais lib

    private RigidBodyControl ctl;
    private int forwardSpeed = 0;
    private boolean fixedCamera = true;
    private Vector3f lightDir = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);
    private WaterFilter water;
    private Geometry floorGeometry;
    private FilterPostProcessor fpp;
    private float rudderAngle = 0.0f;
    
    private Nifty nifty;					// Nifty package which contain the background of the compass
    private Node compassNode;				// The gui of the compass needle
    
    private ReadMessage readMessage;		// Class which handle ais messages

    public static void main(String[] args) {
        Water app = new Water();
        AppSettings aps = new AppSettings(true);
        aps.setFrameRate(60);
        aps.setResolution(1024, 768);
        app.setSettings(aps);
        app.showSettings = false;
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0, -1, 0));

        /*
         * Creates node for compass needle, water and skybox
         */
        Node mainScene = new Node("Main Scene");
        compassNode = new Node("Compass needle");
        rootNode.attachChild(mainScene);
        guiNode.attachChild(compassNode);		// Attach the compassneedle node to the gui interface of JME

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

        vehicleNode = new Node("vehicleNode");

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

        /*
         * Creating the compass Gui
         */
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);

        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/Nifty/compass.xml", "start", this);

        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        /*
         * Creates a compass needle, in JME gui interface
         */
        Picture pic = new Picture("HUD Picture");
        pic.setImage(assetManager, "Textures/compassNeedle.png", true);
        pic.setWidth(settings.getWidth() / 10);
        pic.setHeight(settings.getHeight() / 10);
        pic.move(-settings.getWidth() / 20, -settings.getHeight() / 20, 0);
        pic.rotateUpTo(new Vector3f(0, 0, 0));

        compassNode.attachChild(pic);
        compassNode.move(settings.getWidth() - 105, settings.getHeight() - 100, 0);

        /*
         * Initiating the Ais read messenger as a thread
         */
        readMessage = new ReadMessage();
        readMessage.start();
        
        setupKeys();
        buildWorld();
        toggleWater();
        toggleCamera();
        initiateBoats();
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
    public void buildBoats(float latitude, float longitude, int length, int width) {
    	
        Material wood = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wood.setTexture("ColorMap",assetManager.loadTexture(new TextureKey("Models/Boat/boat.png", false)));
        
        ship = assetManager.loadModel("Models/Boat/boat.mesh.xml");
        ship.setMaterial(wood);    
    	
        ship.scale(width/2, 1.5f, length/2);
        ship.setLocalTranslation(latitude, 1.5f, longitude);
        rootNode.attachChild(ship);
    }
    
    private void initiateBoats(){
        Material wood = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wood.setTexture("ColorMap",assetManager.loadTexture(new TextureKey("Models/Boat/boat.png", false)));
        
        ship = assetManager.loadModel("Models/Boat/boat.mesh.xml");
        ship.setMaterial(wood);    
    }

    /*
     * Draws the boat in the virtual world:
     * 
     * 	Hotkey: Space
     */
    public void buildBoat() {

        Spatial boat = assetManager.loadModel("Models/Boat/boat.mesh.xml");

        Material wood = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wood.setTexture("ColorMap",assetManager.loadTexture(new TextureKey("Models/Boat/boat.png", false)));
        boat.setMaterial(wood);

        boat.scale(1.5f, 1.5f, 1.5f);
        boat.setLocalTranslation(0.0f, 1.5f, 0.0f);

        vehicleNode.attachChild(boat);
        rootNode.attachChild(vehicleNode);
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

            Vector3f shipTranslation = vehicleNode.getLocalTranslation();
            Vector3f constantTranslation = new Vector3f(0f, 20f, 0f);
            Vector3f orientationTranslation = vehicleNode.getLocalRotation().getRotationColumn(2).mult(-50);
            Vector3f totalTranslation = shipTranslation.add(constantTranslation).add(orientationTranslation);

            cam.setLocation(totalTranslation);
            cam.lookAt(vehicleNode.getLocalTranslation(), Vector3f.UNIT_Y);
        } else {
            //toggle to fixed camera
            flyCam.setEnabled(false);
            //create the camera Node
            camNode = new CameraNode("Camera Node", cam);
            //This mode means that camera copies the movements of the target:
            camNode.setControlDir(ControlDirection.SpatialToCamera);
            //Attach the camNode to the target:
            vehicleNode.attachChild(camNode);
            //Move camNode, e.g. behind and above the target:
            camNode.setLocalTranslation(new Vector3f(0, 5, -15));
        }
        fixedCamera = !fixedCamera;
    }

    @Override
    public void simpleUpdate(float tpf) {
    
         if(rootNode.hasChild(vehicleNode)) updateShipPosition(vehicleNode);
         rotateCompassNeedle(vehicleNode.getLocalRotation());
         
    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        if (value) {
            if (binding.equals("Lefts")) {

                rudderAngle++;

            } else if (binding.equals("Rights")) {

                rudderAngle--;

            } else if (binding.equals("Ups")) {

                forwardSpeed++;

            } else if (binding.equals("Downs")) {

                forwardSpeed--;

            } else if (binding.equals("Space")) {

                if (ctl == null) {
                    buildBoat();
                }

            } else if (binding.equals("ToggleWater")) {
                toggleWater();
                System.out.println("Water");

            } else if (binding.equals("ToggleCamera")) {

                toggleCamera();
            } else if (binding.equals("InfoDump")) {

                printShipInfo(vehicleNode);

            } else if (binding.equals("InfoDumpAisMessage")) {

            	aisMessageHandler();

            }
        }
    }

    private void updateShipPosition(Node ship) {

        Vector3f v = ship.getLocalTranslation();
        Vector3f o = ship.getLocalRotation().getRotationColumn(2);

        ship.rotate(0, rudderAngle * 0.001f * forwardSpeed, 0);
        ship.setLocalTranslation(v.add(o.mult(0.1f * forwardSpeed)));

    }

    private void printShipInfo(Node ship) {
        String dump = "";

        dump += "Local translation (position): " + ship.getLocalTranslation() + "\n";
        dump += "Local rotation: " + ship.getLocalRotation() + "\n";
        dump += "Speed (forwardSpeed): " + forwardSpeed + "\n";
        dump += "RudderAngle: " + rudderAngle + "\n";

        System.out.println(dump);
    }

    public void rotateCompassNeedle(Quaternion quaternion) {
    	Quaternion rotation = new Quaternion(0, 0, quaternion.getY(), quaternion.getW());
    	compassNode.setLocalRotation(rotation);
    }
    
    /*
     * Used to test the ReadMessage class and draw ships
     * 
     * 	Hotkey: o
     * 
     * TODO:
     * 	Name the nodes with the ship mmsi.
     * 	Check if the mmsi node is already represented, if so update the current note.
     * 
     * 	When done testing, the ais handler should be added to simpleUpdate(), to keep all the ships locations up to date.
     */
	private void aisMessageHandler(){
		/*
		 * Latitude and longitude variables are used to define origin.
		 * 
		 * The length of 1 minute of latitude is 1.853 km
		 */		
		float latitude = 55.4149920f;
		float longitude = 12.3649320f;
		float diff = 0.2f;
		
		double shipLat = 0;
		double shipLon = 0;
		
		int shipBow = 0;
		int shipPor = 0;
		int shipSta = 0;
		int shipSte = 0;
		
		ArrayList<Integer> shipMMSI = readMessage.getShipMmsi();
		HashMap<Integer,Ship> ship = readMessage.getShipHashMap();
		
		for (Integer mmsi : shipMMSI){
			
			shipLat = ship.get(mmsi).getShipLatitude();
			shipLon = ship.get(mmsi).getShipLongitude();
			
			shipBow = ship.get(mmsi).getShipBow();
			shipPor = ship.get(mmsi).getShipPort();
			shipSta = ship.get(mmsi).getShipStarboard();
			shipSte = ship.get(mmsi).getShipStern();
			
			if ((shipBow != 0 || shipPor !=0 || shipSta != 0 || shipSte != 0 ) &&
			   ( (latitude - diff < shipLat && latitude + diff > shipLat) &&
	      	   ( (longitude -diff < shipLon && longitude +diff > shipLon)))) {
				
				float digitalLatitude = (float) ((((shipLat-latitude)*100) / 1.853)*500);
				float digitalLongitude =(float) ((((shipLon-longitude)*100) / 1.853)*500);
				
				buildBoats(digitalLatitude,digitalLongitude,shipSte,shipSta);	// the multiply is used to scale the world
				System.out.print(mmsi + "\n" + shipBow + "   " + shipPor + "   " + shipSta + "   " + shipSte + "\n");
				System.out.print(ship.get(mmsi).getShipLatitude() + "   " + digitalLatitude + "\n" + ship.get(mmsi).getShipLongitude() + "   " + digitalLongitude + "\n\n");
			}
		}
	}

    @Override
    public void bind(Nifty arg0, Screen arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onEndScreen() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStartScreen() {
        // TODO Auto-generated method stub
    }
}