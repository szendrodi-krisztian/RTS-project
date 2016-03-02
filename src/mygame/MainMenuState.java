package mygame;

import battle.BattleMap;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author szend
 */
public class MainMenuState extends AbstractAppState implements ScreenController {

    private SimpleApplication app;

    boolean to_bf;
    boolean to_me;
    boolean to_ci;

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (to_bf) {
            setEnabled(false);
            app.getStateManager().getState(BattleState.class).setEnabled(true);
            to_bf = false;
            return;
        }
        if (to_me) {
            setEnabled(false);
            app.getStateManager().getState(MapEditorState.class).setEnabled(true);
            to_me = false;
        }
        if (to_ci) {
            setEnabled(false);
            app.getStateManager().getState(CityState.class).setEnabled(true);
            to_ci = false;
        }

    }

    private NiftyJmeDisplay niftyDisplay;

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            niftyDisplay.getNifty().gotoScreen("start");
            to_bf = false;
            to_me = false;
            to_ci = false;
        } else {
            app.getRootNode().detachAllChildren();
            niftyDisplay.getNifty().gotoScreen("nope");
        }
    }

    BattleMap map;

    @Override
    public void initialize(final AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/menuGui.xml", "start", this);
        app.getGuiViewPort().addProcessor(niftyDisplay);
        to_bf = false;
        to_me = false;
        setEnabled(true);
    }

    public void toTheBattleField() {
        to_bf = true;
    }

    public void toTheMapEditor() {
        to_me = true;
    }

    public void toTheCity() {
        to_ci = true;
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
