package battle.terrain;

import battle.terrain.generator.IGenerator;
import battle.terrain.generator.SimpleGenerator;
import com.jme3.math.Vector2f;

/**
 *
 * @author szend
 */
public final class Terrain {

    // public final ArrayList<TerrainElement>[] grid;
    public final TerrainElement terrain[];
    public final TerrainElement decoration[];

    public final int mapWidth, mapHeight;

    public static final int TERRAIN_LAYER = 0;
    public static final int DECORATION_LAYER = 1;

    public Terrain(int mapWidth, int mapHeight, IGenerator generator) {
        this(mapWidth, mapHeight, false, generator);
    }

    public Terrain(int mapWidth, int mapHeight) {
        this(mapWidth, mapHeight, true, new SimpleGenerator(mapWidth, mapHeight, 0xCAFFEE));
    }

    public Terrain(int mapWidth, int mapHeight, int seed) {
        this(mapWidth, mapHeight, true, new SimpleGenerator(mapWidth, mapHeight, seed));
    }

    public Terrain(int mapWidth, int mapHeight, int seed, boolean empty) {
        this(mapWidth, mapHeight, empty, new SimpleGenerator(mapWidth, mapHeight, seed));
    }

    public Terrain(int mapWidth, int mapHeight, boolean empty, IGenerator generator) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        terrain = new TerrainElement[mapHeight * mapWidth];
        decoration = new TerrainElement[mapHeight * mapWidth];

        TerrainElement air = TerrainElementManager.getInstance().getElementByName("air");
        for (int i = 0; i < mapWidth * mapHeight; i++) {
            terrain[i] = air;
            decoration[i] = air;
        }

        if (!empty) {
            generator.generate(terrain, decoration);
        }

    }

    public void setTypeAt(TerrainElement type, Vector2f at, int index) throws ArrayIndexOutOfBoundsException {
        setTypeAt(type, (int) at.x, (int) at.y, index);
    }

    public void setTypeAt(TerrainElement type, int x, int y, int index) throws ArrayIndexOutOfBoundsException {
        if (index == 0) {
            terrain[x * mapHeight + y] = type;
        } else if (index == 1) {
            decoration[x * mapHeight + y] = type;
        }
    }

    public float getResistance(int x, int y) {
        return decoration[x * mapHeight + y].proj_resis;
    }

    public boolean isAccessible(int x, int y) {
        return terrain[x * mapHeight + y].isAccesible() && decoration[x * mapHeight + y].isAccesible();
    }

    public int width() {
        return mapWidth;
    }

    public int height() {
        return mapHeight;
    }

}
