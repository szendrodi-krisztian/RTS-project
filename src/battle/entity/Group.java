package battle.entity;

import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author szend
 */
public final class Group {

    List<Unit> units = new ArrayList<>();

    int leaderIndex = 3;

    public Group() {

    }

    public Unit getLeader() {
        return units.get(leaderIndex);
    }

    public void join(Unit u) {
        units.add(u);
    }

    public void moveTo(int x, int y) {
        Vector2f v = Unit.map.pathFinder((int) getLeader().pos.x, (int) getLeader().pos.y, (int) x, (int) y);
        if (v.x + v.y < 0) {
            leaderIndex = 0;
        } else {
            leaderIndex = 3;
        }
        getLeader().moveTo(x, y);
    }

    public void onUnitMovedGrid(Unit u) {
        if (getLeader().equals(u)) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    if ((2 * i + j) == leaderIndex) {
                        continue;
                    }
                    if (leaderIndex == 0) {
                        units.get(2 * i + j).moveTo((int) getLeader().pos.x - i, (int) getLeader().pos.y - j);
                    } else {
                        units.get(2 * i + j).moveTo((int) getLeader().pos.x + i, (int) getLeader().pos.y + j);
                    }
                }
            }
        }
    }

}
