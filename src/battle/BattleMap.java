package battle;

import battle.entity.SimpleUnit;
import battle.entity.Unit;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import battle.terrain.TerrainElement;
import battle.terrain.TerrainElementManager;
import battle.terrain.render.TerrainDecorationMesh;
import battle.terrain.render.TerrainGridMesh;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
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
    
    public int pathDistanceGrid[];
    public LinkedList<Vector2f> subsequentGrids;
    public List<Vector2f> changedArrayElements;
            
    public BattleMap(int n, int m, Node rootNode, AssetManager assets) {
        this.n = n;
        this.m = m;
        Unit.init(this, assets, rootNode);
        units = new Unit[n * m];
        SimpleUnit u = new SimpleUnit(2, 2);
        
        pathDistanceGrid = new int[n*m];
        subsequentGrids = new LinkedList<>();

        


        units[n*2+2] = u;

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
        buildGridMesh(n, m, rootNode, assets);

    }

    private void buildGridMesh(int n, int m, Node rootNode, AssetManager as) {
        Mesh mesh = new TerrainGridMesh(n, m, grid);
        
        Geometry geom = new Geometry("BattleTerrain", mesh);
        geom.setMaterial(TerrainElementManager.getInstance(null).getTerrainMaterial());
        geom.move(0, 0, 0);
        rootNode.attachChild(geom);

        Mesh me = new TerrainDecorationMesh(n, m, grid);
        Geometry g = new Geometry("BattleDecor", me);
        g.move(0, 0.1f, 0);
        g.setMaterial(TerrainElementManager.getInstance(null).getDecorMaterial());
        rootNode.attachChild(g);
    }

    public Vector2f dijkstra(int posX, int posY, int destX, int destY) {       
        subsequentGrids.clear();        
        int x,y;
        int neighbourX, neighbourY;
        Vector2f currentGrid = new Vector2f(posX,posY);
        Arrays.fill(pathDistanceGrid, Integer.MAX_VALUE);
        subsequentGrids.addLast(new Vector2f(posX,posY));
        pathDistanceGrid[posX*m+posY]=0; 
        while(pathDistanceGrid[destX*m+destY]==Integer.MAX_VALUE)
        {
            if(subsequentGrids.isEmpty())
                return new Vector2f(0,0);
            else
                currentGrid=subsequentGrids.remove();
            System.out.println("destdist: "+pathDistanceGrid[(int)destX*m+(int)destY]);
            for(int i=0; i<9; i++)
            {
                if(i==4)
                    continue;
                neighbourX=(int)(i/3)-1;
                neighbourY=(int)(i%3)-1;
                if((currentGrid.x+neighbourX)+1>n || (currentGrid.x+neighbourY)+1>m || 
                (currentGrid.x+neighbourX)<0 || (currentGrid.y+neighbourY)<0)
                {
                    continue;
                }
                System.out.println(((int)currentGrid.x+neighbourX)*m+((int)currentGrid.y+neighbourY));
                if(grid[((int)currentGrid.x+neighbourX)*m+((int)currentGrid.y+neighbourY)].isAccesible() &&
                pathDistanceGrid[((int)currentGrid.x+neighbourX)*m+((int)currentGrid.y+neighbourY)] > pathDistanceGrid[(int)currentGrid.x*m+(int)currentGrid.y])
                {
                    pathDistanceGrid[((int)currentGrid.x+neighbourX)*m+((int)currentGrid.y+neighbourY)]=pathDistanceGrid[(int)currentGrid.x*m+(int)currentGrid.y]+1;
                    subsequentGrids.addLast(new Vector2f(currentGrid.x+neighbourX, currentGrid.y+neighbourY));
                    //changedArrayElements.add(new Vector2f(currentGrid.x+neighbourX, currentGrid.y+neighbourY));
                }
            }
            System.out.println("size: "+subsequentGrids.size());
        }
        System.out.println("posdist: "+pathDistanceGrid[(int)posX*m+(int)posY]);
        System.out.println("destdist: "+pathDistanceGrid[(int)destX*m+(int)destY]);
        currentGrid.x=destX;
        currentGrid.y=destY;
        for(int i=pathDistanceGrid[(int)destX*m+(int)destY]; i>1; i--)
        {
            for(int j=0; j<9; j++)
            {
                neighbourX=(int)(j/3)-1;
                neighbourY=(int)(j%3)-1;
                if(((int)currentGrid.x+neighbourX)>n-1 || ((int)currentGrid.y+neighbourY)>m-1)
                {
                    continue;
                }
                if(pathDistanceGrid[((int)currentGrid.x+neighbourX)*m+((int)currentGrid.y+neighbourY)] == pathDistanceGrid[(int)currentGrid.x*m+(int)currentGrid.y]-1)
                {
                    currentGrid.x+=neighbourX;
                    currentGrid.y+=neighbourY;
                    break;
                }
            }
        }
        x=(int)currentGrid.x-posX;
        y=(int)currentGrid.y-posY;
        System.out.println(x+","+y);
        return new Vector2f(x,y);
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
                    to.add(i + r);
                }
            }
        }
        for (int i = 0; i < from.size(); i++) {
            /*
             * BE CAREFUL DRAGONS AHEAD!
             * If for any reason two units overlap one of them is DELETED, but the geometry stucks in the scene
             * Theoretically, the pathfinding wont let this happen but if it does, this could be the problem you are looking for.
             */
            System.out.println("___move " + from.get(i) + " to " + to.get(i));
            units[to.get(i)] = units[from.get(i)];
            units[from.get(i)] = null;
        }
    }

}
