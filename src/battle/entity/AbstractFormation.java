package battle.entity;

import battle.BattleMap;
import com.jme3.math.Vector2f;

/**
 *
 * @author szend
 */
public abstract class AbstractFormation {
    
    private final BattleMap map;

    public AbstractFormation(BattleMap map) {
        this.map = map;
    }
   
    
    public abstract Vector2f getRelativePosition(int unit_index, Vector2f leaderPosition);
   
    public Vector2f getReservistPosition(int unit_index, Vector2f leaderPosition){
        // TODO: write reservist placement code here.
        return Vector2f.ZERO;
    }
    
}
