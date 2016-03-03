package city.building;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector2f;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.DroppableDroppedEvent;
import de.lessvoid.nifty.controls.Window;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import de.lessvoid.nifty.tools.SizeValueType;

/**
 * Appstate responsible to display the window of one building
 *
 * @author Krisz
 */
public class BuildingWindowState extends AbstractAppState implements ScreenController {

    protected NiftyJmeDisplay niftyDisplay;
    protected AppStateManager stateManager;
    protected SimpleApplication app;

    @Override
    public final void initialize(AppStateManager stateManager, Application appl) {
        super.initialize(stateManager, appl);
        app = (SimpleApplication) appl;
        this.stateManager = app.getStateManager();
        niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        Nifty n = niftyDisplay.getNifty();
        n.fromXml(getXMLName(), "start", this);
        app.getGuiViewPort().addProcessor(niftyDisplay);
        setEnabled(false);
    }

    @Override
    public void cleanup() {
        niftyDisplay.cleanup();
        niftyDisplay.getNifty().exit();
        app.getGuiViewPort().removeProcessor(niftyDisplay);
        niftyDisplay = null;
    }

    protected String getXMLName() {
        return "Interface/window.xml";
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }

    @Override
    public void setEnabled(boolean e) {
        super.setEnabled(e);
        if (!initialized) {
            return;
        }
        if (e) {
            niftyDisplay.getNifty().getCurrentScreen().findNiftyControl("window-3", Window.class).getElement().resetForHide();
            niftyDisplay.getNifty().getCurrentScreen().findNiftyControl("window-3", Window.class).getElement().show();
        } else {

        }
    }

    public final void openWindow(AppStateManager stateManager) {
        setEnabled(true);
    }

    @NiftyEventSubscriber(id = "drop")
    public void onWindowClosed(final String id, final DroppableDroppedEvent event) {

        System.out.println("dropthebass");
        Vector2f v = app.getInputManager().getCursorPosition();
        event.getDraggable().getElement().resetLayout();
        event.getDraggable().getElement().setConstraintX(new SizeValue(SizeValueType.Pixel, (int) v.x));
        event.getDraggable().getElement().setConstraintY(new SizeValue(SizeValueType.Pixel, (int) v.y));

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
