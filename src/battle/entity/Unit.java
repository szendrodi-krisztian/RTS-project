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
    protected Vector2f pos = new Vector2f();
    // positional value [0-1[
    private final Vector2f fractal = new Vector2f();
    private final Vector2f dest = new Vector2f();
    private Vector2f next = new Vector2f();
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
        geometry.rotate(new Quaternion(new float[]{-FastMath.HALF_PI, 0, 0}));
        geometry.setMaterial(m);
        node.attachChild(geometry);
    }

    /**
     * Moves this unit for animation.
     *
     * @param tpf Time per frame.
     * @return
     */
    public final int move(float tpf) {
        int ret = 0;
        fractal.addLocal(next.mult(tpf * vehicle.getMovementSpeed() / N_STEP));
        if (next.x == 0 && FastMath.abs(fractal.x) > 0.011f) {
            fractal.x += -(tpf * vehicle.getMovementSpeed() / N_STEP) * FastMath.sign(fractal.x);
            geometry.setLocalTranslation(pos.x + fractal.x, 0.0001f, pos.y + fractal.y);
            return ret;
        }
        if (next.y == 0 && FastMath.abs(fractal.y) > 0.011f) {
            fractal.y += -(tpf * vehicle.getMovementSpeed() / N_STEP) * FastMath.sign(fractal.y);
            geometry.setLocalTranslation(pos.x + fractal.x, 0.0001f, pos.y + fractal.y);
            return ret;
        }
        if (FastMath.abs(fractal.x) >= 1) {
            pos.x += FastMath.sign(fractal.x);
            ret = (int) FastMath.sign(fractal.x) * map.n;
            fractal.x = 0;
            next = map.dijkstra((int) pos.x, (int) pos.y, dest.x, dest.y); // map.disjk : gives next position on the path
            geometry.setLocalTranslation(pos.x + fractal.x, 0.0001f, pos.y + fractal.y);
            return ret;
        }
        if (FastMath.abs(fractal.y) >= 1) {
            ret = (int) (FastMath.sign(fractal.y));
            pos.y += FastMath.sign(fractal.y);
            fractal.y = 0;
            next = map.dijkstra((int) pos.x, (int) pos.y, dest.x, dest.y);
            geometry.setLocalTranslation(pos.x + fractal.x, 0.0001f, pos.y + fractal.y);
            return ret;
        }
        geometry.setLocalTranslation(pos.x + fractal.x, 0.0001f, pos.y + fractal.y);
        return ret;
    }

    /**
     * Sets movement destination.
     *
     * @param x
     * @param y
     */
    public void moveTo(int x, int y) {
        dest.x = x;
        dest.y = y;
        Vector2f d = map.dijkstra((int) pos.x, (int) pos.y, dest.x, dest.y);
        next.x = d.x;
        next.y = d.y;
    }

    public final void attack(int x, int y) {
        weapon.attack(pose, accuracy, dmg_mult, (int) pos.x, (int) pos.y, x, y);
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
        return "Unit{" + "vehicle=" + vehicle + ", weapon=" + weapon + ", pos.x=" + pos.x + ", pos.y=" + pos.y + ", fractal.x=" + fractal.x + ", fractal.y=" + fractal.y + ", dest.x=" + dest.x + ", dest.y=" + dest.y + ", next.x=" + next.x + ", next.y=" + next.y + ", health=" + health + ", accuracy=" + accuracy + ", stamina=" + stamina + ", discipline=" + discipline + ", morale=" + morale + ", dmg_mult=" + dmg_mult + '}';
    }

}
