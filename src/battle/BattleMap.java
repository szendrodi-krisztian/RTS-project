package battle;

import battle.entity.SimpleUnit;
import battle.entity.Unit;
import battle.terrain.SimplexNoise;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import battle.terrain.TerrainElement;
import battle.terrain.TerrainElementManager;
import battle.terrain.render.TerrainDecorationMesh;
import battle.terrain.render.TerrainGridMesh;
import com.jme3.math.FastMath;
import com.jme3.scene.Mesh;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Krisz
 */
public class BattleMap {

    public TerrainElement grid[];

    public Unit units[];

    public int mapWidth, mapHeight;
    
    public int pathDistanceGrid[];
    public LinkedList<Vector2f> subsequentGrids;
    public List<Vector2f> changedArrayElements;
            
    public BattleMap(int mapWidth, int mapHeight, Node rootNode, AssetManager assets) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        Unit.init(this, assets, rootNode);
        units = new Unit[mapWidth * mapHeight];
        SimpleUnit u = new SimpleUnit(2, 2);
        
        pathDistanceGrid = new int[mapWidth*mapHeight];
        subsequentGrids = new LinkedList<>();

        


        units[mapHeight*2+2] = u;
        

        grid = new TerrainElement[mapWidth * mapHeight];
        SimplexNoise noise = new SimplexNoise(128, 0.3f, FastMath.nextRandomInt());
        SimplexNoise treenoise = new SimplexNoise(1000, 1.2f, FastMath.nextRandomInt());

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                float n = noise.getNoise(3 * i, 3 * j);
                if (n < -0.12) {
                    grid[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("water");
                    continue;
                }
                if (n < 0.015f) {
                    grid[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("stone");
                    continue;
                }
                if (treenoise.getNoise(20 * i, 20 * j) > 7f) {
                    grid[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("tree");
                } else {
                    grid[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("grass");
                }
            }
        }
        buildGridMesh(mapWidth, mapHeight, rootNode, assets);
        u.moveTo(20, 20);
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

    public Vector2f pathFinder(int posX, int posY, int destX, int destY) {
        if(!grid[destX*mapHeight+destY].isAccesible())
            return new Vector2f(0,0);
        if(posX==destX && posY==destY)
            return new Vector2f(0,0);
        subsequentGrids.clear();
        int neighbourX, neighbourY;
        int neighbours[][]={{0,1},{1,0},{0,-1},{-1,0},{1,1},{-1,1},{-1,-1},{1,-1}};
        Vector2f currentGrid = new Vector2f(posX,posY);
        Arrays.fill(pathDistanceGrid, Integer.MAX_VALUE);
        subsequentGrids.addLast(new Vector2f(posX,posY));
        pathDistanceGrid[posX*mapHeight+posY]=0; 
        while(pathDistanceGrid[destX*mapHeight+destY]==Integer.MAX_VALUE)
        {
            if(subsequentGrids.isEmpty())
                return new Vector2f(0,0);
            else
                currentGrid=subsequentGrids.remove();
            for(int i=0; i<8; i++)
            {
                neighbourX=neighbours[i][0];
                neighbourY=neighbours[i][1];
                if((currentGrid.x+neighbourX)+1>mapWidth || (currentGrid.y+neighbourY)+1>mapHeight || 
                (currentGrid.x+neighbourX)<0 || (currentGrid.y+neighbourY)<0)
                {
                    continue;
                }
                if(grid[((int)currentGrid.x+neighbourX)*mapHeight+((int)currentGrid.y+neighbourY)].isAccesible() &&
                pathDistanceGrid[((int)currentGrid.x+neighbourX)*mapHeight+((int)currentGrid.y+neighbourY)] > pathDistanceGrid[(int)currentGrid.x*mapHeight+(int)currentGrid.y]+1)
                {
                    pathDistanceGrid[((int)currentGrid.x+neighbourX)*mapHeight+((int)currentGrid.y+neighbourY)]=pathDistanceGrid[(int)currentGrid.x*mapHeight+(int)currentGrid.y]+1;
                    subsequentGrids.addLast(new Vector2f(currentGrid.x+neighbourX, currentGrid.y+neighbourY));
                    //changedArrayElements.add(new Vector2f(currentGrid.x+neighbourX, currentGrid.y+neighbourY));
                }                
            }
        }
        currentGrid.x=destX;
        currentGrid.y=destY;
        while(pathDistanceGrid[(int)currentGrid.x*mapHeight+(int)currentGrid.y]!=1)
        {
            for(int j=0; j<8; j++)
            {
                neighbourX=neighbours[j][0];
                neighbourY=neighbours[j][1];
                if((currentGrid.x+neighbourX)+1>mapWidth || (currentGrid.y+neighbourY)+1>mapHeight || 
                (currentGrid.x+neighbourX)<0 || (currentGrid.y+neighbourY)<0)
                {
                    continue;
                }
                if(pathDistanceGrid[((int)currentGrid.x+neighbourX)*mapHeight+((int)currentGrid.y+neighbourY)] == pathDistanceGrid[(int)currentGrid.x*mapHeight+(int)currentGrid.y]-1)
                {
                    currentGrid.x+=neighbourX;
                    currentGrid.y+=neighbourY;
                    break;
                }
            }
        }
        return new Vector2f((int)currentGrid.x-posX,(int)currentGrid.y-posY);
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
           // System.out.println("___move " + from.get(i) + " to " + to.get(i));
            units[to.get(i)] = units[from.get(i)];
            units[from.get(i)] = null;
        }
    }

}
