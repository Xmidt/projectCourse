package dk.dma.esim.simulation;

import com.jme3.app.SimpleApplication;
import com.jme3.input.controls.ActionListener;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;


public class Simulation extends SimpleApplication implements ActionListener, ScreenController {

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
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onAction(String string, boolean bln, float f) {
        throw new UnsupportedOperationException("Not supported yet.");
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
