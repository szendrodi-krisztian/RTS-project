package battle;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import battle.terrain.TerrainElement;
import battle.terrain.TerrainElementManager;
import battle.terrain.render.TerrainGridMesh;
import com.jme3.math.FastMath;
import com.jme3.scene.Mesh;

/**
 *
 * @author Krisz
 */
public class BattleMap {

    public TerrainElement grid[];

    public BattleMap(int n, int m, Node rootNode, AssetManager assets) {
        grid = new TerrainElement[n * m];
        TerrainElement e = TerrainElementManager.getInstance(assets).getElementByName("stone");
        TerrainElement e2 = TerrainElementManager.getInstance(assets).getElementByName("grass");
        for (int i = 0; i < grid.length; i++) {
            if (FastMath.nextRandomInt(0, 10) < 5) {
                grid[i] = e;
            } else {
                grid[i] = e2;
            }
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
        return new Vector2f(1, 1);
    }

}
