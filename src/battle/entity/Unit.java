package battle.entity;

import battle.BattleMap;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.debug.WireBox;
import com.jme3.texture.Texture;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

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

    private final Geometry geometry;

    private static BattleMap map;

    private final IVehicle vehicle;
    private final IWeapon weapon;

    private Pose pose;

    private Rotation rotation;

    public static final float N_STEP = 30;

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
        Material m = new Material(assets, "Common/MatDefs/Light/Lighting.j3md");
        Texture t = assets.loadTexture(new TextureKey("Textures/units/unit.png", false));

        m.setTexture("DiffuseMap", t);
        Mesh mesh = new Mesh();
        FloatBuffer vb = BufferUtils.createFloatBuffer(4 * 3);
        FloatBuffer tc = BufferUtils.createFloatBuffer(4 * 2);
        FloatBuffer nb = BufferUtils.createFloatBuffer(4 * 3);
        IntBuffer ib = BufferUtils.createIntBuffer(6);
        vb.put(0).put(0).put(0);
        vb.put(0).put(0).put(1);
        vb.put(1).put(0).put(1);
        vb.put(1).put(0).put(0);
        ib.put(0).put(2).put(3);
        ib.put(0).put(1).put(2);
        tc.put(0).put(1);
        tc.put(0).put(0);
        tc.put(1).put(0);
        tc.put(1).put(1);
        nb.put(0).put(1).put(0);
        nb.put(0).put(1).put(0);
        nb.put(0).put(1).put(0);
        nb.put(0).put(1).put(0);
        vb.rewind();
        ib.rewind();
        tc.rewind();
        nb.rewind();

        mesh.setBuffer(VertexBuffer.Type.Position, 3, vb);
        mesh.setBuffer(VertexBuffer.Type.Index, 3, ib);
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, tc);
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, nb);

        mesh.updateBound();

        geometry = new Geometry("unit", mesh);
        geometry.setMaterial(m);
        node.attachChild(geometry);

    }

    private static final float Y_LEVEL = 0.02f;

    /**
     * Moves this unit for animation.
     *
     * @param tpf Time per frame.
     * @return
     */
    public final int move(float tpf) {
        // get new only if im in place.
        if (FastMath.abs(fractal.x) < 0.01f && FastMath.abs(fractal.y) < 0.01f) {
            next = map.pathFinder((int) pos.x, (int) pos.y, (int) dest.x, (int) dest.y);
        }
        // if reached next on x axis, refresh destination only on x axis.
        if (FastMath.abs(fractal.x) < 0.01f) {
            fractal.x = -next.x;
            pos.x += next.x;
            geometry.setLocalTranslation(pos.x + fractal.x, Y_LEVEL, pos.y + fractal.y);
            // we must return here because in one movement there can be only one grid chage, in respect to the axes.
            // if next is 0, that means we did not really move here.
            if (next.x != 0) {
                return (int) next.x;
            }
        }
        if (FastMath.abs(fractal.y) < 0.01f) {
            fractal.y = -next.y;
            pos.y += next.y;
            geometry.setLocalTranslation(pos.x + fractal.x, Y_LEVEL, pos.y + fractal.y);
            if (next.y != 0) {
                return (int) (next.y * map.mapHeight);
            }
        }
        float speed = (tpf * vehicle.getMovementSpeed()) / N_STEP;
        if (FastMath.abs(fractal.x) > 0.01f) {
            float sb = FastMath.sign(fractal.x);
            fractal.x += (fractal.x > 0) ? -speed : speed;
            if (sb != FastMath.sign(fractal.x)) {
                fractal.x = 0;
            }
        }
        if (FastMath.abs(fractal.y) > 0.01f) {
            float sb = FastMath.sign(fractal.y);
            fractal.y += (fractal.y > 0) ? -speed : speed;
            if (sb != FastMath.sign(fractal.y)) {
                fractal.y = 0;
            }
        }

        geometry.setLocalTranslation(pos.x + fractal.x, Y_LEVEL, pos.y + fractal.y);
        return 0;
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
