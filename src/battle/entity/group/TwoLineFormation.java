/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class TwoLineFormation extends AbstractFormation {

    Quaternion quater;

    public TwoLineFormation(BattleMap map) {
        super(map);
        quater = new Quaternion().fromAngleNormalAxis(0, Vector3f.UNIT_Y);
    }

    @Override
    public Vector2f getRelativePosition(int unit_index, Vector2f leaderPosition, float rotation) {
        quater.fromAngleNormalAxis(rotation * FastMath.DEG_TO_RAD, Vector3f.UNIT_Y);
        //float index = ((unit_index % 2 == 0) ? -1 : 1) * ((unit_index + 1) / 2);

        float index;
        float index_y;
        if (rev) {
            index = ((unit_index % 4 < 2) ? 1 : -1) * ((unit_index + 2) / 4);
            index_y = ((unit_index % 2 == 0) ? -1 : 1) * (unit_index % 2);
        } else {
            index = ((unit_index % 4 < 2) ? -1 : 1) * ((unit_index + 2) / 4);
            index_y = ((unit_index % 2 == 0) ? 1 : -1) * (unit_index % 2);
        }
        /*
         0  -0 +0
         1  -0 -1
         2  +1 +0
         3  +1 -1
         4  -1 +0
         5  -1 -1
         6  +2 +0
         7  +2 -1
         8  -2 +0
         9  -2 -1
         10 +3 +0
         */
        Vector3f v = new Vector3f(index, 0, index_y);
        if ((rotation / 45) % 2 == 1) {
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
