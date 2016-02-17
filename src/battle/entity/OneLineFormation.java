package battle.entity;

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

    public OneLineFormation(BattleMap map) {
        super(map);
        quater = new Quaternion().fromAngleNormalAxis(0, Vector3f.UNIT_Y);
    }

    Quaternion quater;

    @Override
    public Vector2f getRelativePosition(int unit_index, Vector2f leaderPosition, float rotationAngle) {
        quater.fromAngleNormalAxis(rotationAngle, Vector3f.UNIT_Y);
        int index = ((unit_index%2==0)?-1:1)*((unit_index+1)/2);
        Vector3f rot = quater.mult(new Vector3f(index , 0, 0));
        Vector2f ret = leaderPosition.clone().addLocal(rot.x, rot.z);
        ret.x = Math.round(ret.x);
        ret.y = Math.round(ret.y);
        return ret;

    }

}
