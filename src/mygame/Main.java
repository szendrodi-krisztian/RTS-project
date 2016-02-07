package mygame;

import battle.BattleMap;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    BattleMap map;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        map = new BattleMap(150, 200, rootNode, assetManager);
        cam.setLocation(new Vector3f(0, 10, 0));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
        flyCam.setMoveSpeed(50);
        flyCam.setEnabled(false);

        AnalogListener al = new AnalogListener() {

            final int mult = 10;
            
            @Override
            public void onAnalog(String name, float value, float tpf) {
                switch (name.toLowerCase()) {
                    case "left":
                        cam.setLocation(cam.getLocation().add(Vector3f.UNIT_X.mult(value*mult)));
                        break;
                    case "right":
                        cam.setLocation(cam.getLocation().subtract(Vector3f.UNIT_X.mult(value*mult)));
                        break;
                    case "up":
                        cam.setLocation(cam.getLocation().add(Vector3f.UNIT_Z.mult(value*mult)));
                        break;
                    case "down":
                        cam.setLocation(cam.getLocation().subtract(Vector3f.UNIT_Z.mult(value*mult)));
                        break;
                }
            }
        };

        inputManager.addListener(al, new String[]{"left", "right", "up", "down"});
        inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_S));

    }

    @Override
    public void simpleUpdate(float tpf) {
        map.tick(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {

    }
}
