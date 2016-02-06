/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector2f;

/**
 *
 * @author Krisz
 */
public class BattleMap {

    public TerrainElement grid[];
    
    public BattleMap(int n, int m){
        grid = new TerrainElement[n*m];
        TerrainElement e = TerrainElementManager.getInstance().getElementByName("grass");
        for (int i = 0; i < grid.length; i++) {
            grid[i] = e;
        }
    }

    Vector2f dijkstra(int posX, int posY, float destX, float destY) {
        return new Vector2f(1,1);
    }
    
    
}
