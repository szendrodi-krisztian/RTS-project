package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author szend
 */
public class MainMenuState extends AbstractAppState implements ScreenController {

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

    private NiftyJmeDisplay niftyDisplay;

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            app.getGuiNode().attachChild(menuNode);
            niftyDisplay.getNifty().gotoScreen("start");
            app.getFlyByCamera().setEnabled(false);
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
            niftyDisplay.getNifty().gotoScreen("ingame");
            app.getInputManager().clearMappings();
            app.getGuiNode().detachChild(menuNode);
        }
    }

    @Override
    public void initialize(final AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        menuNode = new Node("Menu Node");
        niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/menuGui.xml", "start", this);
        app.getGuiViewPort().addProcessor(niftyDisplay);
        setEnabled(true);
    }

    public void toTheBattleField() {

        setEnabled(false);
        app.getStateManager().getState(BattleState.class).setEnabled(true);
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        nifty.gotoScreen("start");
    }

    @Override
    public void onStartScreen() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
