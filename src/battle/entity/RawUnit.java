package battle.entity;

import battle.BattleMap;
import battle.path.Path;
import com.jme3.math.Vector2f;

/**
 *
 * @author szend
 */
public abstract class RawUnit {

    public enum Pose {

        STANDING, CROUCHING, LAYING
    }

    protected final Pose pose;
    protected final Group group;
    protected final IVehicle vehicle;
    protected final IWeapon weapon;

    // Health points
    protected int health;
    // Angle in rads
    protected float accuracy;
    // [0-100]
    protected int stamina;
    //
    protected float discipline;
    //
    protected float morale;
    //
    protected float dmg_mult;
    //
    public Vector2f pos = new Vector2f();
    //
    public Vector2f dest = new Vector2f();
    //
    private Path path;

    public RawUnit(IVehicle vehicle, IWeapon weapon, Group group, Pose pose) {
        this.group = group;
        this.vehicle = vehicle;
        this.weapon = weapon;
        this.pose = pose;
    }

    public final Group getGroup() {
        return group;
    }

    public final void attack(int x, int y) {
        weapon.attack(pose, accuracy, dmg_mult, pos, x, y);
    }

    public final Vector2f position() {
        return pos;
    }

    public final Vector2f destination() {
        return dest;
    }

    public final Vector2f nextStepDirection() {
        if (path != null) {
            return path.first();
        } else {
            System.out.println("nulllll");
            return Vector2f.ZERO.clone();
        }
    }

    public final void moveTo(Vector2f dest, BattleMap map) {
        this.dest = dest;
        path = new Path(pos, dest, map.terrain.raw(), map.units);
    }

    public void move() {

    }

}
