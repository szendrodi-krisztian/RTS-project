package mygame;

import battle.BattleMap;
import battle.entity.Unit;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
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

        map = new BattleMap(10, 20, rootNode, assetManager);
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
                        cam.setLocation(cam.getLocation().add(Vector3f.UNIT_X.mult(value * mult)));
                        break;
                    case "right":
                        cam.setLocation(cam.getLocation().subtract(Vector3f.UNIT_X.mult(value * mult)));
                        break;
                    case "up":
                        cam.setLocation(cam.getLocation().add(Vector3f.UNIT_Z.mult(value * mult)));
                        break;
                    case "down":
                        cam.setLocation(cam.getLocation().subtract(Vector3f.UNIT_Z.mult(value * mult)));
                        break;
                }
            }
        };

        ActionListener actl = new ActionListener() {

            Unit select = null;

            @Override
            public void onAction(String name, boolean isPressed, float tpf) {
                if (!isPressed) {
                    return;
                }
                switch (name.toLowerCase()) {
                    case "left click":
                        CollisionResults results = new CollisionResults();
                        Vector2f cur = inputManager.getCursorPosition();
                        Vector3f cur3d = cam.getWorldCoordinates(cur.clone(), 0f).clone();
                        Vector3f dir = cam.getWorldCoordinates(cur.clone(), 1f).subtract(cur3d).normalizeLocal();
                        Ray ray = new Ray(cur3d, dir);
                        rootNode.collideWith(ray, results);
                        CollisionResult coll = results.getClosestCollision();
                        if (coll == null && select == null) {
                            return;
                        }

                        Vector3f hit_geom = coll.getGeometry().getWorldTranslation();
                        Unit unit = map.units[map.n * (int) hit_geom.x + (int) hit_geom.z];
                        System.out.println(unit);
                        if (unit == null) {
                            if (select == null) {
                                return;
                            }
                            // I dont even know why we need that (-1)                                     here!
                            select.moveTo((int) coll.getContactPoint().x, (int) coll.getContactPoint().z - 1);
                        } else {
                            select = unit;
                        }
                        break;
                }
            }
        };

        inputManager.addListener(al, new String[]{"left", "right", "up", "down"});
        inputManager.addListener(actl, new String[]{"left click", "right click"});
        inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("left click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("right click", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
    }

    @Override
    public void simpleUpdate(float tpf) {
        map.tick(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {

    }
}
