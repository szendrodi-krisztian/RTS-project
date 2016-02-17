package battle;

import battle.entity.SimpleUnit;
import battle.entity.Unit;
import battle.entity.UnitGrid;
import battle.entity.group.Group;
import battle.terrain.MeshedTerrain;
import battle.terrain.Terrain;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Krisz
 */
public class BattleMap {

    public MeshedTerrain terrain;

    public UnitGrid units;

    public List<Group> groups = new ArrayList<>();

    public int mapWidth, mapHeight;
    
    public AssetManager assets;
    
    public Node rootNode;

    public BattleMap(int mapWidth, int mapHeight, Node rootNode, AssetManager assets) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.assets = assets;
        this.rootNode = rootNode;
        terrain = new MeshedTerrain(new Terrain(mapWidth, mapHeight, assets), rootNode);
        
        units = new UnitGrid(mapWidth, mapHeight);
        Group g1 = new Group(this);

        spawn(3, 2, g1, SimpleUnit.class);
        spawn(1, 2, g1, SimpleUnit.class);
        spawn(2, 2, g1, SimpleUnit.class);
        spawn(4, 2, g1, SimpleUnit.class);
        spawn(5, 2, g1, SimpleUnit.class);
        spawn(6, 2, g1, SimpleUnit.class);
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

}
