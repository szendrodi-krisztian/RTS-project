package battle.projectile;

import battle.BattleMap;
import battle.gfx.MyQuad;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.texture.Texture;

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
        Material mat = new Material(map.assets, "Common/MatDefs/Light/Lighting.j3md");
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        Texture t = map.assets.loadTexture(new TextureKey("Textures/projectiles/proj.png", false));
        Texture t2 = map.assets.loadTexture(new TextureKey("Textures/projectiles/projalpha.png", false));
        t.setMagFilter(Texture.MagFilter.Nearest);
        t.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        t2.setMagFilter(Texture.MagFilter.Nearest);
        t2.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        mat.setTexture("DiffuseMap", t);
        mat.setTexture("AlphaMap", t2);
        geometry.setMaterial(mat);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        map.rootNode.attachChild(geometry);
        updateGfx();
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
    
    public final void updateGfx(){
        geometry.setLocalTranslation(position.x, 0.1f, position.z);
    }
    
    public void move()
    {
        this.position.x+=this.speed.x;
        this.position.z+=this.speed.z;
        updateGfx();
    }   
}
