package battle.entity;

import battle.BattleMap;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

/**
 *
 * @author Krisz
 */
public abstract class Unit {

    public enum Pose {

        STANDING, CROUCHING, LAYING
    }

    public enum Rotation {

        NORTH, SOUTH, EAST, WEST
    }

    private Geometry geometry;

    private static BattleMap map;

    private final IVehicle vehicle;
    private final IWeapon weapon;

    private Pose pose;

    private Rotation rotation;

    public static final float N_STEP = 5;

    // The position on the grid
    protected int posX, posY;
    // positional value [0-1[
    private float fractalX, fractalY;
    private float destX, destY;
    private float nextX, nextY;
    // Health points
    private int health;
    // Angle in rads
    private float accuracy;
    // [0-100]
    private int stamina;
    //
    private float discipline;
    //
    private float morale;
    //
    private float dmg_mult;
    //
    private static AssetManager assets;
    //
    private static Node node;

    public static void init(BattleMap map, AssetManager assets, Node root) {
        Unit.node = root;
        Unit.assets = assets;
        Unit.map = map;
    }

    public Unit(IVehicle vehicle, IWeapon weapon) {
        this.vehicle = vehicle;
        this.weapon = weapon;
        Material m = new Material(assets, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture t = assets.loadTexture(new TextureKey("Textures/units/unit.png", false));
        
        
        m.setTexture("ColorMap", t);
        Quad q = new Quad(1, 1);
        
        geometry = new Geometry("unit", q);
        geometry.rotate(new Quaternion(new float[]{-FastMath.HALF_PI,0,0}));
        geometry.setMaterial(m);
        node.attachChild(geometry);
    }

    /**
     * Moves this unit for animation.
     *
     * @param tpf Time per frame.
     */
    public final int move(float tpf) {
        int ret = 0;
        fractalX += (tpf * nextX * vehicle.getMovementSpeed()) / N_STEP;
        fractalY += (tpf * nextY * vehicle.getMovementSpeed()) / N_STEP;
        if (FastMath.abs(fractalX) >= 1) {
            posX += FastMath.sign(fractalX);
            ret = (int) FastMath.sign(fractalX);
            fractalX = 0;
            Vector2f d = map.dijkstra(posX, posY, destX, destY); // map.disjk : gives next position on the path
            nextX = d.x;
            nextY = d.y;
            return ret;
        }
        if (FastMath.abs(fractalY) >= 1) {
            ret = (int) FastMath.sign(fractalX)*map.n;
            posY += FastMath.sign(fractalY);
            fractalY = 0;
            Vector2f d = map.dijkstra(posX, posY, destX, destY);
            nextX = d.x;
            nextY = d.y;
        }
        geometry.setLocalTranslation(posX+fractalX, 0.0001f, posY+fractalY);
        return ret;
    }

    /**
     * Sets movement destination.
     *
     * @param x
     * @param y
     */
    public void moveTo(int x, int y) {
        destX = x;
        destY = y;
        Vector2f d = map.dijkstra(posX, posY, destX, destY);
        nextX = d.x;
        nextY = d.y;
    }

    public final void attack(int x, int y) {
        weapon.attack(pose, accuracy, dmg_mult, posX, posY, x, y);
    }

    public void setPose(Pose p) {
        pose = p;
    }

    public Pose getPose() {
        return pose;
    }

    public abstract String getTexture();

    @Override
    public final String toString() {
        return "Unit{" + "vehicle=" + vehicle + ", weapon=" + weapon + ", posX=" + posX + ", posY=" + posY + ", fractalX=" + fractalX + ", fractalY=" + fractalY + ", destX=" + destX + ", destY=" + destY + ", nextX=" + nextX + ", nextY=" + nextY + ", health=" + health + ", accuracy=" + accuracy + ", stamina=" + stamina + ", discipline=" + discipline + ", morale=" + morale + ", dmg_mult=" + dmg_mult + '}';
    }

}
