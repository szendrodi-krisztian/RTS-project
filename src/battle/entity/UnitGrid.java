package battle.entity;

import com.jme3.math.Vector2f;

/**
 * Abstracts a flat 2D array of Units.
 *
 * @author szend
 */
public class UnitGrid {

    private final Unit units[];
    private final int mapWidth, mapHeight;

    public UnitGrid(int mapWidth, int mapHeight) {
        this.mapHeight = mapHeight;
        this.mapWidth = mapWidth;
        units = new Unit[mapWidth * mapHeight];
    }

    /**
     * Place the given unit on the grid.
     *
     * @param x The position.
     * @param y The position.
     * @param u The unit.
     */
    public void place(int x, int y, Unit u) {
        units[mapHeight * x + y] = u;
    }

    // NOTE: This may be wrong, needs code review.
    /**
     * Move all units in this grid.
     *
     * @param tpf
     */
    public void move(float tpf) {
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
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                Unit unit = getUnitAt(i, j);
                if (unit != null && !unit.moved) {
                    unit.move(tpf);
                    unit.moved = true;
                    Vector2f pos_before = unit.position();
                    if (!(i == pos_before.x && j == pos_before.y)) {
                        moveUnitFromTo(i, j, pos_before);
                        System.out.println(this);
                    }
                }
            }
        }

    }

    private void moveUnitFromTo(int fromX, int fromY, Vector2f to) {
        moveUnitFromTo(fromX, fromY, (int) (to.x), (int) (to.y));
    }

    private void moveUnitFromTo(int fromX, int fromY, int toX, int toY) {
        units[mapHeight * toX + toY] = units[mapHeight * fromX + fromY];
        units[mapHeight * fromX + fromY] = null;
    }

    /**
     * @param position
     * @return The unit at (pos) or null if there is no unit there
     */
    public Unit getUnitAt(Vector2f position) {
        return getUnitAt(position.x, position.y);
    }

    /**
     * @param x
     * @param y
     * @return The unit at (x,y) or null if there is no unit there
     */
    public Unit getUnitAt(float x, float y) {
        return getUnitAt((int) x, (int) y);
    }

    /**
     * @param x
     * @param y
     * @return The unit at (x,y) or null if there is no unit there
     */
    public Unit getUnitAt(int x, int y) {
        return getUnitAt(mapHeight * x + y);
    }

    /**
     * @return Unit at given index or null if there is no unit there.
     */
    private Unit getUnitAt(int index) {
        return units[index];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------------------------------------------\n");
        sb.append("UnitGrid:\n");
        for(int i = mapWidth-1;i>=0;i--){
            for(int j = mapHeight-1;j>=0;j--){
                sb.append((getUnitAt(j, i)==null)?'0':'X');
            }
            sb.append('\n');
        }
        sb.append("--------------------------------------------");
        return sb.toString();
    }

}
