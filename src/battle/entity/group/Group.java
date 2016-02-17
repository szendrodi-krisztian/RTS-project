package battle.entity.group;

import battle.BattleMap;
import battle.entity.AbstractFormation;
import battle.entity.OneLineFormation;
import battle.entity.Unit;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author szend
 */
public final class Group {

    private final List<Unit> units;
    private final BattleMap map;

    private final AbstractFormation formation;

    public Group(BattleMap map) {
        this.units = new ArrayList<>();
        this.map = map;
        formation = new OneLineFormation(map);
    }

    public BattleMap getMap() {
        return map;
    }

    public void join(Unit u) {
        units.add(u);
    }

    public Unit getLeader() {
        return units.get(0);
    }

    public void moveTo(int x, int y) {
        Vector2f leader = new Vector2f(x, y);
        float rotation = (FastMath.RAD_TO_DEG * (new Vector2f(x, y).subtractLocal(getLeader().position()).angleBetween(new Vector2f(0, 1))));
        System.out.println(rotation);
        rotation = FastMath.ceil(rotation);
        rotation -= rotation % 45;
        rotation = FastMath.DEG_TO_RAD * rotation;
        for (int i = 0; i < units.size(); i++) {
            Vector2f v = formation.getRelativePosition(i, leader, rotation);
           // System.out.println("vec: " + v + " rot: " + rotation);
            units.get(i).moveTo(v);
        }
    }

    public void onUnitMovedGrid(Unit u) {

    }

}
