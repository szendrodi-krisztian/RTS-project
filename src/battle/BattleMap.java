package battle;

import battle.entity.group.Group;
import battle.entity.group.OneLineFormation;
import battle.entity.group.TwoLineFormation;
import battle.entity.UnitGrid;
import battle.entity.Unit;
import battle.entity.SimpleUnit;
import battle.gfx.MeshedTerrain;
import battle.projectile.Projectile;
import battle.terrain.Terrain;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Krisz
 */
public class BattleMap {

    public final int mapWidth, mapHeight;

    public final AssetManager assets;

    public final Node rootNode;

    private final MeshedTerrain terrain;

    private final UnitGrid units;

    public BattleMap(int mapWidth, int mapHeight, Node rootNode, AssetManager assets) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.assets = assets;
        this.rootNode = rootNode;
        terrain = new MeshedTerrain(new Terrain(mapWidth, mapHeight, assets), rootNode);
        units = new UnitGrid(mapWidth, mapHeight);

        Group g1 = new Group(this, new OneLineFormation(this));
        for (int i = 0; i < 12; i++) {
            spawn(0, 0, g1, SimpleUnit.class);
        }
        g1.moveTo(10, 10);

        Group g2 = new Group(this, new TwoLineFormation(this));
        for (int i = 0; i < 12; i++) {
            spawn(0, 0, g2, SimpleUnit.class);
        }
        g2.moveTo(5, 15);
       
    }
    
    /**
     * Spawns a unit.
     *
     * @param x The position
     * @param y The position
     * @param group The group of the unit.
     * @param type The representing class of this unit. Note that this class
     * must extend {@link battle.entity.Unit} and must have only one constructor
     * with a signature of (int x, int y, Group g).
     */
    public final void spawn(int x, int y, Group group, Class<? extends Unit> type) {
        try {
            Constructor<?>[] c = type.getConstructors();
            Unit u = (Unit) c[0].newInstance(x, y, group);
            units.place(x, y, u);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(BattleMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void tick(float tpf) {
        units.move(tpf);
    }

    public boolean isTerrainAccessible(Vector2f v) {
        return isTerrainAccessible(v.x, v.y);
    }

    public boolean isTerrainAccessible(float x, float y) {
        return terrain.raw().isAccessible((int) x, (int) y);
    }

    public List<Unit> getUnitsAt(float x, float y) {
        return units.getUnitsAt(x, y);
    }

    public List<Unit> getUnitsAt(Vector2f position) {
        return units.getUnitsAt(position);
    }

}
