package battle.entity;

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
    public Vector2f pos;
    public Vector2f dest;
    //
    private Path path;

    public RawUnit(IVehicle vehicle, IWeapon weapon, Group group, Pose pose) {
        this.dest = new Vector2f();
        this.pos = new Vector2f();
        this.group = group;
        this.vehicle = vehicle;
        this.weapon = weapon;
        this.pose = pose;
        path = new Path(pos, dest, getGroup().getMap().terrain.raw(), getGroup().getMap().units);
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
        return path.first();
    }

    public final void moveTo(Vector2f dest) {
        this.dest = dest;
        path = new Path(pos, dest, getGroup().getMap().terrain.raw(), getGroup().getMap().units);
    }

    public final void move() {
        path = new Path(pos, dest, getGroup().getMap().terrain.raw(), getGroup().getMap().units);
    }

    public final Pose pose() {
        return pose;
    }

    @Override
    public String toString() {
        return "RawUnit{pos=" + pos + ", dest=" + dest + ", path=" + path + '}';
    }

}
