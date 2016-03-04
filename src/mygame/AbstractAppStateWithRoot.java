package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import java.util.List;
import util.Util;

/**
 *
 * @author szend
 */
public abstract class AbstractAppStateWithRoot extends AbstractAppState implements ScreenController {

    protected AppStateManager stateManager;
    protected Camera camera;
    protected FlyByCamera flyCam;
    protected InputManager inputManager;
    protected Node realRoot, myRoot;
    protected NiftyJmeDisplay niftyDisplay;

    protected List<AbstractAppState> childStateList = new ArrayList<>();

    @Override
    public void cleanup() {
        super.cleanup();
        realRoot.detachChild(myRoot);
        for (AbstractAppState state : childStateList) {
            stateManager.detach(state);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (AbstractAppState state : childStateList) {
            state.setEnabled(enabled);
        }
        if (enabled) {
            realRoot.attachChild(myRoot);
            niftyDisplay.getNifty().gotoScreen("start");

        } else {
            realRoot.detachChild(myRoot);
            niftyDisplay.getNifty().gotoScreen("nope");

        }
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        realRoot = ((SimpleApplication) app).getRootNode();
        myRoot = new Node("myRoot");
        camera = ((SimpleApplication) app).getCamera();
        flyCam = ((SimpleApplication) app).getFlyByCamera();
        inputManager = ((SimpleApplication) app).getInputManager();
        this.stateManager = ((SimpleApplication) app).getStateManager();
        niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(Util.assets(), inputManager, ((SimpleApplication) app).getAudioRenderer(), ((SimpleApplication) app).getGuiViewPort());
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml(getNiftyXMLName(), "start", this);
        app.getGuiViewPort().addProcessor(niftyDisplay);
    }

    protected abstract String getNiftyXMLName();

    protected final Node getRootNode() {
        return myRoot;
    }

}
