package battle.entity.group;

import battle.BattleMap;
import battle.entity.AbstractFormation;
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

    public Group(BattleMap map, Class<? extends AbstractFormation> form) throws Exception {
        this.units = new ArrayList<>();
        this.map = map;
        formation = (AbstractFormation) form.getConstructors()[0].newInstance(map);
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
        // negative angles mess up Quaternion constructor...
        rotation = (rotation < 0) ? 360 + rotation : rotation;
        rotation -= (rotation % 45);
        for (int i = 0; i < units.size(); i++) {
            Vector2f v = formation.getRelativePosition(i, leader, rotation);
            units.get(i).moveTo(v);
        }
    }

    public void onUnitMovedGrid(Unit u) {

    }

}
