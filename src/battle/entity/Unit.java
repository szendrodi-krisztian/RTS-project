package battle.entity;

import battle.entity.group.Group;
import battle.gfx.MyQuad;
import battle.path.Path;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.texture.Texture;
import java.text.MessageFormat;
import util.Util;

/**
 *
 * @author Krisz
 */
public abstract class Unit extends RawUnit {

    private static final float Y_LEVEL = 0.02f;

    public static final float N_STEP = 10;

    private final Geometry geometry;

    public boolean moved;

    private final Vector2f fractal;

    public Vector2f destination;

    public Path path;

    public Unit(IVehicle vehicle, IWeapon weapon, Group group) {
        super(vehicle, weapon, group, Pose.STANDING, 3);
        this.fractal = new Vector2f();
        Material m = new Material(getGroup().getMap().assets, "Common/MatDefs/Light/Lighting.j3md");
        m.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        Texture t = getGroup().getMap().assets.loadTexture(new TextureKey("Textures/units/le.png", false));
        Texture t2 = getGroup().getMap().assets.loadTexture(new TextureKey("Textures/units/le_alpha.png", false));
        t.setMagFilter(Texture.MagFilter.Nearest);
        t.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        t2.setMagFilter(Texture.MagFilter.Nearest);
        t2.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        m.setTexture("DiffuseMap", t);
        m.setTexture("AlphaMap", t2);
        Mesh mesh = new MyQuad();
        geometry = new Geometry("unit", mesh);
        geometry.setMaterial(m);
        getGroup().getMap().rootNode.attachChild(geometry);
        getGroup().join(this);
        moved = false;
    }

    private void updateGfx() {
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
            rotationAngle = Util.angleToPositiveToOctave(FastMath.RAD_TO_DEG *dest.angleBetween(new Vector2f(0,1)));
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

}
