/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import battle.BattleMap;
import battle.entity.SimpleUnit;
import battle.entity.Unit;
import battle.entity.group.Group;
import battle.entity.group.OneLineFormation;
import battle.entity.group.TwoLineFormation;
import battle.gfx.ClickMesh;
import battle.path.Path;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.util.List;
import util.Util;

/**
 *
 * @author szend
 */
public class BattleState extends AbstractAppStateWithRoot {

    private BattleMap map;

    private AmbientLight light;

    boolean to_menu;

    public BattleState(AppStateManager sm) {
        childStateList.add(new CameraMovementState());
        for (AbstractAppState state : childStateList) {
            sm.attach(state);
        }
    }

    @Override
    public void update(float tpf) {
        if (isEnabled()) {
            map.tick(tpf);
            if (to_menu) {
                setEnabled(false);
                stateManager.getState(MainMenuState.class).setEnabled(true);
                to_menu = false;
            }
        }
    }

    Geometry click_gui = null;
    Geometry click_filler = null;
    Vector2f screen_mouse_right_pos;

    Group select = null;

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (enabled) {

            AnalogListener al = new AnalogListener() {

                @Override
                public void onAnalog(String name, float value, float tpf) {
                    switch (name.toLowerCase()) {

                        case "mouse_move":
                            Vector2f cur2 = inputManager.getCursorPosition();

                            Quaternion q = new Quaternion();
                            if (click_gui == null) {
                                return;
                            }
                            if (screen_mouse_right_pos == null) {
                                return;
                            }
                            Vector2f pos = cur2.subtract(screen_mouse_right_pos);
                            float angle = new Vector2f(0, 1).angleBetween(pos.normalizeLocal());
                            angle = FastMath.DEG_TO_RAD * Util.angleToPositiveToOctave(FastMath.RAD_TO_DEG * angle);
                            q.fromAngleNormalAxis(angle + FastMath.PI - FastMath.QUARTER_PI / 2f, Vector3f.UNIT_Y);
                            click_filler.setLocalRotation(q);
                            break;

                    }
                }
            };

            ActionListener actl;
            actl = new ActionListener() {

                Mesh select_path;
                Vector2f right_pos;

                @Override
                public void onAction(String name, boolean isPressed, float tpf) {
                    if (click_gui == null) {
                        click_gui = new Geometry("click", new ClickMesh());
                        Material m = new Material(assets, "Common/MatDefs/Misc/Unshaded.j3md");
                        m.setTexture("ColorMap", assets.loadTexture("Interface/move.png"));
                        m.getAdditionalRenderState().setDepthTest(false);
                        m.getAdditionalRenderState().setDepthWrite(false);
                        m.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                        click_gui.setMaterial(m);
                        click_gui.setLocalRotation(new Quaternion().fromAngleNormalAxis(FastMath.DEG_TO_RAD * 22.5f, Vector3f.UNIT_Y));
                        click_gui.setQueueBucket(RenderQueue.Bucket.Transparent);
                        getRootNode().attachChild(click_gui);
                        
                        click_filler = new Geometry("click", new ClickMesh());
                        Material m2 = new Material(assets, "Common/MatDefs/Misc/Unshaded.j3md");
                        m2.setTexture("ColorMap", assets.loadTexture("Interface/move_fill.png"));
                        m2.getAdditionalRenderState().setDepthTest(false);
                        m2.getAdditionalRenderState().setDepthWrite(false);
                        m2.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                        click_filler.setMaterial(m2);
                        click_filler.setLocalRotation(new Quaternion().fromAngleNormalAxis(FastMath.DEG_TO_RAD * 22.5f, Vector3f.UNIT_Y));
                        click_filler.setQueueBucket(RenderQueue.Bucket.Transparent);

                        getRootNode().attachChild(click_filler);
                    }
                    switch (name.toLowerCase()) {
                        case "left click":
                            if (!isPressed) {
                                return;
                            }
                            CollisionResults results = new CollisionResults();
                            Vector2f cur = inputManager.getCursorPosition();
                            Vector3f cur3d = camera.getWorldCoordinates(cur.clone(), 0f).clone();
                            Vector3f dir = camera.getWorldCoordinates(cur.clone(), 1f).subtract(cur3d).normalizeLocal();
                            Ray ray = new Ray(cur3d, dir);
                            getRootNode().collideWith(ray, results);
                            CollisionResult coll = results.getClosestCollision();
                            if (coll == null) {
                                return;
                            }
                            Vector3f hit_geom = coll.getGeometry().getWorldTranslation();
                            Unit unit = null;
                            if (!map.getUnitsAt(hit_geom.x, hit_geom.z).isEmpty()) {
                                unit = map.getUnitsAt(hit_geom.x, hit_geom.z).get(0);
                            }
                            System.out.println(unit);
                            if (unit != null) {
                                select = unit.getGroup();
                            }
                            break;
                        case "right click":
                            if (select != null) {
                                if (isPressed) {
                                    CollisionResults results2 = new CollisionResults();
                                    Vector2f cur2 = inputManager.getCursorPosition();
                                    screen_mouse_right_pos = cur2.clone();
                                    Vector3f cur3d2 = camera.getWorldCoordinates(cur2.clone(), 0f).clone();
                                    Vector3f dir2 = camera.getWorldCoordinates(cur2.clone(), 1f).subtract(cur3d2).normalizeLocal();
                                    Ray ray2 = new Ray(cur3d2, dir2);
                                    getRootNode().collideWith(ray2, results2);
                                    CollisionResult coll2 = results2.getClosestCollision();
                                    if (coll2 == null) {
                                        return;
                                    }
                                    right_pos = new Vector2f(coll2.getContactPoint().x, coll2.getContactPoint().z);
                                    click_gui.setLocalTranslation(right_pos.x, 1.1f, right_pos.y);
                                    click_filler.setLocalTranslation(right_pos.x, 1.10001f, right_pos.y);
                                    List<Vector2f> p = new Path((int) select.getLeader().pos.x, (int) select.getLeader().pos.y, (int) select.getLeader().destination.x, (int) select.getLeader().destination.y, map);
                                    FloatBuffer vb = BufferUtils.createFloatBuffer(p.size() * 3 + 3);
                                    for (Vector2f v : p) {
                                        vb.put(v.x).put(0.2f).put(v.y);
                                    }
                                    vb.put(select.getLeader().pos.x).put(0.2f).put(select.getLeader().pos.y);
                                    getRootNode().detachChildNamed("path");
                                    select_path = new Mesh();
                                    select_path.setBuffer(VertexBuffer.Type.Position, 3, vb);
                                    select_path.setMode(Mesh.Mode.LineStrip);
                                    select_path.updateBound();
                                    Geometry g = new Geometry("path", select_path);
                                    Material m = new Material(assets, "Common/MatDefs/Misc/Unshaded.j3md");
                                    m.setColor("Color", ColorRGBA.Pink);
                                    g.setMaterial(m);
                                    g.move(0.5f, 0, 0.5f);
                                    getRootNode().attachChild(g);
                                } else {
                                    CollisionResults results2 = new CollisionResults();
                                    Vector2f cur2 = inputManager.getCursorPosition();
                                    Vector3f cur3d2 = camera.getWorldCoordinates(cur2.clone(), 0f).clone();
                                    Vector3f dir2 = camera.getWorldCoordinates(cur2.clone(), 1f).subtract(cur3d2).normalizeLocal();
                                    Ray ray2 = new Ray(cur3d2, dir2);
                                    getRootNode().collideWith(ray2, results2);
                                    CollisionResult coll2 = results2.getClosestCollision();
                                    if (coll2 == null) {
                                        return;
                                    }
                                    Vector2f release = new Vector2f(coll2.getContactPoint().x, coll2.getContactPoint().z);
                                    if (release.subtract(right_pos).lengthSquared() > 0.5f) {
                                        select.moveTo((int) right_pos.x, (int) right_pos.y, Util.angleToPositiveToOctave(FastMath.RAD_TO_DEG * (release.subtractLocal(right_pos)).angleBetween(new Vector2f(0, 1))));
                                    } else {
                                        select.moveTo((int) right_pos.x, (int) right_pos.y, select.getLeader().finalRotationAngle);
                                    }
                                    click_gui.setLocalTranslation(0, 1.1f, 0);
                                    click_filler.setLocalTranslation(0, 1.10001f, 0);
                                }
                            }
                            break;

                        case "to the menu":
                            if (!isPressed) {
                                return;
                            }
                            to_menu = true;
                            break;
                        case "shoot":
                            if (!isPressed) {
                                return;
                            }
                            if (select != null) {
                                select.attack();
                            }
                            break;
                    }
                }
            };

            inputManager.addListener(al, new String[]{"mouse_move"});
            inputManager.addListener(actl, new String[]{"left click", "right click", "to the menu", "shoot"});
            inputManager.addMapping("to the menu", new KeyTrigger(KeyInput.KEY_M));
            inputManager.addMapping("shoot", new KeyTrigger(KeyInput.KEY_SPACE));
            inputManager.addMapping("left click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
            inputManager.addMapping("right click", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
            inputManager.addMapping("mouse_move", new MouseAxisTrigger(MouseInput.AXIS_X, false));
            inputManager.addMapping("mouse_move", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
            inputManager.addMapping("mouse_move", new MouseAxisTrigger(MouseInput.AXIS_X, true));
            inputManager.addMapping("mouse_move", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        } else {
            inputManager.clearMappings();
        }
    }

    @Override
    public void initialize(AppStateManager stateManager, Application appl) {
        super.initialize(stateManager, appl);
        light = new AmbientLight();
        light.setColor(ColorRGBA.White);
        if (map == null) {
            map = new BattleMap(100, 100, getRootNode(), assets);

            Group g1 = new Group(map, new OneLineFormation(map));
            for (int i = 0; i < 1; i++) {
                map.spawn(0, 0, g1, SimpleUnit.class);
            }
            g1.moveTo(10, 10, 0);

            Group g2 = new Group(map, new TwoLineFormation(map));
            for (int i = 0; i < 12; i++) {
                map.spawn(0, 0, g2, SimpleUnit.class);
            }
            g2.moveTo(5, 15, 0);
        }
        getRootNode().addLight(light);
        to_menu = false;
        setEnabled(false);
    }
}
