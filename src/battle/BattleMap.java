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

    public Unit units[];

    public int n, m;

    public BattleMap(int n, int m, Node rootNode, AssetManager assets) {
        this.n = n;
        this.m = m;
        Unit.init(this, assets, rootNode);
        units = new Unit[n * m];
        SimpleUnit u = new SimpleUnit(1, 1);

        u.moveTo(10, 5);
        SimpleUnit u2 = new SimpleUnit(5, 0);
        u2.moveTo(12, 3);
        units[n * 5 + 0] = u2;
        units[n * 1 + 1] = u;

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
    
    List<Integer> from = new ArrayList<>();
    List<Integer> to = new ArrayList<>();

    public void tick(float tpf) {
        from.clear();
        to.clear();
        for (int i = 0; i < units.length; i++) {
            if (units[i] != null) {
                int r = units[i].move(tpf);
                if (r != 0) {
                    from.add(i);
                    to.add(i+r);
                }
            }
        }
        for(int i = 0;i<from.size();i++){
            units[to.get(i)] = units[from.get(i)];
            units[from.get(i)] = null;
        }
    }

}
