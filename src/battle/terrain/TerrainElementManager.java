package battle.terrain;

import battle.terrain.render.CustomTextureAtlas;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Krisz
 */
public class TerrainElementManager {
    // Texture atlas for all textures.
    private final CustomTextureAtlas atlas;
    //
    private static TerrainElementManager instance;
    //
    private final Map<String, TerrainElement> terrains = new HashMap<>();
    //
    private final Material terrainMaterial;
    /**
     * Get the singleton.
     * @param assets on the first call it must be valid, after that it can be null.
     * @return The singleton.
     */
    public final static TerrainElementManager getInstance(AssetManager assets) {
        if (instance == null) {
            if (assets == null) {
                throw new RuntimeException("TerrainElementManager getInstance() asset is null!");
            }
            instance = new TerrainElementManager(assets);
        }
        return instance;
    }

    public final Material getTerrainMaterial() {
        return terrainMaterial;
    }

    public final Vector2f getTextureOffset(String name) {
        return atlas.getOffset(name);
    }

    private TerrainElementManager(AssetManager assets) {
        atlas = new CustomTextureAtlas();

        // Place all terrain types here as follows:
        TerrainElement elements[] = new TerrainElement[]{new TerrainGrass(), new TerrainStone()};

        for (TerrainElement e : elements) {
            terrains.put(e.getName(), e);
            atlas.addTexture(assets.loadTexture("Textures/terrain/" + e.getName() + ".png"), e.getName(), false);
        }
        atlas.create();
        terrainMaterial = new Material(assets, "Common/MatDefs/Misc/Unshaded.j3md");
        terrainMaterial.setTexture("ColorMap", atlas.getTexture());
    }

    public final TerrainElement getElementByName(String name) {
        TerrainElement element = terrains.get(name);
        if (element == null) {
            throw new RuntimeException("Terrain does not exist");
        }

        return element;
    }

}
