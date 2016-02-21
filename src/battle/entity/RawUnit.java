package battle.entity;

import battle.entity.group.Group;
import com.jme3.math.Vector2f;
import java.text.MessageFormat;

/**
 *
 * @author szend
 */
public abstract class RawUnit {

    public enum Pose {

        STANDING, CROUCHING, LAYING
    }

    public Vector2f pos;
    public Vector2f dest;
    public float rotationAngle;
    public float finalRotationAngle;

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

    public RawUnit(IVehicle vehicle, IWeapon weapon, Group group, Pose pose, int health) {
        this.dest = new Vector2f(0, 0);
        this.pos = new Vector2f();
        this.group = group;
        this.vehicle = vehicle;
        this.weapon = weapon;
        this.pose = pose;
        this.health = health;
    }

    public final Group getGroup() {
        return group;
    }

    public final void attack(int x, int y) {
        //weapon.attack(pose, accuracy, dmg_mult, pos, x, y);
    }

    public final void attack() {
        weapon.attack(pose, accuracy, dmg_mult, pos, rotationAngle);
    }

    public final Vector2f position() {
        return pos;
    }

    public final Vector2f destination() {
        return dest;
    }

    public final Pose pose() {
        return pose;
    }
    
    public void getHit(int damage){
        health-=damage;
        checkIfDead();
    }
    
    public void checkIfDead(){
        if(health<=0)
        {
            System.out.println("I'm such dead, much wow at: "+pos.toString());
            health=5;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format("RawUnit\n\tcurrently at {0}\n\tmoving to {1}\n\tfacing {2}\n\triding {3}\n\tequipped {4}\n\tin group{5}", pos, dest, rotationAngle, vehicle, weapon, group));
        return sb.toString();
    }

}
