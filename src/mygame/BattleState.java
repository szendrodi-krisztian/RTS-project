/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import battle.BattleMap;
import battle.entity.Unit;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author szend
 */
public class BattleState extends AbstractAppState {

    private SimpleApplication app;

    private BattleMap map;

    private AmbientLight light;

    private Node battleRoot;

    private Vector3f camPos;

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    public void update(float tpf) {
        if (isEnabled()) {
            map.tick(tpf);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            app.getRootNode().attachChild(battleRoot);
            if (map == null) {
                map = new BattleMap(100, 100, battleRoot, app.getAssetManager());
            }
            app.getCamera().setLocation(camPos);
            Vector3f look = camPos.clone();
            look.y = 0;
            app.getCamera().lookAt(look, Vector3f.UNIT_Y);
            app.getFlyByCamera().setMoveSpeed(50);
            app.getFlyByCamera().setEnabled(false);

            AnalogListener al = new AnalogListener() {

                final int mult = 10;

                @Override
                public void onAnalog(String name, float value, float tpf) {
                    switch (name.toLowerCase()) {
                        case "left":
                            camPos.addLocal(Vector3f.UNIT_X.mult(value * mult));
                            break;
                        case "right":
                            camPos.subtractLocal(Vector3f.UNIT_X.mult(value * mult));
                            break;
                        case "up":
                            camPos.addLocal(Vector3f.UNIT_Z.mult(value * mult));
                            break;
                        case "down":
                            camPos.subtractLocal(Vector3f.UNIT_Z.mult(value * mult));
                            break;
                        case "zoom in":
                            camPos.subtractLocal(Vector3f.UNIT_Y.mult(value * mult));
                            break;
                        case "zoom out":
                            camPos.addLocal(Vector3f.UNIT_Y.mult(value * mult));
                            break;
                    }
                    app.getCamera().setLocation(camPos);
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
                            Vector2f cur = app.getInputManager().getCursorPosition();
                            Vector3f cur3d = app.getCamera().getWorldCoordinates(cur.clone(), 0f).clone();
                            Vector3f dir = app.getCamera().getWorldCoordinates(cur.clone(), 1f).subtract(cur3d).normalizeLocal();
                            Ray ray = new Ray(cur3d, dir);
                            battleRoot.collideWith(ray, results);
                            CollisionResult coll = results.getClosestCollision();
                            if (coll == null) {
                                return;
                            }
                            Vector3f hit_geom = coll.getGeometry().getWorldTranslation();
                            Unit unit = map.units[map.mapHeight * (int) hit_geom.z + (int) hit_geom.x];
                            System.out.println(unit);
                            //System.out.println((int) hit_geom.x + "  " + (int) hit_geom.z);
                            if (unit != null) {
                                select = unit;
                            }
                            break;
                        case "right click":
                            if (select != null) {
                                CollisionResults results2 = new CollisionResults();
                                Vector2f cur2 = app.getInputManager().getCursorPosition();
                                Vector3f cur3d2 = app.getCamera().getWorldCoordinates(cur2.clone(), 0f).clone();
                                Vector3f dir2 = app.getCamera().getWorldCoordinates(cur2.clone(), 1f).subtract(cur3d2).normalizeLocal();
                                Ray ray2 = new Ray(cur3d2, dir2);
                                battleRoot.collideWith(ray2, results2);
                                CollisionResult coll2 = results2.getClosestCollision();
                                if (coll2 == null) {
                                    return;
                                }

                                select.moveTo((int) coll2.getContactPoint().x, (int) coll2.getContactPoint().z);
                            }
                            break;
                        case "to the menu":
                            setEnabled(false);
                            app.getStateManager().getState(MainMenuState.class).setEnabled(true);
                            break;
                    }
                }
            };

            app.getInputManager().addListener(al, new String[]{"left", "right", "up", "down", "zoom in", "zoom out"});
            app.getInputManager().addListener(actl, new String[]{"left click", "right click", "to the menu"});
            app.getInputManager().addMapping("left", new KeyTrigger(KeyInput.KEY_A));
            app.getInputManager().addMapping("right", new KeyTrigger(KeyInput.KEY_D));
            app.getInputManager().addMapping("up", new KeyTrigger(KeyInput.KEY_W));
            app.getInputManager().addMapping("down", new KeyTrigger(KeyInput.KEY_S));
            app.getInputManager().addMapping("zoom in", new KeyTrigger(KeyInput.KEY_Q));
            app.getInputManager().addMapping("zoom out", new KeyTrigger(KeyInput.KEY_E));
            app.getInputManager().addMapping("to the menu", new KeyTrigger(KeyInput.KEY_M));
            app.getInputManager().addMapping("left click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
            app.getInputManager().addMapping("right click", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        } else {
            app.getInputManager().clearMappings();
            app.getRootNode().detachChild(battleRoot);
        }
    }

    @Override
    public void initialize(AppStateManager stateManager, Application appl) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) appl;
        battleRoot = new Node("Battle Root");
        light = new AmbientLight();
        light.setColor(ColorRGBA.White);
        camPos = new Vector3f(0, 50, 0);

        battleRoot.addLight(light);
        setEnabled(false);
    }
}
