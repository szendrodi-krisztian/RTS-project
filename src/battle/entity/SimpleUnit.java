package battle.entity;

import battle.entity.group.Group;
import com.jme3.math.Vector2f;

/**
 * Placeholder class for testing porpouses only.
 *
 * @author Krisz
 */
public class SimpleUnit extends Unit {
    
    public SimpleUnit(int x, int y, Group g) {
        super(new Foot(), new SimpleWeapon(g.getMap()), g);
        pos.x = x;
        pos.y = y;
        moveTo(new Vector2f(x, y));
    }
    
    @Override
    public String getTexture() {
        return "unit";
    }
    
}
