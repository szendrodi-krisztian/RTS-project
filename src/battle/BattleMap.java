package battle;

import battle.entity.SimpleUnit;
import battle.entity.Unit;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import battle.terrain.TerrainElement;
import battle.terrain.TerrainElementManager;
import battle.terrain.render.TerrainGridMesh;
import com.jme3.math.FastMath;
import com.jme3.scene.Mesh;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Krisz
 */
public class BattleMap {

    public TerrainElement grid[];

    public List<Unit> units = new ArrayList<>();

    public BattleMap(int n, int m, Node rootNode, AssetManager assets) {
        Unit.init(this, assets, rootNode);
        SimpleUnit u = new SimpleUnit();
        u.moveTo(10, 5);
        SimpleUnit u2 = new SimpleUnit();
        u2.moveTo(12, 3);
        units.add(u);
        units.add(u2);

        grid = new TerrainElement[n * m];
        Map<String, TerrainElement> all = TerrainElementManager.getInstance(assets).getAllTerrains();
        for (int i = 0; i < grid.length; i++) {
            int random = FastMath.nextRandomInt(0, all.size());
            Iterator<TerrainElement> iter = all.values().iterator();
            while (--random > 0) {
                iter.next();
            }
            grid[i] = iter.next();
        }
        buildGridMesh(n, m, rootNode);

    }

    private void buildGridMesh(int n, int m, Node rootNode) {
        Mesh mesh = new TerrainGridMesh(n, m, grid);
        Geometry geom = new Geometry("BattleTerrain", mesh);
        geom.setMaterial(TerrainElementManager.getInstance(null).getTerrainMaterial());
        rootNode.attachChild(geom);
    }

    public Vector2f dijkstra(int posX, int posY, float destX, float destY) {
        int x = 0, y = 0;
        if (destX > posX) {
            x = 1;
        }
        if (destX < posX) {
            x = -1;
        }
        if (destY > posY) {
            y = 1;
        }
        if (destY < posY) {
            y = -1;
        }
        return new Vector2f(x, y);
    }

    public void tick(float tpf) {
        for (Unit u : units) {
            u.move(tpf);
        }
    }

}
