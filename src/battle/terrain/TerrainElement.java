package battle.terrain;

/**
 *
 * @author Krisz
 */
public abstract class TerrainElement {
    
    /**
     * The name of this type of terrain
     * This name must be used as a texture, like: "Textures/terrain/NAME.png"
     * @return 
     */
    public abstract String getName();

    /**
     * Wheter or not an entity can stand on this thing.
     * @return 
     */
    public abstract boolean isAccesible();

    /**
     * How fast the unit moves on this surface.
     * @return A multiplier, so 0.5f means half as fast 2.0f is twice as fast.
     */
    public abstract float getMovementModifier();

    /**
     * This float means how much this terrain can hold projectiles back.
     * A big value means its hard to destroy and shoot through it.
     * @return 
     */
    public abstract float getProjectileResistance();

}
