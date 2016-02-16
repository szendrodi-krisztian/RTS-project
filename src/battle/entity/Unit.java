package battle.entity;

import battle.gfx.UnitMesh;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.texture.Texture;

/**
 *
 * @author Krisz
 */
public abstract class Unit extends RawUnit {

    public enum Rotation {

        NORTH, SOUTH, EAST, WEST
    }

    private final Geometry geometry;

    private Rotation rotation;

    public static final float N_STEP = 10;

    public boolean moved;

    // The position on the grid
    // positional value [0-1[
    private final Vector2f fractal = new Vector2f();

    public Unit(IVehicle vehicle, IWeapon weapon, Group group) {
        super(vehicle, weapon, group, Pose.STANDING);
        Material m = new Material(getGroup().getMap().assets, "Common/MatDefs/Light/Lighting.j3md");
        Texture t = getGroup().getMap().assets.loadTexture(new TextureKey("Textures/units/unit.png", false));
        m.setTexture("DiffuseMap", t);
        Mesh mesh = new UnitMesh();
        geometry = new Geometry("unit", mesh);
        geometry.setMaterial(m);
        getGroup().getMap().rootNode.attachChild(geometry);
        getGroup().join(this);
        moved = false;
    }

    private static final float Y_LEVEL = 0.02f;

    private void updateGfx() {
        geometry.setLocalTranslation(pos.x + fractal.x, Y_LEVEL, pos.y + fractal.y);
    }

    // NOTE: This may be wrong, needs code review
    public final void move(float tpf) {
        updateGfx();
        if (FastMath.abs(fractal.x) <= 0.01f) {
            fractal.x = -dest.x;
            pos.x += dest.x;
            dest.x = 0;
            getGroup().onUnitMovedGrid(this);
        }
        if (FastMath.abs(fractal.y) <= 0.01f) {
            fractal.y = -dest.y;
            pos.y += dest.y;
            dest.y = 0;
            getGroup().onUnitMovedGrid(this);
        }
        float s = (tpf * vehicle.getMovementSpeed()) / (N_STEP);

        if (FastMath.abs(fractal.x) > 0.01f) {
            float sb = FastMath.sign(fractal.x);
            fractal.x -= FastMath.sign(fractal.x) * (s);
            if (FastMath.sign(fractal.x) != sb) {
                fractal.x = 0;
            }
        }
        if (FastMath.abs(fractal.y) > 0.01f) {
            float sb = FastMath.sign(fractal.y);
            fractal.y -= FastMath.sign(fractal.y) * (s);
            if (FastMath.sign(fractal.y) != sb) {
                fractal.y = 0;
            }
        }
    }

    public abstract String getTexture();

    @Override
    public String toString() {
        return super.toString() + "Unit{" + "geometry=" + geometry + ", rotation=" + rotation + ", moved=" + moved + ", fractal=" + fractal + '}';
    }

}
