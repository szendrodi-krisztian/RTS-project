package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author szend
 */
public class CameraMovementState extends AbstractAppState implements AnalogListener {

    private Camera camera;
    private InputManager inputManager;
    private Vector3f camPos;


    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;
    private boolean zoom_in = false;
    private boolean zoom_out = false;

    private static final float mult = 0.2f;
    private static final Vector3f UNIT_X = Vector3f.UNIT_X.mult(mult);
    private static final Vector3f UNIT_Y = Vector3f.UNIT_Y.mult(mult);
    private static final Vector3f UNIT_Z = Vector3f.UNIT_Z.mult(mult);

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        if (left) {
            camPos.addLocal(UNIT_X);
        }

        if (right) {
            camPos.subtractLocal(UNIT_X);
        }

        if (up) {
            camPos.addLocal(UNIT_Z);
        }

        if (down) {
            camPos.subtractLocal(UNIT_Z);
        }

        if (zoom_in) {
            camPos.subtractLocal(UNIT_Y);
        }

        if (zoom_out) {
            camPos.addLocal(UNIT_Y);
        }
        left = down = right = up = zoom_in = zoom_out = false;
        camera.setLocation(camPos);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            camera.setLocation(camPos);
            Vector3f look = camPos.clone();
            look.y = 0;
            camera.lookAt(look, Vector3f.UNIT_Y);
            inputManager.addListener(this, "left", "right", "up", "down", "zoom in", "zoom out");
            inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
            inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));
            inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_W));
            inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_S));
            inputManager.addMapping("zoom in", new KeyTrigger(KeyInput.KEY_Q));
            inputManager.addMapping("zoom out", new KeyTrigger(KeyInput.KEY_E));
        } else {
            inputManager.clearMappings();
        }

    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        camera = ((SimpleApplication) app).getCamera();
        inputManager = ((SimpleApplication) app).getInputManager();
        camPos = new Vector3f(0, 20, 0);
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        switch (name) {
            case "left":
                left = true;
                break;
            case "right":
                right = true;
                break;
            case "up":
                up = true;
                break;
            case "down":
                down = true;
                break;
            case "zoom in":
                zoom_in = true;
                break;
            case "zoom out":
                zoom_out = true;
                break;

        }
    }

}
