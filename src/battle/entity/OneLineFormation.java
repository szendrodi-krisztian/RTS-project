package battle.entity;

import battle.BattleMap;
import com.jme3.math.Vector2f;

/**
 *
 * @author szend
 */
public class OneLineFormation extends AbstractFormation{

    public OneLineFormation(BattleMap map) {
        super(map);
    }

    @Override
    public Vector2f getRelativePosition(int unit_index, Vector2f leaderPosition) {
        // TODO: write placement code here.
        // TODO: call this when unit needs to be traited as reservist
        return super.getReservistPosition(unit_index, leaderPosition);
    }
    
}
