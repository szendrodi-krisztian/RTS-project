/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.texture.Texture2D;

/**
 *
 * @author Krisz
 */
public class TerrainGrass extends TerrainElement{

    @Override
    public Texture2D getTexture() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isAccesible() {
        return true;
    }

    @Override
    public float getMovementModifier() {
        return 1.0f;
    }

    @Override
    public float getProjectileResistance() {
        return 0.0f;
    }
    
}
