package battle.projectile;

import battle.BattleMap;
import battle.gfx.MyQuad;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
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
    public boolean outOfTheGun=false;
    private final Geometry geometry;
    private final BattleMap map;

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
        this.map=map;
        this.outOfTheGun=false;
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
    
    public final void destroy(){
        map.rootNode.detachChild(geometry);
    }
    
    public final void updateGfx(){
        geometry.setLocalTranslation(position.x, 0.1f, position.z);
    }
    
    public void move(float tpf)
    {
        this.position.x+=this.speed.x*tpf*10;//30 ideal
        this.position.z+=this.speed.z*tpf*10;//30 ideal
        updateGfx();
    }   
    
    public boolean movedToAnotherGrid()
    {
        int prevX=(int)(position.x-speed.x);
        int prevZ=(int)(position.z-speed.z);
        int currX=(int)(position.x);
        int currZ=(int)(position.z);
        if(currX!=prevX || currZ!=prevZ){
            outOfTheGun=true;
            return true;
        }
        else{
            return false;
        }
    }
    
    public Vector2f getPos(){
        return new Vector2f(position.x, position.z);
    }
    
    public float getPosX(){
        return position.x;
    }
    
    public float getPosZ(){
        return position.z;
    }
    
    public int getPosXi(){
        return (int)position.x;
    }
    
    public int getPosZi(){
        return (int)position.z;
    }
    
    public int getDamage(){
        return damage;
    }
    
    public boolean canCollide(){
        return canCollide;
    }
}
