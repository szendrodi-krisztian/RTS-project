package mygame;

import city.City;
import city.building.Building;
import com.jme3.app.Application;
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
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;

/**
 *
 * @author Krisz
 */
public class CityState extends AbstractAppStateWithRoot {

    private City city;

    public CityState(AppStateManager sm) {

    }

    boolean to_menu;

    @Override
    public void update(float tpf) {
        if (isEnabled()) {
            if (to_menu) {
                setEnabled(false);
                stateManager.detach(this);
                stateManager.attach(new MainMenuState());
                to_menu = false;
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            flyCam.setEnabled(false);
            camera.setLocation(new Vector3f(8, 15, 7));
            camera.lookAt(new Vector3f(8, 0, 7), Vector3f.UNIT_Y);
            ActionListener actl;
            actl = new ActionListener() {

                @Override
                public void onAction(String name, boolean isPressed, float tpf) {
                    switch (name.toLowerCase()) {
                        case "to the menu":
                            if (!isPressed) {
                                return;
                            }
                            to_menu = true;
                            break;
                        case "click":
                            Geometry g = getMouseRayCastGeometry();
                            for (Building b : city.getBuildings()) {
                                if (b.getName().equals(g.getName())) {
                                    b.openWindow(stateManager);
                                    inputManager.deleteMapping("click");
                                    break;
                                }
                            }
                            break;
                    }
                }
            };

            inputManager.addListener(actl, new String[]{"to the menu", "click"});
            inputManager.addMapping("to the menu", new KeyTrigger(KeyInput.KEY_M));
            inputManager.addMapping("click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        } else {
            inputManager.clearMappings();
        }
    }

    private Geometry getMouseRayCastGeometry() {
        CollisionResults results = new CollisionResults();
        Vector2f pos = inputManager.getCursorPosition().clone();
        Vector3f cur3d = camera.getWorldCoordinates(pos.clone(), 0f).clone();
        Vector3f dir = camera.getWorldCoordinates(pos.clone(), 1f).subtract(cur3d).normalizeLocal();
        Ray ray = new Ray(cur3d, dir);
        getRootNode().collideWith(ray, results);
        CollisionResult coll = results.getClosestCollision();
        if (coll == null) {
            return null;
        }
        return coll.getGeometry();
    }

    @Override
    public void initialize(AppStateManager stateManager, Application appl) {
        super.initialize(stateManager, appl);
        AmbientLight light = new AmbientLight();
        light.setColor(ColorRGBA.White);
        getRootNode().addLight(light);
        to_menu = false;
        city = new City(getRootNode(), assets);
        setEnabled(true);
    }

    @Override
    protected String getNiftyXMLName() {
        return "Interface/city_gui.xml";
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
