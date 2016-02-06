/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Krisz
 */
public class TerrainElementManager {

    private static final TerrainElementManager instance = new TerrainElementManager();

    private Map<String, TerrainElement> terrains = new HashMap<>();

    static TerrainElementManager getInstance() {
        return instance;
    }

    private TerrainElementManager() {
        terrains.put("grass", new TerrainGrass());
    }

    public TerrainElement getElementByName(String name) {
        TerrainElement element = terrains.get(name);
        if (element == null) {
            throw new RuntimeException("Terrain does not exist");
        }

        return element;
    }

}
