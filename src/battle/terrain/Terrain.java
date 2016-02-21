package battle.terrain;

import util.SimplexNoise;
import com.jme3.asset.AssetManager;

/**
 *
 * @author szend
 */
public final class Terrain {

    public final TerrainElement grid[];
    public final int mapWidth, mapHeight;

    public Terrain(int mapWidth, int mapHeight, AssetManager assets) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        grid = new TerrainElement[mapWidth * mapHeight];
        SimplexNoise noise = new SimplexNoise(128, 0.3f, 0xCAFFEE);
        SimplexNoise treenoise = new SimplexNoise(1000, 1.2f, 0xCAFFEE);

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                float n = noise.getNoise(2f * i, 2f * j);
                if (n < -0.16) {
                    grid[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("water");
                    continue;
                }
                if (n < 0.015f) {
                    grid[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("stone");
                    continue;
                }
                if (treenoise.getNoise(20 * i, 20 * j) > 10f) {
                    grid[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("tree");
                } else {
                    grid[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("grass");
                }
            }
        }

        for (int i = 0; i < mapWidth; i++) {
            grid[i * mapHeight + 0] = TerrainElementManager.getInstance(assets).getElementByName("wall");
            grid[i * mapHeight + mapHeight - 1] = TerrainElementManager.getInstance(assets).getElementByName("wall");
        }

        for (int i = 0; i < mapHeight; i++) {
            grid[0 * mapHeight + i] = TerrainElementManager.getInstance(assets).getElementByName("wall");
            grid[(mapWidth-1) * mapHeight + i] = TerrainElementManager.getInstance(assets).getElementByName("wall");
        }
    }
    
    public float getResistance(int x, int y)
    {
        return grid[x*mapHeight+y].proj_resis;
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
