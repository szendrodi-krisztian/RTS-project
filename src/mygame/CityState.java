package mygame;

import city.City;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;

/**
 *
 * @author Krisz
 */
public class CityState extends AbstractAppStateWithRoot {

    private City city;

    public CityState(AppStateManager sm) {

    }

    boolean to_menu;

    @Override
    public void update(float tpf) {
        if (isEnabled()) {
            if (to_menu) {
                setEnabled(false);
                stateManager.getState(MainMenuState.class).setEnabled(true);
                to_menu = false;
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            flyCam.setEnabled(false);
            camera.setLocation(new Vector3f(8, 15, 7));
            camera.lookAt(new Vector3f(8, 0, 7), Vector3f.UNIT_Y);
            ActionListener actl;
            actl = new ActionListener() {

                @Override
                public void onAction(String name, boolean isPressed, float tpf) {
                    switch (name.toLowerCase()) {
                        case "to the menu":
                            if (!isPressed) {
                                return;
                            }
                            to_menu = true;
                            break;
                    }
                }
            };

            inputManager.addListener(actl, new String[]{"to the menu"});
            inputManager.addMapping("to the menu", new KeyTrigger(KeyInput.KEY_M));
        } else {
            inputManager.clearMappings();
        }
    }

    @Override
    public void initialize(AppStateManager stateManager, Application appl) {
        super.initialize(stateManager, appl);
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.White);
        getRootNode().addLight(light);
        to_menu = false;
        city = new City(getRootNode(), assets);
        setEnabled(false);
    }

    @Override
    protected String getNiftyXMLName() {
        return "Interface/city_gui.xml";
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {

    }

    @Override
    public void onStartScreen() {

    }

    @Override
    public void onEndScreen() {

    }

}
