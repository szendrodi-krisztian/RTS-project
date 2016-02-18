package battle.entity.group;

import battle.BattleMap;
import com.jme3.math.Vector2f;

/**
 *
 * @author szend
 */
public abstract class AbstractFormation {
    
    protected final BattleMap map;
    public boolean rev = false;
    public int position_offset;
    public int position_offset_neg;

    public AbstractFormation(BattleMap map) {
        this.map = map;
    }
   
    public abstract Vector2f getRelativePosition(int unit_index, Vector2f leaderPosition, float rotation);
   
    public Vector2f getReservistPosition(int unit_index, Vector2f leaderPosition){
        // TODO: write reservist placement code here.
        return leaderPosition;
    }
    
}
