package virtual.world;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
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
import com.jme3.scene.plugins.blender.BlenderLoader;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;



class ModelTest extends SimpleApplication {

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
    settings.setResolution(800,600);
    settings.setFrameRate(30);
    settings.setSamples(0);

    ModelTest app = new ModelTest();
    app.setSettings(settings);
    app.setShowSettings(false);     // Disable the settings menu at start
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
    	
    	DirectionalLight sun = new DirectionalLight();
    	sun.setColor(ColorRGBA.White);
    	sun.setDirection(lightDir);
    	rootNode.addLight(sun);

    	DirectionalLight sun2 = new DirectionalLight();
    	sun2.setColor(ColorRGBA.White);
    	sun.setDirection(new Vector3f(4.9236743f, -1.27054665f, 5.896916f));
    	rootNode.addLight(sun);

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
    	            	        
    	/*
    	 * Define boat models
    	 */
    	
    	Spatial boat1 = assetManager.loadModel("Shipmodels/drakkar263/drakkar263.j3o");
    	Spatial boat2 = assetManager.loadModel("Shipmodels/josy/josy.j3o");
    	Spatial boat3 = assetManager.loadModel("Shipmodels/loane/loane.j3o");
    	Spatial boat4 = assetManager.loadModel("Shipmodels/Yacht/Yacht.j3o");
    	Spatial boat5 = assetManager.loadModel("Shipmodels/Hermes/Hermes.j3o");
    	Spatial boat6 = assetManager.loadModel("Shipmodels/mayflower/mayflower.j3o");
    	
    	        
    	boat1.setLocalTranslation(10.0f, 3f, 0.0f);
    	boat2.setLocalTranslation(30.0f, 3f, 0.0f);
    	boat3.setLocalTranslation(50.0f, 3f, 0.0f);
    	boat4.setLocalTranslation(70.0f, 3f, 0.0f);
    	boat5.setLocalTranslation(90.0f, 3f, 0.0f);
    	boat6.setLocalTranslation(110.0f, 3f, 0.0f);

    	rootNode.attachChild(boat1);
    	rootNode.attachChild(boat2);
    	rootNode.attachChild(boat3);
    	rootNode.attachChild(boat4);
    	rootNode.attachChild(boat5);
    	rootNode.attachChild(boat6);    	

    }	
}