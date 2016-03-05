package battle.entity;

import com.google.common.collect.ArrayListMultimap;
import com.jme3.math.Vector2f;
import java.util.Iterator;
import java.util.List;

/**
 * Abstracts a flat 2D array of Units.
 *
 * @author szend
 */
public final class UnitGrid {

    //private final ArrayList<Unit> units[];
    private final ArrayListMultimap<Vector2f, Unit> unitMap;
    private final int mapWidth, mapHeight;

    public UnitGrid(int mapWidth, int mapHeight) {
        this.unitMap = ArrayListMultimap.create();
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        //units = new ArrayList[mapWidth * mapHeight];
        //for (int i = 0; i < mapWidth * mapHeight; i++) {
        ///    units[i] = new ArrayList<>(5);
        //}
    }

    /**
     * Place the given unit on the grid.
     *
     * @param x The position.
     * @param y The position.
     * @param u The unit.
     */
    public void place(int x, int y, Unit u) {
        //units[mapHeight * x + y].add(u);
        unitMap.put(new Vector2f(x, y), u);
    }

    // NOTE: This may be wrong, needs code review.
    /**
     * Move all units in this grid.
     *
     * @param tpf
     */
    public void move(float tpf) {
        Vector2f temp = new Vector2f();
        ArrayListMultimap<Vector2f, Unit> tempMap = ArrayListMultimap.create();
        for (Iterator<Unit> it = unitMap.values().iterator(); it.hasNext();) {
            Unit unit = it.next();
            unit.moved = false;

            if (!unit.moved) {
                temp.x = unit.position().x;
                temp.y = unit.position().y;
                unit.move(tpf);
                if (!unit.position().equals(temp)) {
                    it.remove();
                    tempMap.put(temp.clone(), unit);
                }
            }

        }

        for (Unit u : tempMap.values()) {
            unitMap.put(u.position().clone(), u);
        }

        tempMap.clear();

        /* for (int i = 0; i < mapWidth; i++) {
         for (int j = 0; j < mapHeight; j++) {
         List<Unit> list = getUnitsAt(i, j);
         for (int k = 0; k < list.size(); k++) {
         Unit unit = list.get(k);
         if (unit != null && !unit.moved) {
         unit.move(tpf);
         unit.moved = true;
         Vector2f pos_before = unit.position();
         if (!(i == pos_before.x && j == pos_before.y)) {
         moveUnitFromTo(unit, i, j, pos_before, i, j, k);
         }
         }
         }
         }
         }*/
    }

    public void remove(Unit unit) {
        unitMap.remove(unit.position(), unit);
    }

    private void moveUnitFromTo(Unit u, int fromX, int fromY, Vector2f to, int i, int j, int k) {
        moveUnitFromTo(u, fromX, fromY, (int) (to.x), (int) (to.y), i, j, k);
    }

    private void moveUnitFromTo(Unit u, int fromX, int fromY, int toX, int toY, int i, int j, int k) {
        //units[mapHeight * toX + toY].add(units[mapHeight * i + j].remove(k));
    }

    /**
     * @param position
     * @return The unit at (pos) or null if there is no unit there
     */
    public List<Unit> getUnitsAt(Vector2f position) {
        return unitMap.get(position);
    }

    /**
     * @param x
     * @param y
     * @return The unit at (x,y) or null if there is no unit there
     */
    public List<Unit> getUnitsAt(float x, float y) {
        return getUnitsAt((int) x, (int) y);
    }

    /**
     * @param x
     * @param y
     * @return The unit at (x,y) or null if there is no unit there
     */
    public List<Unit> getUnitsAt(int x, int y) {
        return unitMap.get(new Vector2f(x, y));
    }

    public boolean isEmpty(int x, int y) {
        return unitMap.get(new Vector2f(x, y)).isEmpty();
    }

    public boolean isEmpty(float x, float y) {
        return unitMap.get(new Vector2f(x, y)).isEmpty();
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
