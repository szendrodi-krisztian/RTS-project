package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author szend
 */
public abstract class AbstractAppStateWithRoot extends AbstractAppState {

    protected AppStateManager stateManager;
    protected Camera camera;
    protected FlyByCamera flyCam;
    protected InputManager inputManager;
    protected AssetManager assets;
    protected Node realRoot, myRoot;
    
    protected List<AbstractAppState> childStateList = new ArrayList<>();
    
    @Override
    public void cleanup() {
        super.cleanup();
        realRoot.detachChild(myRoot);
        for(AbstractAppState state : childStateList){
            stateManager.detach(state);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for(AbstractAppState state : childStateList){
            state.setEnabled(enabled);
        }
        if (enabled) {
            realRoot.attachChild(myRoot);
        } else {
            realRoot.detachChild(myRoot);
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
        assets = ((SimpleApplication) app).getAssetManager();
        this.stateManager = ((SimpleApplication) app).getStateManager();
    }

    protected final Node getRootNode() {
        return myRoot;
    }

}
