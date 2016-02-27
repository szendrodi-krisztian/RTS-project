package battle.terrain.generator;

import battle.terrain.TerrainElement;
import battle.terrain.TerrainElementManager;
import com.jme3.asset.AssetManager;
import util.SimplexNoise;

/**
 *
 * @author Krisz
 */
public class SimpleGenerator implements IGenerator {

    protected final int mapWidth, mapHeight, seed;
    protected final AssetManager assets;

    public SimpleGenerator(int w, int h, AssetManager assets, int seed) {
        mapWidth = w;
        mapHeight = h;
        this.seed = seed;
        this.assets = assets;
    }

    @Override
    public void generate(TerrainElement elements[]) {
        SimplexNoise noise = new SimplexNoise(128, 0.3f, seed);
        SimplexNoise treenoise = new SimplexNoise(1000, 1.2f, seed);

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                float n = noise.getNoise(2f * i, 2f * j);
                if (n < -0.16) {
                    elements[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("water");
                    continue;
                }
                if (n < 0.015f) {
                    elements[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("stone");
                    continue;
                }
                if (treenoise.getNoise(20 * i, 20 * j) > 10f) {
                    elements[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("tree");
                } else {
                    elements[i * mapHeight + j] = TerrainElementManager.getInstance(assets).getElementByName("grass");
                }
            }
        }

        for (int i = 0; i < mapWidth; i++) {
            elements[i * mapHeight + 0] = TerrainElementManager.getInstance(assets).getElementByName("wall");
            elements[i * mapHeight + mapHeight - 1] = TerrainElementManager.getInstance(assets).getElementByName("wall");
        }

        for (int i = 0; i < mapHeight; i++) {
            elements[0 * mapHeight + i] = TerrainElementManager.getInstance(assets).getElementByName("wall");
            elements[(mapWidth - 1) * mapHeight + i] = TerrainElementManager.getInstance(assets).getElementByName("wall");
        }
    }

}
