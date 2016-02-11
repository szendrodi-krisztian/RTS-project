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
import com.jme3.math.FastMath;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
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
        SimpleUnit u = new SimpleUnit(1, 1);
        
        pathDistanceGrid = new int[n*m];
        subsequentGrids = new LinkedList<>();

        u.moveTo(1, 2);
        SimpleUnit u2 = new SimpleUnit(5, 5);
        u2.moveTo(8, 8);
        units[n * 5 + 0] = u2;
        units[n * 1 + 2] = u;

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
        
        Mesh me = new TerrainDecorationMesh(n, m, grid);
        Geometry g = new Geometry("BattleDecor", me);
        g.setMaterial(TerrainElementManager.getInstance(null).getDecorMaterial());
        g.setQueueBucket(RenderQueue.Bucket.Transparent);
        rootNode.attachChild(g);
    }

    public Vector2f dijkstra(int posX, int posY, int destX, int destY) {       
        subsequentGrids.clear();        
        int x,y;
        int neighbourX=-1, neighbourY=-1;
        Vector2f currentGrid = new Vector2f(posX,posY);
        Arrays.fill(pathDistanceGrid, Integer.MAX_VALUE);
        subsequentGrids.addLast(new Vector2f(posX,posY));
        pathDistanceGrid[posX*m+posY]=0; 
        while(pathDistanceGrid[destX*m+destY]==Integer.MAX_VALUE)
        {
            if(subsequentGrids.isEmpty())
                return new Vector2f(0,0);
            else
                currentGrid=subsequentGrids.pollFirst();
            
            for(int i=0; i<9; i++)
            {
                if(i==4)
                    continue;
                neighbourX=(int)(i/3)-1;
                neighbourY=(int)(i%3)-1;
                if((currentGrid.x+neighbourX)+1>n || (currentGrid.x+neighbourY)+1>m || 
                (currentGrid.x+neighbourX)<0 || (currentGrid.x+neighbourY)<0)
                {
                    continue;
                }
                if(/*grid[((int)currentGrid.x+neighbourX)*m+((int)currentGrid.y+neighbourY)].isAccesible() &&*/ 
                pathDistanceGrid[((int)currentGrid.x+neighbourX)*m+((int)currentGrid.y+neighbourY)]+1 > pathDistanceGrid[(int)currentGrid.x*m+(int)currentGrid.y])
                {
                    pathDistanceGrid[((int)currentGrid.x+neighbourX)*m+((int)currentGrid.y+neighbourY)]=pathDistanceGrid[(int)currentGrid.x*m+(int)currentGrid.y]+1;
                    subsequentGrids.add(new Vector2f(currentGrid.x+neighbourX, currentGrid.y+neighbourY));
                    //changedArrayElements.add(new Vector2f(currentGrid.x+neighbourX, currentGrid.y+neighbourY));
                }
            }
        }
        System.out.print(pathDistanceGrid[(int)destX*m+(int)destY]);
        currentGrid.x=destX;
        currentGrid.y=destY;
        for(int i=pathDistanceGrid[(int)destX*m+(int)destY]; i>0; i--)
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
            units[to.get(i)] = units[from.get(i)];
            units[from.get(i)] = null;
        }
    }

}
