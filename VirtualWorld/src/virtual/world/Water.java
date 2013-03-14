package virtual.world;

import com.jme3.app.SimpleApplication;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
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

    public static void main(String[] args) {
        Water app = new Water();
        app.start();
    }

    @Override
    public void simpleInitApp() {
    	/*
    	 * Camera settings
    	 */
    	flyCam.setMoveSpeed(50);
    	cam.setFrustumFar(4000);
        cam.setLocation(new Vector3f(-327.21957f, 61.6459f, 126.884346f));
        cam.setRotation(new Quaternion(0.052168474f, 0.9443102f, -0.18395276f, 0.2678024f));
        cam.setRotation(new Quaternion().fromAngles(new float[]{FastMath.PI * 0.06f, FastMath.PI * 0.65f, 0}));

        /*
         * Turn off status window
         */
        setDisplayFps(false);
        setDisplayStatView(false);

        /*
         * MainScene, for rendering the scene
         */
        Node mainScene = new Node("Main Scene");
        rootNode.attachChild(mainScene);
        
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

    }
}