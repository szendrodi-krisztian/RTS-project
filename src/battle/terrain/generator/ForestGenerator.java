package battle.terrain.generator;

import battle.terrain.TerrainElement;
import battle.terrain.TerrainElementManager;
import util.SimplexNoise;

/**
 *
 * @author Krisz
 */
public final class ForestGenerator extends SimpleGenerator {

    public ForestGenerator(int w, int h, int seed) {
        super(w, h, seed);
    }

    @Override
    public void generate(TerrainElement terrain[], TerrainElement decoration[]) {
        SimplexNoise noise = new SimplexNoise(128, 0.3f, seed);
        SimplexNoise treenoise = new SimplexNoise(1000, 1.2f, seed);

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                float n = noise.getNoise(2f * i, 2f * j);
                if (treenoise.getNoise(20 * i, 20 * j) > 7f) {
                    decoration[i * mapHeight + j] = TerrainElementManager.getInstance().getElementByName("tree");
                }
                terrain[i * mapHeight + j] = TerrainElementManager.getInstance().getElementByName("grass");

            }
        }
        makeEdgeWall(terrain, decoration);
    }

}
