package battle.entity;

import battle.BattleMap;
import battle.projectile.Projectile;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * Placeholder class for testing porpouses only.
 * @author Krisz
 */
public class SimpleWeapon implements IWeapon {

    String tex = "weapon";
    int min_dmg = 1;
    int max_dmg = 3;
    int acc = 30;
    int range = 5;
    int cd = 1;
    float sw_in = 0.2f;
    float sw_out = 0.2f;
    boolean ready = true;
    BattleMap map;

    public SimpleWeapon(BattleMap map) {
        this.map = map;
    }

    @Override
    public String getTexture() {
        return tex;
    }

    @Override
    public void setTexture(String tex) {
        this.tex = tex;
    }

    @Override
    public int getMinDamage() {
        return min_dmg;
    }

    @Override
    public void setMinDamage(int dmg) {
        min_dmg = dmg;
    }

    @Override
    public int getMaxDamage() {
        return max_dmg;
    }

    @Override
    public void setMaxDamage(int dmg) {
        max_dmg = dmg;
    }

    @Override
    public int getAccuracy() {
        return acc;
    }

    @Override
    public void setAccuracy(int newacc) {
        acc = newacc;
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public void setRange(int newrange) {
        range = newrange;
    }

    @Override
    public int getCooldown() {
        return cd;
    }

    @Override
    public void setCooldown(int newacc) {
        cd = newacc;
    }

    @Override
    public float getSwitchOut() {
        return sw_out;
    }

    @Override
    public void setSwitchOut(float x) {
        sw_out = x;
    }

    @Override
    public float getSwitchIn() {
        return sw_in;
    }

    @Override
    public void setSwitchIn(float x) {
        sw_in = x;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void setReady(boolean r) {
        ready = r;
    }

    @Override
    public void attack(Unit.Pose pose, float acc, float dmg_mult, Vector2f pos, float rotationAngle) {
        float rad=rotationAngle*FastMath.DEG_TO_RAD;
        Vector2f v = new Vector2f(0,1);
        v.rotateAroundOrigin(rad, true);
        Vector3f pos3=new Vector3f(pos.x, 0, pos.y);
        Vector3f speed=new Vector3f(v.x, 0, v.y);
        map.projectileList.add(new Projectile(speed, pos3, getMinDamage(), getRange(), 1, true, false, map));
    }

}
