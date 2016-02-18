package battle.entity.group;

import battle.entity.group.AbstractFormation;
import battle.BattleMap;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 *
 * @author szend
 */
public class OneLineFormation extends AbstractFormation {

    private final int start_index;
    

    public OneLineFormation(BattleMap map) {
        super(map);
        this.start_index = 0;
        quater = new Quaternion().fromAngleNormalAxis(0, Vector3f.UNIT_Y);
    }

    Quaternion quater;

    @Override
    public Vector2f getRelativePosition(int unit_index, Vector2f leaderPosition, float rotationAngle) {
        quater.fromAngleNormalAxis(rotationAngle * FastMath.DEG_TO_RAD, Vector3f.UNIT_Y);
        float index;
        if (rev) {
            index = ((unit_index % 2 == 0) ? 1 : -1) * ((unit_index + 1) / 2);
        } else {
            index = ((unit_index % 2 == 0) ? -1 : 1) * ((unit_index + 1) / 2);
        }
        Vector3f v = new Vector3f(index, 0, 0);
        if ((rotationAngle / 45) % 2 == 1) {
            v.multLocal(FastMath.sqrt(2));
        }
        Vector3f rot = quater.mult(v);
        //System.out.println("rotated: " + rot);
        Vector2f ret = leaderPosition.clone().addLocal(rot.x, rot.z);
        ret.x = Math.round(ret.x);
        ret.y = Math.round(ret.y);
        return ret;

    }

}
