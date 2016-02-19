package battle.entity.group;

import battle.BattleMap;
import battle.entity.Unit;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author szend
 */
public final class Group {

    private final List<Unit> units;
    private final BattleMap map;

    private final AbstractFormation formation;

    /**
     * Creates a new empty group.
     *
     * @param map The map instance this group lives on.
     * @param form the formation
     */
    public Group(BattleMap map, AbstractFormation form) {
        this.units = new ArrayList<>();
        this.map = map;
        formation = form;
    }

    /**
     * @return The map on which the group lives.
     */
    public BattleMap getMap() {
        return map;
    }

    /**
     * Joins the given unit to this group. Note: with this in place one unit
     * could belong to more than one group in this case the behavior is
     * undefined.
     *
     * @param u The Unit to join.
     */
    public void join(Unit u) {
        units.add(u);
    }

    /**
     * @return The leader of this group, who the reference for the movement and formation is.
     */
    public Unit getLeader() {
        return units.get(0);
    }

    /**
     * Moves the whole group to a new destination.
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void moveTo(int x, int y) {
        float rot_bef = getLeader().rotationAngle;
        Vector2f leader = new Vector2f(x, y);
        float rotation = (FastMath.RAD_TO_DEG * (new Vector2f(x, y).subtractLocal(getLeader().position()).angleBetween(new Vector2f(0, 1))));
        // negative angles mess up Quaternion constructor...
        rotation = (rotation < 0) ? 360 + rotation : rotation;
        rotation -= (rotation % 45);
        formation.position_offset = 0;
        formation.position_offset_neg = 0;
        if (FastMath.abs(rot_bef - rotation) >= 90) {
            formation.rev = !formation.rev;
        }
        for (int i = 0; i < units.size(); i++) {
            Vector2f v = formation.getRelativePosition(i, leader, rotation);
            units.get(i).moveTo(v);
            units.get(i).rotationAngle = rotation;
        }
    }

}
