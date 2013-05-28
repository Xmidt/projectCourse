package dk.dma.esim.virtualworld;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

/**
 *
 * @author Team Echo
 */
public class Sky {

    private Spatial sky;
    private DirectionalLight sun;
    private AssetManager assetManger;
    private AmbientLight ambientLight;
    
    /**
     * Class for the virtual world with skybox, light source and ambientlight.
     * 
     * @param rootNode		The main Node in JME, which contains all objects/elements in the 3d rendered world
     * @param assetManger	Default library of JME models, textures and etc. etc.
     */
    public Sky(Node rootNode, AssetManager assetManger) {
        this.assetManger = assetManger;
        
        // Skybox image
        sky = SkyFactory.createSky(this.assetManger, "Scenes/Beach/FullskiesSunset0068.dds", false);
        sky.setLocalScale(350);
        rootNode.attachChild(sky);
        
        // Light source as the sun
        sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0.1f, -0.7f, 1.0f).normalizeLocal());
        rootNode.addLight(sun);
        
        // General background light => ambient light
        ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.White.mult(1000.3f));
        rootNode.addLight(ambientLight);
    }
}
