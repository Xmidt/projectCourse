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

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.PlaneCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
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


public class Water extends SimpleApplication implements ActionListener {


    private BulletAppState bulletAppState;
    private VehicleControl vehicle;
    private final float accelerationForce = 1000.0f;
    private final float brakeForce = 100.0f;
    private float steeringValue = 0;
    private float accelerationValue = 0;
    private Vector3f jumpForce = new Vector3f(0, 3000, 0);
    private CameraNode camNode;
    private Node vehicleNode;
    private Node westNode;
    private Node eastNode;
    private Box box;
    private static final float shipLength = 2.48f;
    private static final float shipWidth  = 2.24f;
    private static final float shipHeight = 2.12f;
    //private ShipPhysicsControl ctl;
    private RigidBodyControl ctl;
    private int forwardSpeed = 0;
    private float acceleration = 1;
    private boolean fixedCamera = true;
    private Vector3f lightDir = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);
    private WaterFilter water;
    private Geometry floorGeometry;
    private FilterPostProcessor fpp;
    private float rudderAngle = 0.0f;

    public static void main(String[] args) {
        //TestPhysicsCar app = new TestPhysicsCar();
        Water app = new Water();
        AppSettings aps = new AppSettings(true);
        aps.setFrameRate(60);
        aps.setResolution(1024, 768);
        app.setSettings(aps);
        app.showSettings = false;
        aps.setAudioRenderer(null);
        app.start();
    }


    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0, -1, 0));
        
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
        
        vehicleNode = new Node("vehicleNode");
        
        camNode = new CameraNode("Camera Node", cam);
        /*
         * Camera Settings
         /
        //toggle to fixed camera
        flyCam.setEnabled(false);
        flyCam.setMoveSpeed(50);
        
        //create the camera Node
        //This mode means that camera copies the movements of the target:
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        //Attach the camNode to the target:
        vehicleNode.attachChild(camNode);
        //Move camNode, e.g. behind and above the target:
        camNode.setLocalTranslation(new Vector3f(0, 5, -10));
        */
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
    }


    private PhysicsSpace getPhysicsSpace(){
        return bulletAppState.getPhysicsSpace();
    }


    private void setupKeys() {
        inputManager.addMapping("Lefts", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("Rights", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Ups", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("Downs", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Reset", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("ToggleCamera",  new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping("ToggleWater",  new KeyTrigger(KeyInput.KEY_V));
        inputManager.addMapping("ResetRudder",  new KeyTrigger(KeyInput.KEY_Y));
        inputManager.addListener(this, "Lefts");
        inputManager.addListener(this, "Rights");
        inputManager.addListener(this, "Ups");
        inputManager.addListener(this, "Downs");
        inputManager.addListener(this, "Space");
        inputManager.addListener(this, "Reset");
        inputManager.addListener(this, "ToggleCamera");
        inputManager.addListener(this, "ToggleWater");
        inputManager.addListener(this, "ResetRudder");
    }


    private void buildWorld() {
        Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Red);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Blender/2.4x/textures/Grass_256.png",false)));
        material.setColor("Color", ColorRGBA.Blue);
          
        Box floorBox = new Box(140000, 0.25f, 1400000);
        Plane plane = new Plane();
        plane.setOriginNormal(new Vector3f(0, -0.25f, 0), Vector3f.UNIT_Y);
        
        floorGeometry = new Geometry("Floor", floorBox);
        floorGeometry.setMaterial(material);
        floorGeometry.setLocalTranslation(0, -0.25f, 0);
        floorGeometry.addControl(new RigidBodyControl(new PlaneCollisionShape(plane), 0));
        
        rootNode.attachChild(floorGeometry);
        bulletAppState.getPhysicsSpace().add(floorGeometry);
        
    }

    public void buildBuoys(){
       
        Spatial Buoy1 = assetManager.loadModel("Models/MonkeyHead/MonkeyHead.mesh.xml");
        
        Material wood = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wood.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Models/MonkeyHead/MonkeyHead_diffuse.jpg",false)));
        Buoy1.setMaterial(wood);
        //Buoy1.addControl(new RigidBodyControl(new BoxCollisionShape(),0));
        Buoy1.scale(3.5f, 3.5f, 3.5f);
        //rootNode.attachChild(Buoy1);
        Buoy1.setLocalTranslation(100.0f, 4.5f, 100.0f);
        rootNode.attachChild(Buoy1);
        Buoy1.addControl(new RigidBodyControl(new BoxCollisionShape(),0));
        bulletAppState.getPhysicsSpace().add(Buoy1);
        
        Spatial Buoy2 = assetManager.loadModel("Models/MonkeyHead/MonkeyHead.mesh.xml");
        Buoy2.setMaterial(wood);
        Buoy2.scale(3.5f, 3.5f, 3.5f);
        Buoy2.setLocalTranslation(-100.0f, 4.5f, 100.0f);
        rootNode.attachChild(Buoy2);
        Buoy2.addControl(new RigidBodyControl(new BoxCollisionShape(),0));
        bulletAppState.getPhysicsSpace().add(Buoy2);
        
        Spatial Buoy3 = assetManager.loadModel("Models/MonkeyHead/MonkeyHead.mesh.xml");
        Buoy3.setMaterial(wood);
        Buoy3.scale(3.5f, 3.5f, 3.5f);
        Buoy3.setLocalTranslation(100.0f, 4.5f, -100.0f);
        rootNode.attachChild(Buoy3);
        Buoy3.addControl(new RigidBodyControl(new BoxCollisionShape(),0));
        bulletAppState.getPhysicsSpace().add(Buoy3);
        
        Material tex4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        tex4.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Models/Ferrari/Car.jpg",false)));
        
        Spatial Buoy4 = assetManager.loadModel("Models/Ferrari/Car.mesh.xml");
        Buoy4.setMaterial(tex4);
        Buoy4.scale(3.5f, 3.5f, 3.5f);
        Buoy4.setLocalTranslation(-100.0f, 4.5f, -100.0f);
        rootNode.attachChild(Buoy4);
        Buoy4.addControl(new RigidBodyControl(new BoxCollisionShape(),0));
        bulletAppState.getPhysicsSpace().add(Buoy4);
        
        Spatial Buoy5 = assetManager.loadModel("Models/MonkeyHead/MonkeyHead.mesh.xml");
        Buoy5.setMaterial(wood);
        Buoy5.scale(3.5f, 3.5f, 3.5f);
        Buoy5.setLocalTranslation(0f, 4.5f, 100.0f);
        Buoy5.rotate(0, 3, 0);
        rootNode.attachChild(Buoy5);
        //bulletAppState.getPhysicsSpace().add(Buoy5);
        
        Material tex6 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        tex6.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Models/Elephant/Elephant_normal.jpg",false)));
        
        Spatial Buoy6 = assetManager.loadModel("Models/Elephant/Elephant.mesh.xml");
        Buoy6.setMaterial(tex6);
        Buoy6.scale(3.5f, 3.5f, 3.5f);
        Buoy6.setLocalTranslation(-100.0f, 4.5f, 0.0f);
        Buoy6.rotate(0, 3, 0);
        rootNode.attachChild(Buoy6);
        //bulletAppState.getPhysicsSpace().add(Buoy6);
        
         
//        Material tex7 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        tex7.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Models/SpaceCraft/Rocket.png",false)));
//        
//        Spatial Buoy7 = assetManager.loadModel("Models/SpaceCraft/Rocket.mesh.xml");
//        Buoy7.setMaterial(tex7);
//        Buoy7.scale(3.5f, 3.5f, 3.5f);
//        Buoy7.setLocalTranslation(100.0f, 4.5f, 0.0f);
//        Buoy7.rotate(0, 3, 0);
//        
//        rootNode.attachChild(Buoy7);
        
    }
    
    public void buildBoat(){
         
        Spatial boat = assetManager.loadModel("Models/Boat/boat.mesh.xml");
        //westNode = assetManager.loadModel("Models/SpaceCraft/Rocket.mesh.xml");;
        //eastNode = assetManager.loadModel("Models/SpaceCraft/Rocket.mesh.xml");;
        westNode = new Node();
        eastNode = new Node();
//        Material tex7 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        tex7.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Models/SpaceCraft/Rocket.png",false)));
//        westNode.setMaterial(tex7);
//        eastNode.setMaterial(tex7);
//        eastNode.scale(5.0f,5.0f,5.0f);
//        westNode.scale(5.0f,5.0f,5.0f);       
        
        
        Material wood = new Material(
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            wood.setTexture("ColorMap",
                assetManager.loadTexture(new TextureKey("Models/Boat/boat.png",false)));
            boat.setMaterial(wood);

        boat.scale(1.5f, 1.5f, 1.5f);
        boat.setLocalTranslation(0.0f, 1.5f, 0.0f);
        
        vehicleNode.attachChild(boat);
        //westNode.setControlDir(ControlDirection.SpatialToCamera);
        //eastNode.setControlDir(ControlDirection.SpatialToCamera);
        vehicleNode.attachChild(westNode);
        vehicleNode.attachChild(eastNode);
        westNode.setLocalTranslation(new Vector3f(100, 0, 0));
        eastNode.setLocalTranslation(new Vector3f(-100, 0, 0));
       
        rootNode.attachChild(vehicleNode);

        ctl = new RigidBodyControl(25f);
        
        vehicleNode.addControl(ctl);
        bulletAppState.getPhysicsSpace().add(ctl);
        
        
        //Maintain speed
        ctl.setFriction(0f);
        ctl.setMass(1000);
    }
    
    private void toggleWater(){
        
        if(water.isEnabled()){
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
    
    private void toggleCamera(){
        if (fixedCamera) {
            //toggle to free camera
            camNode.setEnabled(false);
            inputManager.setCursorVisible(false);
            System.out.println("freecam");
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
            System.out.println("fixedcam");
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
    
    
   
//    public void update(float tpf) {
//
//            Vector3f dir = new Vector3f();
//
//            // Direction we want to go to (lookAt-like)
//            dir = destination.subtract(playerShip.getWorldTranslation()).normalize();
//
//            Vector3f radians = getCurrentAngleInRadians(dir);
//            float speedToGo = getSpeedFromAngle(radians);
//            float distanceToTarget = getDistanceToDestination();
//
//            if (amount <= 1) {
//// Copy of our actual rotation Quaternion
//                Quaternion rotator = playerShip.getWorldRotation();
//// Up vector has to be proper up vector for ship
//                if (speedToGo >= .50f || distanceToTarget < 5f) {
//// level ship by rolling
//                    rotator.lookAt(dir, Vector3f.UNIT_Y);
//                } else {
//// sets Up to look at for this frame
//                    Vector3f playerUp = playerShip.getLocalRotation().mult(Vector3f.UNIT_Y);
//                    rotator.lookAt(dir, playerUp);
//                }
//// Rotate ship only by amount into direction
//                playerShip.getLocalRotation().slerp(rotator, amount * tpf);
//// Rotate by its own value?
//                playerShip.setLocalRotation(playerShip.getLocalRotation());
//            }
//
//            gMgrs.getGameState().getPlayer().getShip().incToXSpeed(speedToGo);
//
//// FIXME: find algorithm to decelerate ship as distance shortens
//// according to speed.
//            if (distanceToTarget >= 0 && distanceToTarget <= 0.3) {
//                System.out.println("We’re there!");
//// stop ship
//                gMgrs.getGameState().getPlayer().getShip().decToStopSpeed();
//                readyToGo = false;
//                areWeThereYet = true;
//            }
//        
//    }
    
    int i = 20;
   
    @Override
    public void simpleUpdate(float tpf) {
       // cam.lookAt(vehicle.getPhysicsLocation(), Vector3f.UNIT_Y);
               //System.out.println("SimpleUpdate");
        //ctl.applyCentralForce(jumpForce);
        //ctl.setPhysicsLocation(new Vector3f(0.0f,ctl.getPhysicsLocation().y+=0.02f,0.0f));
        //System.out.println(ctl.getPhysicsLocation().toString());
        
        //update();
        
        
        if(ctl != null){
        
                
        if(i==0){    //only print info on every i'th update tick
        calculateNewHeading();
//                System.out.println("Speed:" + forwardSpeed);
//                System.out.println(ctl.getLinearVelocity().toString());
//                System.out.println("Direction:");
//                System.out.println(vehicleNode.getLocalRotation().getRotationColumn(2).toString());
//                System.out.println("Angular Vel:");
//                System.out.println(ctl.getAngularVelocity().toString());
                
                i = 20;
            } else{
                i--;
            }
        }
    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        
        //Vector3f playerUp = vehicleNode.getLocalRotation().mult(Vector3f.UNIT_Y);
        
        if(value){
        if (binding.equals("Lefts")) {

            rudderAngle+=0.01f;
            System.out.println("New rudder angel: " + rudderAngle);
            
        } else if (binding.equals("Rights")) {
            
            rudderAngle-=0.01f;
            System.out.println("New rudder angel: " + rudderAngle);
            
        } else if (binding.equals("Ups")) {    
            
            forwardSpeed++;
            ctl.setLinearVelocity(vehicleNode.getLocalRotation().getRotationColumn(2).mult(forwardSpeed).mult(new Vector3f(1,0,1)));
            
        } else if (binding.equals("Downs")) {
            
            forwardSpeed--;
            ctl.setLinearVelocity(vehicleNode.getLocalRotation().getRotationColumn(2).mult(forwardSpeed).mult(new Vector3f(1,0,1)));
            
        } else if (binding.equals("Space")) {
            
            if(ctl == null) buildBoat();

        } else if (binding.equals("Reset")) {
                buildBuoys();
              
        } else if (binding.equals("ToggleWater")){
                toggleWater();
                System.out.println("Water");
                
        } else if (binding.equals("ToggleCamera")){
                toggleCamera();
        } else if (binding.equals("ResetRudder")){
                rudderAngle=0.0f;
                
        }
        }
    }
    
    private void calculateNewHeading() {

        Vector3f playerUp = vehicleNode.getLocalRotation().mult(Vector3f.UNIT_Y);
        
        //System.out.println("Local rotation: " + vehicleNode.getLocalRotation());
        
        //westNode.setLocalTranslation(rootNode.getLocalTranslation().x+100, rootNode.getLocalTranslation().y, rootNode.getLocalTranslation().z);
        if(rudderAngle != 0){
        vehicleNode.removeControl(ctl);
           
            //Vector3f destination = new Vector3f(-100.0f, 0.0f, 100.0f);
        //Vector3f destination = vehicleNode.getWorldTranslation().mult(Vector3f.UNIT_XYZ);
            
            //Quaternion destination = new Quaternion(-100.0f, 4.5f, 100.0f, 1.0f);
            
            //low shipfactor for slow ships
            //if (amount != 1){
        Vector3f destination;
        destination = rudderAngle > 0.0 ? westNode.getWorldTranslation().mult(Vector3f.UNIT_XYZ) : eastNode.getWorldTranslation().mult(Vector3f.UNIT_XYZ);
        
        if(rudderAngle > 0.0){
            System.out.println("Turning Left");
        }else{
            System.out.println("Turning Right");
        }
        
        Vector3f dir = destination.subtract(vehicleNode.getWorldTranslation()).normalize();
            //System.out.println("Dir: " + dir);
        Quaternion rotator = vehicleNode.getWorldRotation(); //up vector has to be proper up vector for given object
                
        rotator.lookAt(dir, playerUp); //rotate ship only by amount into direction
                //System.out.println(rotator);
        vehicleNode.getLocalRotation().slerp(rotator, 0.03f*forwardSpeed*0.1f);
        vehicleNode.setLocalRotation(vehicleNode.getLocalRotation());
        //System.out.println("Local rotation: " + vehicleNode.getLocalRotation());
  
            //}
        
        vehicleNode.addControl(ctl);
        
        ctl.setLinearVelocity(vehicleNode.getLocalRotation().getRotationColumn(2).mult(forwardSpeed));
        ctl.setLinearVelocity(ctl.getLinearVelocity().multLocal(new Vector3f(1,0,1)));
        
        
        }
    }
}
