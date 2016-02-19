package battle;

import battle.entity.group.OneLineFormation;
import battle.entity.SimpleUnit;
import battle.entity.group.TwoLineFormation;
import battle.entity.Unit;
import battle.entity.UnitGrid;
import battle.entity.group.Group;
import battle.terrain.MeshedTerrain;
import battle.terrain.Terrain;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
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
        Group g1 = null;
        Group g2 = null;
        try {
            g1 = new Group(this, new OneLineFormation(this));
            g2 = new Group(this, new TwoLineFormation(this));
        } catch (Exception e) {
        }
        spawn(3, 2, g1, SimpleUnit.class);
        spawn(1, 2, g1, SimpleUnit.class);
        spawn(2, 2, g1, SimpleUnit.class);
        spawn(4, 2, g1, SimpleUnit.class);
        spawn(5, 2, g1, SimpleUnit.class);
        spawn(6, 2, g1, SimpleUnit.class);
        spawn(7, 2, g1, SimpleUnit.class);
        spawn(3, 3, g1, SimpleUnit.class);
        spawn(1, 3, g1, SimpleUnit.class);
        spawn(2, 3, g1, SimpleUnit.class);
        spawn(4, 3, g1, SimpleUnit.class);
        spawn(5, 3, g1, SimpleUnit.class);

        spawn(3, 2, g2, SimpleUnit.class);
        spawn(1, 2, g2, SimpleUnit.class);
        spawn(2, 2, g2, SimpleUnit.class);
        spawn(4, 2, g2, SimpleUnit.class);
        spawn(5, 2, g2, SimpleUnit.class);
        spawn(6, 2, g2, SimpleUnit.class);
        spawn(7, 2, g2, SimpleUnit.class);
        spawn(3, 3, g2, SimpleUnit.class);
        spawn(1, 3, g2, SimpleUnit.class);
        spawn(2, 3, g2, SimpleUnit.class);
        spawn(4, 3, g2, SimpleUnit.class);
        spawn(5, 3, g2, SimpleUnit.class);
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
