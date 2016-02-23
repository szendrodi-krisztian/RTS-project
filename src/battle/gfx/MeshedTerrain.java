package battle.gfx;

import battle.terrain.Terrain;
import battle.terrain.TerrainElement;
import battle.terrain.TerrainElementManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

/**
 *
 * @author szend
 */
public class MeshedTerrain {

    private final Terrain terrain;

    public MeshedTerrain(Terrain terrain, Node rootNode) {
        this.terrain = terrain;
        Mesh mesh = new TerrainGridMesh(terrain.width(), terrain.height(), terrain.grid);

        Geometry geom = new Geometry("BattleTerrain", mesh);
        geom.setMaterial(TerrainElementManager.getInstance(null).getTerrainMaterial());
        
        geom.move(0, 0, 0);
        rootNode.attachChild(geom);

        Mesh me = new TerrainDecorationMesh(terrain.mapWidth, terrain.height(), terrain.grid);
        Geometry g = new Geometry("BattleDecor", me);
        g.move(0, 0.00001f, 0);
        g.setMaterial(TerrainElementManager.getInstance(null).getDecorMaterial());
        rootNode.attachChild(g);
    }
    
    public TerrainElement[] getGrid(){
        return terrain.grid;
    }
    
    public Terrain raw(){
        return terrain;
    }

}
