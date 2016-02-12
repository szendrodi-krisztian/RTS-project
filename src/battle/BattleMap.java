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
import java.util.Collections;
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
    public List<Vector2f> fullPath=new ArrayList<>();
            
            
    public BattleMap(int mapWidth, int mapHeight, Node rootNode, AssetManager assets) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        Unit.init(this, assets, rootNode);
        units = new Unit[mapWidth * mapHeight];
        SimpleUnit u = new SimpleUnit(2, 2);
        SimpleUnit u2 = new SimpleUnit(2, 3);

        pathDistanceGrid = new int[mapWidth * mapHeight];
        subsequentGrids = new LinkedList<>();

        units[mapHeight * 2 + 2] = u;
        units[mapHeight * 2 + 3] = u2;

        grid = new TerrainElement[mapWidth * mapHeight];
        SimplexNoise noise = new SimplexNoise(128, 0.3f, 0xCAFFEE);
        SimplexNoise treenoise = new SimplexNoise(1000, 1.2f, 0xCAFFEE);

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
       /// u.moveTo(10, 10);
        //u2.moveTo(10, 20);
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

    public List<Vector2f> getPath(int posX, int posY, int destX, int destY) {
        fullPath.clear();
        if(!grid[destX*mapHeight+destY].isAccesible() || (posX==destX && posY==destY))
        {
            fullPath.add(new Vector2f(posX,posY));
            return fullPath;
        }   
        subsequentGrids.clear();
        int neighbourX=0, neighbourY=0;
        int neighbours[][] = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {-1, 1}, {-1, -1}, {1, -1}};
        Vector2f currentGrid = new Vector2f(posX, posY);
        Arrays.fill(pathDistanceGrid, Integer.MAX_VALUE);
        subsequentGrids.addLast(new Vector2f(posX,posY));
        pathDistanceGrid[posX*mapHeight+posY]=0; 
        while(pathDistanceGrid[destX*mapHeight+destY]==Integer.MAX_VALUE)
        {
            if(subsequentGrids.isEmpty())
            {
                fullPath.add(new Vector2f(posX,posY));
                return fullPath;
            }   
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
                if (grid[((int) currentGrid.x + neighbourX) * mapHeight + ((int) currentGrid.y + neighbourY)].isAccesible()
                        && pathDistanceGrid[((int) currentGrid.x + neighbourX) * mapHeight + ((int) currentGrid.y + neighbourY)] > pathDistanceGrid[(int) currentGrid.x * mapHeight + (int) currentGrid.y] + 1
                        && units[((int) currentGrid.x + neighbourX) * mapHeight + ((int) currentGrid.y + neighbourY)] == null) {
                    pathDistanceGrid[((int) currentGrid.x + neighbourX) * mapHeight + ((int) currentGrid.y + neighbourY)] = pathDistanceGrid[(int) currentGrid.x * mapHeight + (int) currentGrid.y] + 1;
                    subsequentGrids.addLast(new Vector2f(currentGrid.x + neighbourX, currentGrid.y + neighbourY));
                    //changedArrayElements.add(new Vector2f(currentGrid.x+neighbourX, currentGrid.y+neighbourY));
                }
            }
        }
        currentGrid.x=destX;
        currentGrid.y=destY;
        fullPath.add(currentGrid.clone());
        List<Vector2f> options= new ArrayList<>();
        while(pathDistanceGrid[(int)currentGrid.x*mapHeight+(int)currentGrid.y]!=1)
        {
            options.clear();
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
                    options.add(currentGrid.add(new Vector2f(neighbourX,neighbourY)));
                }
            }
            if(options.size()==1)
            {
                currentGrid=options.get(0);
                fullPath.add(currentGrid.clone());
            }
            else
            if(options.size()>1)
            {
                boolean yolo=true;
                List<Float> distance = new ArrayList<>(options.size());
                double nominator = Math.hypot(destX-posX, destY-posY);
                for(Vector2f s:options)
                {
                    distance.add(FastMath.abs(((destY-posY)*s.x)-((destX-posX)*s.y)+(destX*posY)-(destY*posX))/(float)nominator);
                }
                int minIndex=0;
                float min=Integer.MAX_VALUE;
                for(int i=0; i<options.size(); i++)
                {
                    //If it gets further away
                    if(options.get(i).subtract(new Vector2f(destX,destY)).lengthSquared()>currentGrid.subtract(new Vector2f(destX,destY)).lengthSquared())
                        yolo=false;
                }
                if(!yolo)
                {
                    for(int i=0; i<distance.size(); i++)
                    {
                        if(min>distance.get(i))
                        {
                            min=distance.get(i);
                            minIndex=i;
                        }
                    }
                }
                else
                {
                    min=Integer.MIN_VALUE;
                    for(int i=0; i<distance.size(); i++)
                    {
                        if(min<distance.get(i))
                        {
                            min=distance.get(i);
                            minIndex=i;
                        }
                    }
                }
                currentGrid=options.get(minIndex);
                fullPath.add(options.get(minIndex));
            }
        }
        return fullPath;
    }
    
    public Vector2f pathFinder(int posX, int posY, int destX, int destY)
    {
        List<Vector2f> path = getPath(posX,posY,destX,destY);
        Vector2f relative = path.get(path.size()-1);
        relative.subtractLocal(posX, posY);
        return relative;
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
