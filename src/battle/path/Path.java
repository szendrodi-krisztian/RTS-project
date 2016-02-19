package battle.path;

import battle.BattleMap;
import battle.entity.Unit;
import battle.entity.UnitGrid;
import util.MyVector2f;
import util.ObjectPool;
import battle.terrain.Terrain;
import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author szend
 */
public final class Path extends ArrayList<Vector2f> {

    public static final int neighbours[][] = {
        {+0, +1},
        {+1, +0},
        {+0, -1},
        {-1, +0},
        {+1, +1},
        {-1, +1},
        {-1, -1},
        {+1, -1},};

    private int posX, posY, destX, destY;
    
    private final BattleMap map;
    

    private int pathDistanceGrid[];
    private List<MyVector2f> subsequentGrids;

    private static final ObjectPool<MyVector2f> pool = new ObjectPool<>(MyVector2f.class);

    public Path(Vector2f from, Vector2f to, BattleMap map) {
        this((int) from.x, (int) from.y, (int) to.x, (int) to.y, map);
    }

    public Path(int posX, int posY, int destX, int destY, BattleMap map) {
        this.posX = posX;
        this.posY = posY;
        this.destX = destX;
        this.destY = destY;
        this.map = map;
        pathDistanceGrid = new int[map.mapWidth * map.mapHeight];
        subsequentGrids = new ArrayList<>();
        reCalculate(true);
    }

    public void setStart(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void reCalculate(boolean checkUnits) {
        clear();
        for (MyVector2f subsequentGrid : subsequentGrids) {
            pool.destroy(subsequentGrid);
        }
        subsequentGrids.clear();
        if (destX < 0 || destY < 0 || !map.isTerrainAccessible(destX, destY) || (posX == destX && posY == destY)) {
            this.add(new Vector2f(posX, posY));
            return;
        }

        int neighbourX, neighbourY;
        MyVector2f currentGrid = pool.create(posX, posY);
        Arrays.fill(pathDistanceGrid, Integer.MAX_VALUE);
        subsequentGrids.add(pool.create(posX, posY));
        pathDistanceGrid[posX * map.mapHeight + posY] = 0;
        while (pathDistanceGrid[destX * map.mapHeight + destY] == Integer.MAX_VALUE) {
            if (subsequentGrids.isEmpty()) {
                if (checkUnits) {
                    reCalculate(false);
                    return;
                } else {
                    this.add(new Vector2f(posX, posY));
                    return;
                }
            } else {
                pool.destroy(currentGrid);
                currentGrid = subsequentGrids.remove(0);

            }
            for (int i = 0; i < 8; i++) {
                neighbourX = neighbours[i][0];
                neighbourY = neighbours[i][1];
                if ((currentGrid.x + neighbourX) + 1 > map.mapWidth || (currentGrid.y + neighbourY) + 1 > map.mapHeight
                        || (currentGrid.x + neighbourX) < 0 || (currentGrid.y + neighbourY) < 0) {
                    continue;
                }

                int opt1 = ((int) currentGrid.x + neighbourX) * map.mapHeight + ((int) currentGrid.y + neighbourY);
                int opt2 = (int) currentGrid.x * map.mapHeight + (int) currentGrid.y;
                if (map.isTerrainAccessible(currentGrid.x + neighbourX, currentGrid.y + neighbourY)
                        && pathDistanceGrid[opt1] > pathDistanceGrid[opt2] + 1) {

                    pathDistanceGrid[opt1] = pathDistanceGrid[opt2] + 1;
                    subsequentGrids.add(pool.create(currentGrid.x + neighbourX, currentGrid.y + neighbourY));
                        //changedArrayElements.add(new Vector2f(currentGrid.x+neighbourX, currentGrid.y+neighbourY));

                }
            }
        }
        currentGrid.x = destX;
        currentGrid.y = destY;
        this.add(new Vector2f(currentGrid.x, currentGrid.y));
        while (pathDistanceGrid[(int) currentGrid.x * map.mapHeight + (int) currentGrid.y] != 1) {
            for (int j = 0; j < 8; j++) {
                neighbourX = neighbours[j][0];
                neighbourY = neighbours[j][1];
                if ((currentGrid.x + neighbourX) + 1 > map.mapWidth || (currentGrid.y + neighbourY) + 1 > map.mapHeight
                        || (currentGrid.x + neighbourX) < 0 || (currentGrid.y + neighbourY) < 0) {
                    continue;
                }
                if (pathDistanceGrid[((int) currentGrid.x + neighbourX) * map.mapHeight + ((int) currentGrid.y + neighbourY)] == pathDistanceGrid[(int) currentGrid.x * map.mapHeight + (int) currentGrid.y] - 1) {
                    currentGrid.x += neighbourX;
                    currentGrid.y += neighbourY;
                    this.add(new Vector2f(currentGrid.x, currentGrid.y));
                    break;
                }
            }
        }
        for (MyVector2f subsequentGrid : subsequentGrids) {
            pool.destroy(subsequentGrid);
        }
        subsequentGrids.clear();
        //printGrid();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------------------------------------------------\n");
        sb.append("from: (").append(posX).append(", ").append(posY).append(") to (").append(destX).append(", ").append(destY).append(")\n");
        sb.append("path: ").append(super.toString()).append("\n");
        /* for (int i = terrain.width() - 1; i >= 0; i--) {
         for (int j = terrain.height() - 1; j >= 0; j--) {
         sb.append((pathDistanceGrid[j * terrain.height() + i] == Integer.MAX_VALUE) ? "X" : pathDistanceGrid[j * terrain.height() + i]).append(" ");
         }
         sb.append('\n');
         }*/
        sb.append("----------------------------------------------------------------------\n");
        return sb.toString();
    }

    public void reValidate() {
        if(size()==0) return;
        Vector2f next = get(size() - 1);
        List<Unit> list =map.getUnitsAt(next);

        if (list.isEmpty()) {
            return;
        }
        boolean will_move = true;
        for (Unit u : list) {
            if (u.position().equals(u.destination)) {
                will_move = false;
                break;
            }
        }
        if (will_move) {
            add(new Vector2f(posX, posY));
        }else{
            reCalculate(true);
        }
    }

    public Vector2f first() {
        Vector2f relative = remove(size() - 1);
        return relative.subtractLocal(posX, posY);
    }

}
