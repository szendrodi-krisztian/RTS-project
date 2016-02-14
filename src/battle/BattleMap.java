package battle;

import battle.entity.Group;
import battle.entity.SimpleUnit;
import battle.entity.Unit;
import battle.terrain.MyVector2f;
import battle.terrain.SimplexNoise;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import battle.terrain.TerrainElement;
import battle.terrain.TerrainElementManager;
import battle.terrain.VectorPool;
import battle.terrain.render.TerrainDecorationMesh;
import battle.terrain.render.TerrainGridMesh;
import com.jme3.scene.Mesh;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Krisz
 */
public class BattleMap {

    public TerrainElement grid[];

    public Unit units[];

    public List<Group> groups = new ArrayList<>();

    public int mapWidth, mapHeight;

    public int pathDistanceGrid[];
    public List<MyVector2f> subsequentGrids;
    public List<Vector2f> changedArrayElements;
    public List<Vector2f> fullPath = new ArrayList<>();

    public BattleMap(int mapWidth, int mapHeight, Node rootNode, AssetManager assets) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        Unit.init(this, assets, rootNode);
        units = new Unit[mapWidth * mapHeight];
        Group g1 = new Group();

        SimpleUnit u = new SimpleUnit(2, 2, g1);
        SimpleUnit u2 = new SimpleUnit(3, 2, g1);
        SimpleUnit u3 = new SimpleUnit(1, 2, g1);

        units[mapHeight * 2 + 2] = u;
        units[mapHeight * 3 + 2] = u2;
        units[mapHeight * 1 + 2] = u3;

        pathDistanceGrid = new int[mapWidth * mapHeight];
        subsequentGrids = new ArrayList<>();

        grid = new TerrainElement[mapWidth * mapHeight];
        SimplexNoise noise = new SimplexNoise(128, 0.3f, 0xCAFFEE);
        SimplexNoise treenoise = new SimplexNoise(1000, 1.2f, 0xCAFFEE);

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                float n = noise.getNoise(0.3f * i, 0.3f * j);
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
    public static final int neighbours[][] = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

    public final List<Vector2f> getPath(int posX, int posY, int destX, int destY) {

        fullPath.clear();
        if (!grid[destX * mapHeight + destY].isAccesible() || (posX == destX && posY == destY)) {
            fullPath.add(new Vector2f(posX, posY));
            return fullPath;
        }

        int neighbourX, neighbourY;
        MyVector2f currentGrid = VectorPool.getInstance().createVector(posX, posY);
        Arrays.fill(pathDistanceGrid, Integer.MAX_VALUE);
        subsequentGrids.add(VectorPool.getInstance().createVector(posX, posY));
        pathDistanceGrid[posX * mapHeight + posY] = 0;
        while (pathDistanceGrid[destX * mapHeight + destY] == Integer.MAX_VALUE) {
            if (subsequentGrids.isEmpty()) {
                fullPath.add(new Vector2f(posX, posY));
                return fullPath;
            } else {
                VectorPool.getInstance().destroyVector(currentGrid);
                currentGrid = subsequentGrids.remove(0);

            }
            for (int i = 0; i < 8; i++) {
                neighbourX = neighbours[i][0];
                neighbourY = neighbours[i][1];
                if ((currentGrid.x + neighbourX) + 1 > mapWidth || (currentGrid.y + neighbourY) + 1 > mapHeight
                        || (currentGrid.x + neighbourX) < 0 || (currentGrid.y + neighbourY) < 0) {
                    continue;
                }
                int opt1 = ((int) currentGrid.x + neighbourX) * mapHeight + ((int) currentGrid.y + neighbourY);
                int opt2 = (int) currentGrid.x * mapHeight + (int) currentGrid.y;
                if (grid[opt1].isAccesible()
                        && pathDistanceGrid[opt1] > pathDistanceGrid[opt2] + 1
                        && units[opt1] == null) {
                    pathDistanceGrid[opt1] = pathDistanceGrid[opt2] + 1;
                    subsequentGrids.add(VectorPool.getInstance().createVector(currentGrid.x + neighbourX, currentGrid.y + neighbourY));
                    //changedArrayElements.add(new Vector2f(currentGrid.x+neighbourX, currentGrid.y+neighbourY));
                }
            }
        }
        currentGrid.x = destX;
        currentGrid.y = destY;
        fullPath.add(new Vector2f(currentGrid.x, currentGrid.y));
        while (pathDistanceGrid[(int) currentGrid.x * mapHeight + (int) currentGrid.y] != 1) {
            for (int j = 0; j < 8; j++) {
                neighbourX = neighbours[j][0];
                neighbourY = neighbours[j][1];
                if ((currentGrid.x + neighbourX) + 1 > mapWidth || (currentGrid.y + neighbourY) + 1 > mapHeight
                        || (currentGrid.x + neighbourX) < 0 || (currentGrid.y + neighbourY) < 0) {
                    continue;
                }
                if (pathDistanceGrid[((int) currentGrid.x + neighbourX) * mapHeight + ((int) currentGrid.y + neighbourY)] == pathDistanceGrid[(int) currentGrid.x * mapHeight + (int) currentGrid.y] - 1) {
                    currentGrid.x += neighbourX;
                    currentGrid.y += neighbourY;
                    fullPath.add(new Vector2f(currentGrid.x, currentGrid.y));
                    break;
                }
            }
        }
        for (int i = 0; i < subsequentGrids.size(); i++) {
            VectorPool.getInstance().destroyVector(subsequentGrids.get(i));
            subsequentGrids.set(i, null);
        }
        subsequentGrids.clear();
        return fullPath;
    }

    public Vector2f pathFinder(int posX, int posY, int destX, int destY) {
        List<Vector2f> path = getPath(posX, posY, destX, destY);
        Vector2f relative = path.get(path.size() - 1);
        int index = (int) (mapHeight*relative.x+relative.y);
        if(units[index]!=null){
            return Vector2f.ZERO;
        }
        relative.subtractLocal(posX, posY);
        return relative;
    }

    public void tick(float tpf) {
        for (Unit unit : units) {
            if (unit != null) {
                unit.moved = false;
            }
        }
        /*
         * BE CAREFUL DRAGONS AHEAD!
         * If for any reason two units overlap one of them is DELETED, but the geometry stucks in the scene
         * Theoretically, the pathfinding wont let this happen but if it does, this could be the problem you are looking for.
         */
        for (int i = 0; i < units.length; i++) {
            if (units[i] != null && !units[i].moved) {
                int r = units[i].move(tpf);
                if (r != 0) {
                    units[i + r] = units[i];
                    units[i] = null;
                }
                units[i + r].moved = true;
            }
        }
    }

}
