package battle.entity;

import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Abstracts a flat 2D array of Units.
 *
 * @author szend
 */
public class UnitGrid {

    private final ArrayList<Unit> units[];
    private final int mapWidth, mapHeight;

    public UnitGrid(int mapWidth, int mapHeight) {
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        units = new ArrayList[mapWidth * mapHeight];
        for(int i = 0;i<mapWidth * mapHeight;i++){
            units[i] = new ArrayList<>(2);
        }
    }

    /**
     * Place the given unit on the grid.
     *
     * @param x The position.
     * @param y The position.
     * @param u The unit.
     */
    public void place(int x, int y, Unit u) {
        units[mapHeight * x + y].add(u);
    }

    // NOTE: This may be wrong, needs code review.
    /**
     * Move all units in this grid.
     *
     * @param tpf
     */
    public void move(float tpf) {
        for (ArrayList<Unit> list : units) {
            for (Unit unit : list) {
                if (unit != null) {
                    unit.moved = false;
                }
            }
        }
        /*
         * BE CAREFUL DRAGONS AHEAD!
         * If for any reason two units overlap one of them is DELETED, but the geometry stucks in the scene
         * Theoretically, the pathfinding wont let this happen but if it does, this could be the problem you are looking for.
         */
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                ArrayList<Unit> list = getUnitsAt(i, j);
                for (Iterator<Unit> it = list.iterator(); it.hasNext();) {
                    Unit unit = it.next();
                    if (unit != null && !unit.moved) {
                        unit.move(tpf);
                        unit.moved = true;
                        Vector2f pos_before = unit.position();
                        if (!(i == pos_before.x && j == pos_before.y)) {
                            moveUnitFromTo(unit, i, j, pos_before, it);
                            //System.out.println(this);
                        }
                    }
                }
            }
        }

    }

    private void moveUnitFromTo(Unit u, int fromX, int fromY, Vector2f to, Iterator<Unit> it) {
        moveUnitFromTo(u, fromX, fromY, (int) (to.x), (int) (to.y), it);
    }

    private void moveUnitFromTo(Unit u, int fromX, int fromY, int toX, int toY, Iterator<Unit> it) {
        units[mapHeight * toX + toY].add(u);
        it.remove();
    }

    /**
     * @param position
     * @return The unit at (pos) or null if there is no unit there
     */
    public ArrayList<Unit> getUnitsAt(Vector2f position) {
        return getUnitsAt(position.x, position.y);
    }

    /**
     * @param x
     * @param y
     * @return The unit at (x,y) or null if there is no unit there
     */
    public ArrayList<Unit> getUnitsAt(float x, float y) {
        return getUnitsAt((int) x, (int) y);
    }

    /**
     * @param x
     * @param y
     * @return The unit at (x,y) or null if there is no unit there
     */
    public ArrayList<Unit> getUnitsAt(int x, int y) {
        return getUnitsAt(mapHeight * x + y);
    }

    /**
     * @return Unit at given index or null if there is no unit there.
     */
    private ArrayList<Unit> getUnitsAt(int index) {
        return units[index];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------------\n");
        sb.append("UnitGrid:\n");
        for (int i = mapWidth - 1; i >= 0; i--) {
            for (int j = mapHeight - 1; j >= 0; j--) {
                //sb.append((getUnitAt(j, i) == null) ? '0' : 'X');
            }
            sb.append('\n');
        }
        sb.append("--------------------------------------------");
        return sb.toString();
    }

}
