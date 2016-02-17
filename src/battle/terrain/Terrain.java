package battle.terrain;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;

/**
 *
 * @author szend
 */
public final class Terrain {

    public final TerrainElement grid[];
    protected final int mapWidth, mapHeight;

    public Terrain(int mapWidth, int mapHeight, AssetManager assets) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        grid = new TerrainElement[mapWidth * mapHeight];
        SimplexNoise noise = new SimplexNoise(128, 0.3f, 0xCAFFEE);
        SimplexNoise treenoise = new SimplexNoise(1000, 1.2f, 0xCAFFEE);

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                float n = noise.getNoise(2f * i, 2f * j);
                if (n < -0.12) {
                    grid[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("water");
                    continue;
                }
                if (n < 0.015f) {
                    grid[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("stone");
                    continue;
                }
                if (treenoise.getNoise(20 * i, 20 * j) > 8f) {
                    grid[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("tree");
                } else {
                    grid[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("grass");
                }
            }
        }
    }

    public boolean isAccessible(Vector2f v) {
        return isAccessible(v.x, v.y);
    }

    public boolean isAccessible(float x, float y) {
        return isAccessible((int) x, (int) y);
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
