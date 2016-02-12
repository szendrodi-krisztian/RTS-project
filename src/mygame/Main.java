package mygame;

import battle.BattleMap;
import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;

public class Main extends SimpleApplication {

    BattleMap map;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        final BattleState bs = new BattleState();
        final MainMenuState ms = new MainMenuState();
        stateManager.attach(bs);
        stateManager.attach(ms);

    }

    @Override
    public void simpleUpdate(float tpf) {

    }

    @Override
    public void simpleRender(RenderManager rm) {

    }
}
