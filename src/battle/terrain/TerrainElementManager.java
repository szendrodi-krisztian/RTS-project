package battle.terrain;

import battle.gfx.CustomTextureAtlas;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector2f;
import com.jme3.texture.Texture;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Krisz
 */
public class TerrainElementManager {

    // Texture atlas for 1*1 textures.
    private final CustomTextureAtlas atlas;
    // Texture atlas for 1*2 textures.
    private final CustomTextureAtlas bigAtlas;
    //
    private static TerrainElementManager instance;
    //
    private final Map<String, TerrainElement> terrains = new LinkedHashMap<>();
    private final Map<Character, TerrainElement> terrainsByAscii = new LinkedHashMap<>();
    //
    private final Material terrainMaterial;
    //
    private final Material decorMaterial;

    /**
     * Get the singleton.
     *
     * @param assets on the first call it must be valid, after that it can be
     * null.
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

    public final Material getDecorMaterial() {
        return decorMaterial;
    }

    public final Vector2f getTextureOffset(String name) {
        switch (getElementByName(name).getLayer()) {
            case "TERRAIN":
                return atlas.getOffset(name);
            case "DECORATION":
                return bigAtlas.getOffset(name);
            default:
                return null;
        }
    }

    private TerrainElementManager(AssetManager assets) {
        atlas = new CustomTextureAtlas();
        bigAtlas = new CustomTextureAtlas();
        List<TerrainElement> elements = new ArrayList<>(10);
        // Add any special elements also to this list
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("terrain.txt"), "UTF-8"));
            String line = r.readLine();
            while (null != (line = r.readLine())) {
                //System.out.println(line);
                SimpleTerrain e = new SimpleTerrain();
                String arg[] = line.split(" ");
                e.name = arg[0];
                e.accessible = Boolean.parseBoolean(arg[1]);
                e.movement = Float.parseFloat(arg[2]);
                e.proj_resis = Float.parseFloat(arg[3]);
                e.has_alpha = Boolean.parseBoolean(arg[4]);
                e.ascii = arg[5].charAt(0);
                e.layer = arg[6];
                elements.add(e);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(TerrainElementManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TerrainElementManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (TerrainElement e : elements) {
            try {
                terrains.put(e.getName(), e);
                terrainsByAscii.put(e.ascii, e);
                if (e.getName().equals("air")) {
                    continue;
                }
                Texture t = assets.loadTexture("Textures/terrain/" + e.getName() + ".png");
                Texture am;
                if (e.has_alpha) {
                    am = assets.loadTexture("Textures/terrain/" + e.getName() + "alpha.png");
                } else {
                    am = assets.loadTexture("Textures/terrain/terrainalpha.png");
                }
                if (e.getLayer().equals("TERRAIN")) {
                    atlas.addTexture(t, e.getName(), false);
                    atlas.addTexture(am, e.getName(), true);
                }
                if (e.getLayer().equals("DECORATION")) {
                    bigAtlas.addTexture(t, e.getName(), false);
                    if (e.has_alpha) {
                        bigAtlas.addTexture(am, e.getName(), true);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        atlas.create();
        bigAtlas.create();
        terrainMaterial = new Material(assets, "Common/MatDefs/Light/Lighting.j3md");
        terrainMaterial.setTexture("DiffuseMap", atlas.getTexture());
        terrainMaterial.setTexture("AlphaMap", atlas.getAlphaTexture());
        terrainMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        terrainMaterial.getAdditionalRenderState().setDepthTest(false);
        decorMaterial = new Material(assets, "Common/MatDefs/Light/Lighting.j3md");
        decorMaterial.setTexture("DiffuseMap", bigAtlas.getTexture());
        decorMaterial.setTexture("AlphaMap", bigAtlas.getAlphaTexture());
        decorMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
    }

    public final Map<String, TerrainElement> getAllTerrains() {
        return Collections.unmodifiableMap(terrains);
    }

    public final TerrainElement getElementByName(String name) {
        return terrains.get(name);
    }

    public final TerrainElement getElementByChar(char c) {
        return terrainsByAscii.get(c);
    }

}
