package battle.terrain;

import battle.terrain.generator.IGenerator;
import battle.terrain.generator.SimpleGenerator;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;

/**
 *
 * @author szend
 */
public final class Terrain {

    public final TerrainElement grid[];
    public final int mapWidth, mapHeight;
    public AssetManager assets;
    private final IGenerator generator;

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
        this.generator = generator;
        grid = new TerrainElement[mapWidth * mapHeight];

        if (!empty) {
            generator.generate(grid);
        }

    }

    public void setTypeAt(TerrainElement type, Vector2f at) throws ArrayIndexOutOfBoundsException {
        setTypeAt(type, (int) at.x, (int) at.y);
    }

    public void setTypeAt(TerrainElement type, int x, int y) throws ArrayIndexOutOfBoundsException {
        grid[x * mapHeight + y] = type;
    }

    public float getResistance(int x, int y) {
        return grid[x * mapHeight + y].proj_resis;
    }

    public boolean isAccessible(int x, int y) {
        return grid[x * mapHeight + y].isAccesible();
    }

    public int width() {
        return mapWidth;
    }

    public int height() {
        return mapHeight;
    }

}
