package battle.projectile;

import com.jme3.math.Vector3f;

/**
 *
 * @author Auror
 */
public class Projectile {
    private Vector3f speed;
    private Vector3f position;
    private int damage;
    private int range;
    private int aoeRange;
    private boolean canCollide;
    private boolean isVisible;
    private boolean canGravityAffect;

    public Projectile(Vector3f speed, Vector3f position, int damage, int range, int aoeRange, boolean canCollide, boolean canGravityAffect) {
        this.speed = speed;
        this.position = position;
        this.damage = damage;
        this.range = range;
        this.aoeRange = aoeRange;
        this.canCollide = canCollide;
        this.canGravityAffect = canGravityAffect;
    }
    
    public void reInitialise(Vector3f speed, Vector3f position, int damage, int range, int aoeRange, boolean canCollide, boolean canGravityAffect) {
        this.speed = speed;
        this.position = position;
        this.damage = damage;
        this.range = range;
        this.aoeRange = aoeRange;
        this.canCollide = canCollide;
        this.canGravityAffect = canGravityAffect;
    }
    
    public void move()
    {
        
    }   
    
    
}
