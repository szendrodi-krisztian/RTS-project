package battle.entity.group;

import battle.BattleMap;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 *
 * @author Krisz
 */
public class TriangleFormation extends AbstractFormation {

    Quaternion quater;

    public TriangleFormation(BattleMap map) {
        super(map);
        quater = new Quaternion().fromAngleNormalAxis(0, Vector3f.UNIT_Y);
    }

    @Override
    public Vector2f getRelativePosition(int unit_in, Vector2f leaderPosition, float rotationAngle) {
        Vector2f ret;
        do {
            int unit_index;
            if ((true)) {
                unit_index = unit_in + position_offset;
            } else {
                unit_index = unit_in + position_offset_neg;
            }
            quater.fromAngleNormalAxis(rotationAngle * FastMath.DEG_TO_RAD, Vector3f.UNIT_Y);
            float index;
            float index_y;

            int row_num = ((int) FastMath.floor(FastMath.sqrt(unit_index)));
            int count_in_row = row_num * 2 + 1;

            int all_before_lines = 0;
            for (int i = 0; i < row_num; i++) {
                all_before_lines += i * 2 + 1;
            }
            int in_line_index = unit_index - all_before_lines;
            if (rev) {
                index_y = -row_num;
                index = (-(count_in_row / 2) + in_line_index);
            } else {
                index_y = -row_num;
                index = (-(count_in_row / 2) + in_line_index);
            }
            /*
             0*0    0 +0 +0    1    0

             1*1     1 +1 -1   3    1
             2 +0 -1        1.41
             3 -1 -1        1.73

             2*2     4 +2 -2   5    2
             5 +1 -2        2.23
             6 +0 -2        2.44
             7 -1 -2        2.64
             8 -2 -2        2.82

             3*3     9 +3 -3   7
             */
            Vector3f v = new Vector3f(index, 0, index_y);
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
                if (true) {
                    position_offset++;
                } else {
                    position_offset_neg++;
                }
            }
        } while (!map.isTerrainAccessible(ret));
        return ret;
    }

}
