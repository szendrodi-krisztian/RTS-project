package mygame;

import battle.BattleMap;
import battle.terrain.TerrainElement;
import battle.terrain.TerrainElementManager;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.MapFile;

/**
 *
 * @author szend
 */
public final class MapEditorState extends AbstractAppStateWithRoot {

    private BattleMap map = null;

    boolean to_menu;

    public MapEditorState(AppStateManager sm) {
        childStateList.add(new CameraMovementState());
        for (AbstractAppState state : childStateList) {
            sm.attach(state);
        }
    }

    @Override
    public void update(float tpf) {
        if (isEnabled() && map != null) {
            map.tick(tpf);
            if (to_menu) {
                setEnabled(false);
                stateManager.getState(MainMenuState.class).setEnabled(true);
                to_menu = false;
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            if (map == null) {
                try {
                    MapFile mapFile = new MapFile("plswork", assets);
                    mapFile.read();
                    map = new BattleMap(mapFile, myRoot);
                } catch (IOException ex) {
                    Logger.getLogger(MapEditorState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            ActionListener actl;
            actl = new ActionListener() {

                @Override
                public void onAction(String name, boolean isPressed, float tpf) {
                    switch (name.toLowerCase()) {
                        case "left click":
                            if (!isPressed) {
                                Vector2f clicked = getMouseRayCastIntCoords();
                                map.getTerrain().raw().setTypeAt(getSelectedTerrainType(), clicked);
                                map.getTerrain().reBuild();
                            }
                            break;
                        case "right click":

                            break;
                        case "to the menu":
                            if (!isPressed) {
                                return;
                            }
                            to_menu = true;
                            break;
                    }
                }
            };

            inputManager.addListener(actl, new String[]{"left click", "right click", "to the menu"});
            inputManager.addMapping("to the menu", new KeyTrigger(KeyInput.KEY_M));
            inputManager.addMapping("left click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
            inputManager.addMapping("right click", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        } else {
            inputManager.clearMappings();
            if (map != null) {
                MapFile f = new MapFile("plssave", map.getTerrain().raw());
                try {
                    f.write();
                } catch (IOException ex) {
                    Logger.getLogger(MapEditorState.class.getName()).log(Level.SEVERE, null, ex);
                }
                myRoot.detachAllChildren();
                map = null;
            }
        }
    }

    private Vector2f getMouseRayCastIntCoords() {
        CollisionResults results = new CollisionResults();
        Vector2f cur = inputManager.getCursorPosition();
        Vector3f cur3d = camera.getWorldCoordinates(cur.clone(), 0f).clone();
        Vector3f dir = camera.getWorldCoordinates(cur.clone(), 1f).subtract(cur3d).normalizeLocal();
        Ray ray = new Ray(cur3d, dir);
        getRootNode().collideWith(ray, results);
        CollisionResult coll = results.getClosestCollision();
        if (coll == null) {
            return Vector2f.ZERO;
        }
        return new Vector2f(coll.getContactPoint().x, coll.getContactPoint().z);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application appl) {
        super.initialize(stateManager, appl);
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.White);
        getRootNode().addLight(light);
        to_menu = false;
        setEnabled(false);
    }

    private TerrainElement getSelectedTerrainType() {
        DropDown<String> dd = niftyDisplay.getNifty().getCurrentScreen().findNiftyControl("SelectDrop", DropDown.class);
        String select = dd.getSelection();
        return TerrainElementManager.getInstance(assets).getElementByName(select);
    }

    public void resizeMap() {
        TextField tf = (TextField) niftyDisplay.getNifty().getCurrentScreen().findControl("GTextfield0", Controller.class);
        TextField tf2 = (TextField) niftyDisplay.getNifty().getCurrentScreen().findControl("GTextfield1", Controller.class);
        int w = Integer.parseInt(tf.getDisplayedText());
        int h = Integer.parseInt(tf2.getDisplayedText());
        myRoot.detachAllChildren();
        map = new BattleMap(w, h, myRoot, assets);
    }

    public void regenerate() {
        myRoot.detachAllChildren();
        map = new BattleMap(map.mapWidth, map.mapHeight, myRoot, assets, FastMath.nextRandomInt());
    }

    @Override
    protected String getNiftyXMLName() {
        return "Interface/map_editor_gui.xml";
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        // Set up dropdown for selecting terrain to place
        DropDown<String> drop;
        drop = nifty.getCurrentScreen().findNiftyControl("SelectDrop", DropDown.class);
        for (String s : TerrainElementManager.getInstance(assets).getAllTerrains().keySet()) {
            drop.addItem(s);
        }
    }

    @Override
    public void onStartScreen() {

    }

    @Override
    public void onEndScreen() {

    }
}
