package battle.path;

import battle.entity.UnitGrid;
import battle.terrain.MyVector2f;
import battle.terrain.Terrain;
import battle.terrain.VectorPool;
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

    private final int posX, posY, destX, destY;
    private final Terrain terrain;
    private final UnitGrid units;

    private int pathDistanceGrid[];
    private List<MyVector2f> subsequentGrids;

    public Path(Vector2f from, Vector2f to, Terrain terrain, UnitGrid units) {
        this((int) from.x, (int) from.y, (int) to.x, (int) to.y, terrain, units);
    }

    public Path(int posX, int posY, int destX, int destY, Terrain terrain, UnitGrid units) {
        this.posX = posX;
        this.posY = posY;
        this.destX = destX;
        this.destY = destY;
        this.terrain = terrain;
        this.units = units;
        pathDistanceGrid = new int[terrain.width() * terrain.height()];
        subsequentGrids = new ArrayList<>();
        reCalculate();
    }

    public void reCalculate() {
        this.clear();

        if (!terrain.isAccessible(destX, destY) || (posX == destX && posY == destY)) {
            this.add(new Vector2f(posX, posY));
            return;
        }

        int neighbourX, neighbourY;
        MyVector2f currentGrid = VectorPool.getInstance().createVector(posX, posY);
        Arrays.fill(pathDistanceGrid, Integer.MAX_VALUE);
        subsequentGrids.add(VectorPool.getInstance().createVector(posX, posY));
        pathDistanceGrid[posX * terrain.height() + posY] = 0;
        while (pathDistanceGrid[destX * terrain.height() + destY] == Integer.MAX_VALUE) {
            if (subsequentGrids.isEmpty()) {
                this.add(new Vector2f(posX, posY));
                return;
            } else {
                VectorPool.getInstance().destroyVector(currentGrid);
                currentGrid = subsequentGrids.remove(0);

            }
            for (int i = 0; i < 8; i++) {
                neighbourX = neighbours[i][0];
                neighbourY = neighbours[i][1];
                if ((currentGrid.x + neighbourX) + 1 > terrain.width() || (currentGrid.y + neighbourY) + 1 > terrain.height()
                        || (currentGrid.x + neighbourX) < 0 || (currentGrid.y + neighbourY) < 0) {
                    continue;
                }
                int opt1 = ((int) currentGrid.x + neighbourX) * terrain.height() + ((int) currentGrid.y + neighbourY);
                int opt2 = (int) currentGrid.x * terrain.height() + (int) currentGrid.y;
                if (terrain.grid[opt1].isAccesible()
                        && pathDistanceGrid[opt1] > pathDistanceGrid[opt2] + 1
                        && units.getUnitAt(((int) currentGrid.x + neighbourX), ((int) currentGrid.y + neighbourY)) == null) {
                    pathDistanceGrid[opt1] = pathDistanceGrid[opt2] + 1;
                    subsequentGrids.add(VectorPool.getInstance().createVector(currentGrid.x + neighbourX, currentGrid.y + neighbourY));
                    //changedArrayElements.add(new Vector2f(currentGrid.x+neighbourX, currentGrid.y+neighbourY));
                }
            }
        }
        currentGrid.x = destX;
        currentGrid.y = destY;
        this.add(new Vector2f(currentGrid.x, currentGrid.y));
        while (pathDistanceGrid[(int) currentGrid.x * terrain.height() + (int) currentGrid.y] != 1) {
            for (int j = 0; j < 8; j++) {
                neighbourX = neighbours[j][0];
                neighbourY = neighbours[j][1];
                if ((currentGrid.x + neighbourX) + 1 > terrain.width() || (currentGrid.y + neighbourY) + 1 > terrain.height()
                        || (currentGrid.x + neighbourX) < 0 || (currentGrid.y + neighbourY) < 0) {
                    continue;
                }
                if (pathDistanceGrid[((int) currentGrid.x + neighbourX) * terrain.height() + ((int) currentGrid.y + neighbourY)] == pathDistanceGrid[(int) currentGrid.x * terrain.height() + (int) currentGrid.y] - 1) {
                    currentGrid.x += neighbourX;
                    currentGrid.y += neighbourY;
                    this.add(new Vector2f(currentGrid.x, currentGrid.y));
                    break;
                }
            }
        }
        for (int i = 0; i < subsequentGrids.size(); i++) {
            VectorPool.getInstance().destroyVector(subsequentGrids.get(i));
            subsequentGrids.set(i, null);
        }
        subsequentGrids.clear();
    }

    public Vector2f first() {
        Vector2f relative = get(size()-1).clone();
        if (units.getUnitAt(relative) != null) {
            return Vector2f.ZERO;
        }
        System.out.println("rel: "+relative+" posx: "+posX+" posy: "+posY);
        relative.subtractLocal(posX, posY);
        return relative;
    }

}