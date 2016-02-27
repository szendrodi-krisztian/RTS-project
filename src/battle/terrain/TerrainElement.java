package battle.terrain;

/**
 *
 * @author Krisz
 */
public abstract class TerrainElement {

    protected boolean accessible;
    protected float movement;
    protected float proj_resis;
    protected String name;
    protected int texture_width;
    protected int texture_heigth;
    protected boolean has_alpha;
    protected char ascii;
    protected String under;

    public char getAscii() {
        return ascii;
    }

    public String getUnder() {
        return under;
    }

    /**
     * @return true when Alpha map exists.
     */
    public boolean isAlpha() {
        return has_alpha;
    }

    public void set_alpha(boolean has_alpha) {
        this.has_alpha = has_alpha;
    }

    /**
     * @return The width of the texture.
     */
    public int getTexture_width() {
        return texture_width;
    }

    /**
     * @param texture_width The new texture size.
     */
    public void setTexture_width(int texture_width) {
        this.texture_width = texture_width;
    }

    /**
     * @return The heigth of the texture.
     */
    public int getTexture_heigth() {
        return texture_heigth;
    }

    /**
     * @param texture_heigth The new texture size.
     */
    public void setTexture_heigth(int texture_heigth) {
        this.texture_heigth = texture_heigth;
    }

    /**
     * The name of this type of terrain This name must be used as a texture,
     * like: "Textures/terrain/NAME.png"
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Wheter or not an entity can stand on this thing.
     *
     * @return
     */
    public boolean isAccesible() {
        return accessible;
    }

    /**
     * How fast the unit moves on this surface.
     *
     * @return A multiplier, so 0.5f means half as fast 2.0f is twice as fast.
     */
    public float getMovementModifier() {
        return movement;
    }

    /**
     * This float means how much this terrain can hold projectiles back. A big
     * value means its hard to destroy and shoot through it.
     *
     * @return
     */
    public float getProjectileResistance() {
        return proj_resis;
    }

}
