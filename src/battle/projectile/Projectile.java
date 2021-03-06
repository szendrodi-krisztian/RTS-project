package battle.projectile;

import battle.BattleMap;
import battle.gfx.MyQuad;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.texture.Texture;
import util.Util;

/**
 *
 * @author Auror
 */
public class Projectile {

    private Vector3f speed;
    private Vector3f position;
    private int damage;
    private int range;
    private int aoeRange;
    private boolean canCollide;
    private boolean isVisible;
    private boolean canGravityAffect;
    private final Geometry geometry;
    private final BattleMap map;
    private Vector2f prevPos = new Vector2f();

    public Projectile(Vector3f speed, Vector3f position, int damage, int range, int aoeRange, boolean canCollide, boolean canGravityAffect, BattleMap map) {
        this.speed = speed;
        this.position = position;
        this.damage = damage;
        this.range = range;
        this.aoeRange = aoeRange;
        this.canCollide = canCollide;
        this.canGravityAffect = canGravityAffect;
        Mesh mesh = new MyQuad();
        geometry = new Geometry("proj", mesh);
        Material mat = new Material(Util.assets(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        Texture t = Util.assets().loadTexture(new TextureKey("Textures/projectiles/proj.png", false));
        t.setMagFilter(Texture.MagFilter.Nearest);
        t.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        mat.setTexture("ColorMap", t);
        geometry.setMaterial(mat);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        map.rootNode.attachChild(geometry);
        updateGfx();
        this.map = map;
    }

    public void reInitialise(Vector3f speed, Vector3f position, int damage, int range, int aoeRange, boolean canCollide, boolean canGravityAffect) {
        this.speed = speed;
        this.position = position;
        this.damage = damage;
        this.range = range;
        this.aoeRange = aoeRange;
        this.canCollide = canCollide;
        this.canGravityAffect = canGravityAffect;
    }

    public final void destroy() {
        map.rootNode.detachChild(geometry);
        this.speed = Vector3f.ZERO;
    }

    public final void updateGfx() {
        geometry.setLocalTranslation(position.x, 0.1f, position.z);
    }

    public void move(float tpf) {
        prevPos.x = position.x;
        prevPos.y = position.z;
        position.x += speed.x * tpf * 10;//30 ideal
        position.z += speed.z * tpf * 10;//30 ideal
        updateGfx();
    }

    public boolean movedToAnotherGrid() {
        return (int) prevPos.x != (int) position.x || (int) prevPos.y != (int) position.z;
    }

    public Vector2f getPos() {
        return new Vector2f(position.x, position.z);
    }

    public float getPosX() {
        return position.x;
    }

    public float getPosZ() {
        return position.z;
    }

    public int getPosXi() {
        return (int) position.x;
    }

    public int getPosZi() {
        return (int) position.z;
    }

    public int getDamage() {
        return damage;
    }

    public boolean canCollide() {
        return canCollide;
    }
}
