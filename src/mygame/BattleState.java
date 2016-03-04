package mygame;

import battle.BattleMap;
import battle.entity.SimpleUnit;
import battle.entity.Unit;
import battle.entity.group.AbstractFormation;
import battle.entity.group.Group;
import battle.entity.group.OneLineFormation;
import battle.entity.group.TwoLineFormation;
import battle.gfx.ClickMesh;
import battle.gfx.PathMesh;
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
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
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
            if (select != null && select.size() > 0) {
                displayPath(select.getLeader().path, select.getLeader().position());
            }
            if (to_menu) {
                setEnabled(false);
                stateManager.detach(this);
                stateManager.attach(new MainMenuState());
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
                    switch (name.toLowerCase()) {
                        case "left click":
                            if (!isPressed) {
                                return;
                            }
                            Vector2f hit_geom = getMouseRayCastIntCoords();
                            Unit unit = null;
                            if (!map.getUnitsAt(hit_geom.x, hit_geom.y).isEmpty()) {
                                unit = map.getUnitsAt(hit_geom.x, hit_geom.y).get(0);
                            }
                            System.out.println(unit);
                            if (unit != null) {
                                select = unit.getGroup();
                            }
                            break;
                        case "right click":
                            if (select != null) {
                                if (isPressed) {
                                    right_pos = getMouseRayCastIntCoords();
                                    click_gui.setLocalTranslation(right_pos.x, 1.1f, right_pos.y);
                                    click_filler.setLocalTranslation(right_pos.x, 1.1001f, right_pos.y);

                                } else {
                                    Vector2f release = getMouseRayCastIntCoords();
                                    if (release.subtract(right_pos).lengthSquared() > 0.5f) {
                                        select.moveTo((int) right_pos.x, (int) right_pos.y, Util.angleToPositiveToOctave(FastMath.RAD_TO_DEG * (release.subtractLocal(right_pos)).angleBetween(new Vector2f(0, 1))));
                                    } else {
                                        select.moveTo((int) right_pos.x, (int) right_pos.y, select.getLeader().finalRotationAngle);
                                    }
                                    click_gui.setLocalTranslation(0, 1.1f, 0);
                                    click_filler.setLocalTranslation(0, 1.10001f, 0);
                                    Path p = new Path((int) select.getLeader().pos.x, (int) select.getLeader().pos.y, (int) select.getLeader().destination.x, (int) select.getLeader().destination.y, map);
                                    displayPath(p, select.getLeader().position());
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

    private void displayPath(Path p, Vector2f start) {
        getRootNode().detachChildNamed("path");
        PathMesh mesh = new PathMesh(p, start);
        Geometry g = new Geometry("path", mesh);
        Material m = new Material(Util.assets(), "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", ColorRGBA.White);
        g.setMaterial(m);
        getRootNode().attachChild(g);
    }

    private Group spawnGroup(AbstractFormation formation, int numUnits, int team) {
        Group g = new Group(map, formation, team);
        for (int i = 0; i < numUnits; i++) {
            map.spawn(0, 0, g, SimpleUnit.class);
        }
        return g;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application appl) {
        super.initialize(stateManager, appl);
        light = new AmbientLight();
        light.setColor(ColorRGBA.White);
        if (map == null) {
            map = new BattleMap(100, 100, getRootNode());
            spawnGroup(new OneLineFormation(map), 10, 1).moveTo(20, 30, 0);
            spawnGroup(new TwoLineFormation(map), 14, 1).moveTo(5, 15, 0);
        }
        getRootNode().addLight(light);
        to_menu = false;
        click_gui = new Geometry("click", new ClickMesh());
        Material m = new Material(Util.assets(), "Common/MatDefs/Misc/Unshaded.j3md");
        m.setTexture("ColorMap", Util.assets().loadTexture("Interface/move.png"));
        m.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        m.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        click_gui.setMaterial(m);
        click_gui.setLocalRotation(new Quaternion().fromAngleNormalAxis(FastMath.DEG_TO_RAD * 22.5f, Vector3f.UNIT_Y));

        getRootNode().attachChild(click_gui);

        click_filler = new Geometry("click", new ClickMesh());
        Material m2 = new Material(Util.assets(), "Common/MatDefs/Misc/Unshaded.j3md");
        m2.setTexture("ColorMap", Util.assets().loadTexture("Interface/move_fill.png"));
        m2.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        m2.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        click_filler.setMaterial(m2);
        click_filler.setLocalRotation(new Quaternion().fromAngleNormalAxis(FastMath.DEG_TO_RAD * 22.5f, Vector3f.UNIT_Y));
        getRootNode().attachChild(click_filler);
        setEnabled(true);
    }

    private Vector2f getMouseRayCastIntCoords() {
        CollisionResults results = new CollisionResults();
        screen_mouse_right_pos = inputManager.getCursorPosition().clone();
        Vector3f cur3d = camera.getWorldCoordinates(screen_mouse_right_pos.clone(), 0f).clone();
        Vector3f dir = camera.getWorldCoordinates(screen_mouse_right_pos.clone(), 1f).subtract(cur3d).normalizeLocal();
        Ray ray = new Ray(cur3d, dir);
        getRootNode().collideWith(ray, results);
        CollisionResult coll = results.getClosestCollision();
        if (coll == null) {
            return Vector2f.ZERO;
        }
        return new Vector2f(coll.getContactPoint().x, coll.getContactPoint().z);
    }

    @Override
    protected String getNiftyXMLName() {
        return "Interface/battle_gui.xml";
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
