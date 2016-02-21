package battle.entity.group;

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
        this.position_offset = 0;
        quater = new Quaternion().fromAngleNormalAxis(0, Vector3f.UNIT_Y);
    }

    Quaternion quater;

    @Override
    public Vector2f getRelativePosition(int unit_in, Vector2f leaderPosition, float rotationAngle) {
        Vector2f ret;
        do {
            int unit_index;
            if ((unit_in % 2 == 0)) {
                unit_index = unit_in + position_offset;
            } else {
                unit_index = unit_in + position_offset_neg;
            }
            quater.fromAngleNormalAxis(rotationAngle * FastMath.DEG_TO_RAD, Vector3f.UNIT_Y);
            float index;
            if (rev) {
                index = ((unit_in % 2 == 0) ? 1 : -1) * ((unit_index + 1) / 2);
            } else {
                index = ((unit_in % 2 == 0) ? -1 : 1) * ((unit_index + 1) / 2);
            }
            Vector3f v = new Vector3f(index, 0, 0);
            if ((rotationAngle / 45) % 2 == 1) {
                v.multLocal(FastMath.sqrt(2));
            }
            Vector3f rot = quater.mult(v);
            //System.out.println("rotated: " + rot);
            ret = leaderPosition.clone().addLocal(rot.x, rot.z);
            ret.x = Math.round(ret.x);
            ret.y = Math.round(ret.y);
            if (ret.x < 0 || ret.y < 0 || ret.x > map.mapWidth - 1 || ret.y > map.mapHeight - 1) {
                return super.getReservistPosition(unit_in, leaderPosition);
            }
            if (!map.isTerrainAccessible(ret)) {
                if ((unit_in % 2 == 0)) {
                    position_offset++;
                } else {
                    position_offset_neg++;
                }
            }
        } while (!map.isTerrainAccessible(ret));
        return ret;

    }

}
