package battle.entity;

import battle.entity.group.Group;
import battle.gfx.UnitMesh;
import battle.path.Path;
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

    public final void moveTo(Vector2f dest) {
        this.destination = dest;
        path = new Path(pos, destination, getGroup().getMap().terrain.raw(), getGroup().getMap().units);
    }

    public Vector2f destination;

    public Path path;

    private void nextStep() {
        if (destination.equals(pos)) {
            dest.x = 0;
            dest.y = 0;
            return;
        }
        path.setStart((int) pos.x, (int) pos.y);
        path.reCalculate();
        dest = path.first();
    }

    public final void move(float tpf) {
        updateGfx();
        if (fractal.lengthSquared() == 0) {
            nextStep();
            fractal.subtractLocal(dest);
            pos.addLocal(dest);
            getGroup().onUnitMovedGrid(this);
        } else {
            float sx = FastMath.sign(fractal.x) * vehicle.getMovementSpeed() * tpf;
            float sy = FastMath.sign(fractal.y) * vehicle.getMovementSpeed() * tpf;
            if (FastMath.sign(fractal.x - sx) != FastMath.sign(fractal.x)) {
                sx = fractal.x;
            }
            if (FastMath.sign(fractal.y - sy) != FastMath.sign(fractal.y)) {
                sy = fractal.y;
            }
            fractal.subtractLocal(sx, sy);
        }
    }

    public abstract String getTexture();

    @Override
    public String toString() {
        return super.toString() + "Unit{" + "geometry=" + geometry + ", rotation=" + rotation + ", moved=" + moved + ", fractal=" + fractal + '}';
    }

}
