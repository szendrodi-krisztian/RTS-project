package battle.gfx;

import battle.terrain.Terrain;
import battle.terrain.TerrainElement;
import battle.terrain.TerrainElementManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author szend
 */
public class MeshedTerrain {

    private final Terrain terrain;
    public final Node root;

    public MeshedTerrain(Terrain terrain, Node rootNode) {
        this.terrain = terrain;
        this.root = rootNode;
        Mesh mesh = new TerrainGridMesh(terrain.width(), terrain.height(), terrain.grid);

        Geometry geom = new Geometry("BattleTerrain", mesh);
        geom.setMaterial(TerrainElementManager.getInstance(null).getTerrainMaterial());

        geom.move(0, 0, 0);
        rootNode.attachChild(geom);

        for (TerrainElement type : TerrainElementManager.getInstance(null).getAllTerrains().values()) {
            if (!type.getLayer().equals("DECORATION")) {
                continue;
            }
            if (type.getName().equals("air")) {
                continue;
            }
            Mesh me = new TerrainDecorationMesh(terrain.width(), terrain.height(), terrain.grid, type);
            Geometry g = new Geometry("BattleDecor" + type.getName(), me);
            g.move(0, 0.00001f, 0);
            g.setMaterial(TerrainElementManager.getInstance(null).getDecorMaterial(type));
            rootNode.attachChild(g);
        }

    }

    public ArrayList<TerrainElement>[] getGrid() {
        return terrain.grid;
    }

    public Terrain raw() {
        return terrain;
    }

    public void reBuildDecor() {
        for (TerrainElement type : TerrainElementManager.getInstance(null).getAllTerrains().values()) {
            if (!type.getLayer().equals("DECORATION")) {
                continue;
            }
            if (type.getName().equals("air")) {
                continue;
            }
            Geometry bd = (Geometry) root.getChild("BattleDecor" + type.getName());
            TerrainDecorationMesh me = (TerrainDecorationMesh) bd.getMesh();
            me.update(raw().grid);
        }
    }

    public void reBuildTerrain(int x, int y) {
        Geometry bt = (Geometry) root.getChild("BattleTerrain");
        TerrainGridMesh mesh = (TerrainGridMesh) bt.getMesh();
        mesh.update(raw().grid, x, y);
    }

}
