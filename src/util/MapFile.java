package util;

import battle.terrain.Terrain;
import battle.terrain.TerrainElementManager;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
    private boolean load = false;
    private boolean save = false;

    public MapFile(String name, AssetManager as) {
        this.name = name;
        this.assets = as;
        load = true;
    }

    public MapFile(String name, Terrain terrain) {
        this.name = name;
        this.terrain = terrain;
        assets = null;
        save = true;
    }

    public void write() throws IOException {
        if (load) {
            throw new RuntimeException("You tried to save an empty file!");
        }
        File f = new File("maps" + File.separator + name);
        f.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(f))) {
            writer.write(String.valueOf(terrain.width()));
            writer.newLine();
            writer.write(String.valueOf(terrain.height()));
            writer.newLine();
            for (int i = 0; i < terrain.width(); i++) {
                for (int j = 0; j < terrain.height(); j++) {
                    writer.write(terrain.grid[terrain.width() * j + i].getAscii());
                }
                writer.newLine();
            }
            writer.flush();
        }
    }

    public void read() throws FileNotFoundException, IOException {
        if (save) {
            System.out.println("WARNING: You will overwrite the actual terrain with the loaded one.");
        }
        File f = new File("maps" + File.separator + name);
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            width = Integer.parseInt(reader.readLine());
            heigth = Integer.parseInt(reader.readLine());
            terrain = new Terrain(width, heigth, assets);
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
