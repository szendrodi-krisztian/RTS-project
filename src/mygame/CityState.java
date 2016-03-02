package mygame;

import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;

/**
 *
 * @author Krisz
 */
public class CityState extends AbstractAppStateWithRoot {

    public CityState(AppStateManager sm) {
        childStateList.add(new CameraMovementState());
        for (AbstractAppState state : childStateList) {
            sm.attach(state);
        }
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
