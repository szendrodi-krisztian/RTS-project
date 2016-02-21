package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
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

    boolean to_bf;
    boolean to_me;

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (to_bf) {
            setEnabled(false);
            BattleState bs = new BattleState();
            app.getStateManager().attach(bs);
            niftyDisplay.getNifty().gotoScreen("ingame");
            return;
        }
        if (to_me) {
            setEnabled(false);
            MapEditorState ms = new MapEditorState();
            app.getStateManager().attach(ms);
            niftyDisplay.getNifty().gotoScreen("map_edit_screen");
            
        }

    }

    private NiftyJmeDisplay niftyDisplay;

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            app.getGuiNode().attachChild(menuNode);
            niftyDisplay.getNifty().gotoScreen("start");
            app.getFlyByCamera().setEnabled(false);
            to_bf = false;
            to_me = false;
        } else {
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
        nifty.addXml("Interface/map_editor_gui.xml");
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

    @Override
    public void bind(Nifty nifty, Screen screen) {
        // nifty.gotoScreen("start");
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
