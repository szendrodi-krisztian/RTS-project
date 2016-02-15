package battle.entity;

import battle.BattleMap;
import battle.path.Path;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Texture;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

/**
 *
 * @author Krisz
 */
public abstract class Unit extends RawUnit{


    public enum Rotation {

        NORTH, SOUTH, EAST, WEST
    }

    private final Geometry geometry;

    public  static BattleMap map;

    

    private Rotation rotation;

    public static final float N_STEP = 30;
    
    public boolean moved;

    // The position on the grid
    
    // positional value [0-1[
    private final Vector2f fractal = new Vector2f();

    //
    private static AssetManager assets;
    //
    private static Node node;
    //

    public static void init(BattleMap map, AssetManager assets, Node root) {
        Unit.node = root;
        Unit.assets = assets;
        Unit.map = map;
    }
    
   

    public Unit(IVehicle vehicle, IWeapon weapon, Group group) {
        super(vehicle, weapon, group, Pose.STANDING);
        group.join(this);
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
        int ret = 0;
        // get new only if im in place.
        if (FastMath.abs(fractal.x) < 0.01f && FastMath.abs(fractal.y) < 0.01f) {
            moveTo(dest, map);
        }
        // if reached next on x axis, refresh destination only on x axis.
        if (FastMath.abs(fractal.x) < 0.01f) {
            fractal.x = -nextStepDirection().x;
            pos.x += nextStepDirection().x;
            geometry.setLocalTranslation(pos.x + fractal.x, Y_LEVEL, pos.y + fractal.y);
            // we must return here because in one movement there can be only one grid chage, in respect to the axes.
            // if next is 0, that means we did not really move here.
            if (nextStepDirection().x != 0) {
                ret =  (int) (nextStepDirection().x * map.mapHeight);
                group.onUnitMovedGrid(this);
            }
        }
        if (FastMath.abs(fractal.y) < 0.01f) {
            fractal.y = -nextStepDirection().y;
            pos.y += nextStepDirection().y;
            geometry.setLocalTranslation(pos.x + fractal.x, Y_LEVEL, pos.y + fractal.y);
            if (nextStepDirection().y != 0) {
                ret +=(int) (nextStepDirection().y);
                group.onUnitMovedGrid(this);
            }
        }
        float speed = (tpf * vehicle.getMovementSpeed()) / (N_STEP*(FastMath.abs(nextStepDirection().x)+FastMath.abs(nextStepDirection().y)+((nextStepDirection().x==0&&nextStepDirection().y==0)?1:0)));
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
        return ret;
    }

    /**
     * Sets movement destination.
     *
     * @param x
     * @param y
     */
    public void moveTo(int x, int y) {
        moveTo(new Vector2f(x, y), map);
    }


    public Pose getPose() {
        return pose;
    }

    public abstract String getTexture();

    @Override
    public String toString() {
        return "Unit{" + "geometry=" + geometry + ", rotation=" + rotation + ", moved=" + moved + ", fractal=" + fractal + '}';
    }

    

}
