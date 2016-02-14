package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Node;

/**
 *
 * @author szend
 */
public class MainMenuState extends AbstractAppState {

    private SimpleApplication app;
    
    private Node menuNode;

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            app.getGuiNode().attachChild(menuNode);
            ActionListener al = new ActionListener() {

                @Override
                public void onAction(String name, boolean isPressed, float tpf) {
                    switch (name) {
                        case "to the battle":
                            setEnabled(false);
                            app.getStateManager().getState(BattleState.class).setEnabled(true);
                            break;
                    }
                }
            };
            app.getInputManager().addMapping("to the battle", new KeyTrigger(KeyInput.KEY_N));
            app.getInputManager().addListener(al, new String[]{"to the battle"});
        } else {
            app.getInputManager().clearMappings();
            app.getGuiNode().detachChild(menuNode);
        }
    }

    @Override
    public void initialize(final AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        menuNode = new Node("Menu Node");
        System.out.println((int)(1%2));
        setEnabled(true);
    }

}
