package battle.terrain;

import battle.terrain.generator.IGenerator;
import battle.terrain.generator.SimpleGenerator;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import java.util.ArrayList;

/**
 *
 * @author szend
 */
public final class Terrain {

    public final ArrayList<TerrainElement>[] grid;

    public final int mapWidth, mapHeight;
    public AssetManager assets;

    public static final int TERRAIN_LAYER = 0;
    public static final int DECORATION_LAYER = 1;

    public Terrain(int mapWidth, int mapHeight, AssetManager assets, IGenerator generator) {
        this(mapWidth, mapHeight, assets, false, generator);
    }

    public Terrain(int mapWidth, int mapHeight, AssetManager assets) {
        this(mapWidth, mapHeight, assets, true, new SimpleGenerator(mapWidth, mapHeight, assets, 0xCAFFEE));
    }

    public Terrain(int mapWidth, int mapHeight, AssetManager assets, int seed) {
        this(mapWidth, mapHeight, assets, true, new SimpleGenerator(mapWidth, mapHeight, assets, seed));
    }

    public Terrain(int mapWidth, int mapHeight, AssetManager assets, int seed, boolean empty) {
        this(mapWidth, mapHeight, assets, empty, new SimpleGenerator(mapWidth, mapHeight, assets, seed));
    }

    public Terrain(int mapWidth, int mapHeight, AssetManager assets, boolean empty, IGenerator generator) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.assets = assets;
        grid = new ArrayList[mapWidth * mapHeight];
        TerrainElement air = TerrainElementManager.getInstance(assets).getElementByName("air");
        for (int i = 0; i < mapWidth * mapHeight; i++) {
            grid[i] = new ArrayList<>(2);
            grid[i].add(air);
            grid[i].add(air);
        }

        if (!empty) {
            generator.generate(grid);
        }

    }

    public void setTypeAt(TerrainElement type, Vector2f at, int index) throws ArrayIndexOutOfBoundsException {
        setTypeAt(type, (int) at.x, (int) at.y, index);
    }

    public void setTypeAt(TerrainElement type, int x, int y, int index) throws ArrayIndexOutOfBoundsException {
        grid[x * mapHeight + y].set(index, type);
    }

    public float getResistance(int x, int y) {
        return grid[x * mapHeight + y].get(DECORATION_LAYER).proj_resis;
    }

    public boolean isAccessible(int x, int y) {
        boolean good = true;
        for (TerrainElement e : grid[x * mapHeight + y]) {
            good &= e.isAccesible();
        }
        return good;
    }

    public int width() {
        return mapWidth;
    }

    public int height() {
        return mapHeight;
    }

}
