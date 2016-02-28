package battle.terrain.generator;

import battle.terrain.Terrain;
import battle.terrain.TerrainElement;
import battle.terrain.TerrainElementManager;
import com.jme3.asset.AssetManager;
import java.util.ArrayList;
import util.SimplexNoise;

/**
 *
 * @author Krisz
 */
public final class ForestGenerator extends SimpleGenerator {

    public ForestGenerator(int w, int h, AssetManager assets, int seed) {
        super(w, h, assets, seed);
    }

    @Override
    public void generate(ArrayList<TerrainElement>[] elements) {
        SimplexNoise noise = new SimplexNoise(128, 0.3f, seed);
        SimplexNoise treenoise = new SimplexNoise(1000, 1.2f, seed);

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                float n = noise.getNoise(2f * i, 2f * j);
                if (treenoise.getNoise(20 * i, 20 * j) > 7f) {
                    elements[i * mapHeight + j].set(Terrain.DECORATION_LAYER, TerrainElementManager.getInstance(assets).getElementByName("tree"));
                }
                elements[i * mapHeight + j].set(Terrain.TERRAIN_LAYER, TerrainElementManager.getInstance(assets).getElementByName("grass"));

            }
        }
        makeEdgeWall(elements);
    }

}
