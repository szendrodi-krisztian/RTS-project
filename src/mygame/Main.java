package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.GeometryComparator;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Util;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
        Logger root = Logger.getLogger("");

        Handler[] handlers = root.getHandlers();

        for (int i = 0; i < handlers.length; i++) {

            if (handlers[i] instanceof ConsoleHandler) {

                ((ConsoleHandler) handlers[i]).setLevel(Level.SEVERE);

            }

        }
    }

    @Override
    public void simpleInitApp() {
        Util.init(assetManager);
        flyCam.setEnabled(false);
        final MainMenuState ms = new MainMenuState();
        stateManager.attach(ms);
        RenderQueue rq = viewPort.getQueue();
        rq.setGeometryComparator(RenderQueue.Bucket.Opaque, new GeometryComparator() {

            @Override
            public void setCamera(Camera cam) {

            }

            // This is needed because of the lack of Z-Buffer testing on BattleTerrain.
            // It cant have Z-Buffering enabled, in order to avoid stuterring, what is caused by the method of
            // transitioning beetwen textures.
            @Override
            public int compare(Geometry a, Geometry b) {
                String name1 = a.getName();
                String name2 = b.getName();
                if (name1.equals("BattleTerrain")) {
                    return -1;
                }
                if (name2.equals("BattleTerrain")) {
                    return 1;
                }
                float y1 = a.getWorldTranslation().y;
                float y2 = b.getWorldTranslation().y;
                if (y1 > y2) {
                    return 1;
                } else if (y1 > y2) {
                    return -1;
                }
                return 0;
            }
        });
    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    @Override
    public void simpleRender(RenderManager rm) {

    }
}
