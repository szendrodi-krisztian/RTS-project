package util;

import battle.terrain.Terrain;
import battle.terrain.TerrainElementManager;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Krisz
 */
public class MapFile {

    private final String name;
    private Terrain terrain;
    private final AssetManager assets;
    private int width;
    private int heigth;

    public MapFile(String name, AssetManager as) {
        this.name = name;
        this.assets = as;
    }

    public void read() throws FileNotFoundException, IOException {
        File f = new File(name);
        System.out.println(f.getAbsolutePath());
        BufferedReader reader = new BufferedReader(new FileReader(f));
        width = Integer.parseInt(reader.readLine());
        heigth = Integer.parseInt(reader.readLine());
        terrain = new Terrain(width, heigth);
        String line;
        Vector2f pos = new Vector2f(0, 0);
        while ((line = reader.readLine()) != null) {
            pos.x = 0;
            for (char c : line.toCharArray()) {
                terrain.setTypeAt(TerrainElementManager.getInstance(assets).getElementByChar(c), pos);
                pos.x++;
            }
            pos.y++;
        }
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public int width() {
        return width;
    }

    public int heigth() {
        return heigth;
    }

    public AssetManager assets() {
        return assets;
    }

}
