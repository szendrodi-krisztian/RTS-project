package mygame;

import battle.BattleMap;
import battle.entity.SimpleUnit;
import battle.entity.Unit;
import battle.entity.group.Group;
import battle.entity.group.OneLineFormation;
import battle.gfx.ClickMesh;
import battle.path.Path;
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
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.util.List;
import util.Util;

/**
 *
 * @author szend
 */
public class MapEditorState extends AbstractAppState {

    private SimpleApplication app;

    private BattleMap map = null;

    private AmbientLight light;

    private Node battleRoot;

    private Vector3f camPos;

    boolean to_menu;

    @Override
    public void cleanup() {
        super.cleanup();
    }

    @Override
    public void update(float tpf) {
        if (isEnabled() && map != null) {
            map.tick(tpf);
            if (to_menu) {
                setEnabled(false);
                app.getStateManager().getState(MainMenuState.class).setEnabled(true);
                app.getStateManager().detach(this);
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
            app.getRootNode().attachChild(battleRoot);
            if (map == null) {
                map = new BattleMap(15, 15, battleRoot, app.getAssetManager());
                Group g1 = new Group(map, new OneLineFormation(map));
                for (int i = 0; i < 1; i++) {
                    //map.spawn(0, 0, g1, SimpleUnit.class);
                }
                //g1.moveTo(0, 0, 0);
            }
            app.getCamera().setLocation(camPos);
            Vector3f look = camPos.clone();
            look.y = 0;
            app.getCamera().lookAt(look, Vector3f.UNIT_Y);
            app.getFlyByCamera().setMoveSpeed(50);
            app.getFlyByCamera().setEnabled(false);

            AnalogListener al = new AnalogListener() {

                final int mult = 25;

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
                        case "mouse_move":
                            Vector2f cur2 = app.getInputManager().getCursorPosition();

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
                    app.getCamera().setLocation(camPos);
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
                        Material m = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                        m.setTexture("ColorMap", app.getAssetManager().loadTexture("Interface/move.png"));
                        m.getAdditionalRenderState().setDepthTest(false);
                        m.getAdditionalRenderState().setDepthWrite(false);
                        m.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                        m.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
                        click_gui.setMaterial(m);
                        click_gui.setLocalRotation(new Quaternion().fromAngleNormalAxis(FastMath.DEG_TO_RAD * 22.5f, Vector3f.UNIT_Y));
                        click_gui.setQueueBucket(RenderQueue.Bucket.Transparent);

                        battleRoot.attachChild(click_gui);
                        click_filler = new Geometry("click", new ClickMesh());
                        Material m2 = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                        m2.setTexture("ColorMap", app.getAssetManager().loadTexture("Interface/move_fill.png"));
                        m2.getAdditionalRenderState().setDepthTest(false);
                        m2.getAdditionalRenderState().setDepthWrite(false);
                        m2.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                        m2.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
                        click_filler.setMaterial(m2);
                        click_filler.setLocalRotation(new Quaternion().fromAngleNormalAxis(FastMath.DEG_TO_RAD * 22.5f, Vector3f.UNIT_Y));
                        click_filler.setQueueBucket(RenderQueue.Bucket.Transparent);

                        battleRoot.attachChild(click_filler);
                    }
                    switch (name.toLowerCase()) {
                        case "left click":
                            if (!isPressed) {
                                return;
                            }
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
                                    Vector2f cur2 = app.getInputManager().getCursorPosition();
                                    screen_mouse_right_pos = cur2.clone();
                                    Vector3f cur3d2 = app.getCamera().getWorldCoordinates(cur2.clone(), 0f).clone();
                                    Vector3f dir2 = app.getCamera().getWorldCoordinates(cur2.clone(), 1f).subtract(cur3d2).normalizeLocal();
                                    Ray ray2 = new Ray(cur3d2, dir2);
                                    battleRoot.collideWith(ray2, results2);
                                    CollisionResult coll2 = results2.getClosestCollision();
                                    if (coll2 == null) {
                                        return;
                                    }
                                    right_pos = new Vector2f(coll2.getContactPoint().x, coll2.getContactPoint().z);
                                    click_gui.setLocalTranslation(right_pos.x, 1.1f, right_pos.y);
                                    click_filler.setLocalTranslation(right_pos.x, 1.2f, right_pos.y);
                                    List<Vector2f> p = new Path((int) select.getLeader().pos.x, (int) select.getLeader().pos.y, (int) select.getLeader().destination.x, (int) select.getLeader().destination.y, map);
                                    FloatBuffer vb = BufferUtils.createFloatBuffer(p.size() * 3 + 3);
                                    for (Vector2f v : p) {
                                        vb.put(v.x).put(0.2f).put(v.y);
                                    }
                                    vb.put(select.getLeader().pos.x).put(0.2f).put(select.getLeader().pos.y);
                                    battleRoot.detachChildNamed("path");
                                    select_path = new Mesh();
                                    select_path.setBuffer(VertexBuffer.Type.Position, 3, vb);
                                    select_path.setMode(Mesh.Mode.LineStrip);
                                    select_path.updateBound();
                                    Geometry g = new Geometry("path", select_path);
                                    Material m = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
                                    m.setColor("Color", ColorRGBA.Pink);
                                    g.setMaterial(m);
                                    g.move(0.5f, 0, 0.5f);
                                    battleRoot.attachChild(g);
                                } else {
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
                                    Vector2f release = new Vector2f(coll2.getContactPoint().x, coll2.getContactPoint().z);
                                    if (release.subtract(right_pos).lengthSquared() > 0.5f) {
                                        select.moveTo((int) right_pos.x, (int) right_pos.y, Util.angleToPositiveToOctave(FastMath.RAD_TO_DEG * (release.subtractLocal(right_pos)).angleBetween(new Vector2f(0, 1))));
                                    } else {
                                        select.moveTo((int) right_pos.x, (int) right_pos.y, select.getLeader().finalRotationAngle);
                                    }
                                    click_gui.setLocalTranslation(0, 1.1f, 0);
                                    click_filler.setLocalTranslation(0, 1.2f, 0);
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

            app.getInputManager().addListener(al, new String[]{"left", "right", "up", "down", "zoom in", "zoom out", "mouse_move"});
            app.getInputManager().addListener(actl, new String[]{"left click", "right click", "to the menu", "shoot"});
            app.getInputManager().addMapping("left", new KeyTrigger(KeyInput.KEY_A));
            app.getInputManager().addMapping("right", new KeyTrigger(KeyInput.KEY_D));
            app.getInputManager().addMapping("up", new KeyTrigger(KeyInput.KEY_W));
            app.getInputManager().addMapping("down", new KeyTrigger(KeyInput.KEY_S));
            app.getInputManager().addMapping("zoom in", new KeyTrigger(KeyInput.KEY_Q));
            app.getInputManager().addMapping("zoom out", new KeyTrigger(KeyInput.KEY_E));
            app.getInputManager().addMapping("to the menu", new KeyTrigger(KeyInput.KEY_M));
            app.getInputManager().addMapping("shoot", new KeyTrigger(KeyInput.KEY_SPACE));
            app.getInputManager().addMapping("left click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
            app.getInputManager().addMapping("right click", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
            app.getInputManager().addMapping("mouse_move", new MouseAxisTrigger(MouseInput.AXIS_X, false));
            app.getInputManager().addMapping("mouse_move", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
            app.getInputManager().addMapping("mouse_move", new MouseAxisTrigger(MouseInput.AXIS_X, true));
            app.getInputManager().addMapping("mouse_move", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        } else {
            app.getInputManager().clearMappings();
            app.getRootNode().detachChild(battleRoot);
        }
    }

    @Override
    public void initialize(AppStateManager stateManager, Application appl) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) appl;
        battleRoot = new Node("MapEdit Root");
        light = new AmbientLight();
        light.setColor(ColorRGBA.White);
        camPos = new Vector3f(0, 20, 0);

        battleRoot.addLight(light);
        setEnabled(true);
        to_menu = false;
    }
}