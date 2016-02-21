/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battle.terrain;

/**
 *
 * @author szend
 */
public class InvisibleWallTerrain extends TerrainElement {

    public InvisibleWallTerrain() {
        accessible = false;
        movement = 0.0f;
        proj_resis = 1.0f;
        name = "wall";
        texture_width = 128;
        texture_heigth = 128;
        has_alpha = false;
    }

}
