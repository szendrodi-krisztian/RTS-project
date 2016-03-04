package battle.terrain.generator;

import battle.terrain.TerrainElement;
import battle.terrain.TerrainElementManager;
import util.SimplexNoise;

/**
 *
 * @author Krisz
 */
public class SimpleGenerator implements IGenerator {

    protected final int mapWidth, mapHeight, seed;

    public SimpleGenerator(int w, int h, int seed) {
        mapWidth = w;
        mapHeight = h;
        this.seed = seed;
    }

    @Override
    public void generate(TerrainElement terrain[], TerrainElement decoration[]) {
        SimplexNoise noise = new SimplexNoise(128, 0.3f, seed);
        SimplexNoise treenoise = new SimplexNoise(1000, 1.2f, seed);

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                float n = noise.getNoise(2f * i, 2f * j);
                if (n < -0.16) {
                    terrain[i * mapHeight + j] = TerrainElementManager.getInstance().getElementByName("water");
                    continue;
                }
                if (n < 0.015f) {
                    terrain[i * mapHeight + j] = TerrainElementManager.getInstance().getElementByName("sand");

                }
                if (treenoise.getNoise(20 * i, 20 * j) > 10f) {
                    decoration[i * mapHeight + j] = TerrainElementManager.getInstance().getElementByName("tree");
                }
                if (n > 0.015f) {
                    terrain[i * mapHeight + j] = TerrainElementManager.getInstance().getElementByName("grass");
                }

            }
        }

        makeEdgeWall(terrain, decoration);
    }

    protected final void makeEdgeWall(TerrainElement terrain[], TerrainElement decoration[]) {

        for (int i = 0; i < mapWidth; i++) {
            terrain[i * mapHeight + 0] = TerrainElementManager.getInstance().getElementByName("wall");
            terrain[i * mapHeight + mapHeight - 1] = TerrainElementManager.getInstance().getElementByName("wall");
        }

        for (int i = 0; i < mapHeight; i++) {
            terrain[0 * mapHeight + i] = TerrainElementManager.getInstance().getElementByName("wall");
            terrain[(mapWidth - 1) * mapHeight + i] = TerrainElementManager.getInstance().getElementByName("wall");
        }
    }

}
