package battle.entity;

import battle.entity.group.Group;
import battle.gfx.UnitMesh;
import battle.path.Path;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;
import java.text.MessageFormat;
import util.Util;

/**
 *
 * @author Krisz
 */
public abstract class Unit extends RawUnit {

    private static final float Y_LEVEL = 0.00002f;

    public static final float N_STEP = 10;

    private final Geometry geometry;

    private final UnitMesh mesh;

    public boolean moved;

    private final Vector2f fractal;

    public Vector2f destination;

    public Path path;

    private static Material material = null;

    public Unit(IVehicle vehicle, IWeapon weapon, Group group) {
        super(vehicle, weapon, group, Pose.STANDING, 2);
        this.fractal = new Vector2f();
        if (material == null) {
            material = new Material(Util.assets(), "Common/MatDefs/Misc/Unshaded.j3md");
            material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
            Texture t = Util.assets().loadTexture(new TextureKey("Textures/units/unit_atlas.png", false));
            t.setMagFilter(Texture.MagFilter.Nearest);
            t.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
            material.setTexture("ColorMap", t);
        }
        mesh = new UnitMesh();
        geometry = new Geometry("unit", mesh);
        geometry.setMaterial(material);
        getGroup().getMap().rootNode.attachChild(geometry);
        getGroup().join(this);
        moved = false;
    }

    private void updateGfx() {
        mesh.rotate(rotationAngle);
        geometry.setLocalTranslation(pos.x + fractal.x, Y_LEVEL, pos.y + fractal.y);
    }

    public final void moveTo(Vector2f dest) {
        this.destination = dest;
        path = new Path(pos, destination, getGroup().getMap());
    }

    private void nextStep() {
        if (destination.equals(pos)) {
            dest.x = 0;
            dest.y = 0;
            rotationAngle = finalRotationAngle;
            return;
        }
        path.setStart((int) pos.x, (int) pos.y);
        path.reValidate();
        if (!path.isEmpty()) {
            dest = path.first();
            rotationAngle = Util.angleToPositiveToOctave(FastMath.RAD_TO_DEG * dest.angleBetween(new Vector2f(0, 1)));
        } else {

        }
    }

    public final void move(float tpf) {
        updateGfx();
        if (fractal.lengthSquared() == 0) {
            nextStep();
            fractal.subtractLocal(dest);
            pos.addLocal(dest);
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
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format("Unit\n{0}\n\tfractal is {1}\n\tfinal destination is {2}", super.toString(), fractal, destination));
        return sb.toString();
    }

    public void destroy() {
        geometry.getParent().detachChild(geometry);
        getGroup().leave(this);
    }
}
