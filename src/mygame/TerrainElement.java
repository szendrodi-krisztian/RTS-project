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
public abstract class TerrainElement {
    
    public abstract Texture2D getTexture();
    
    public abstract boolean isAccesible();
    
    public abstract float getMovementModifier();
    
    public abstract float getProjectileResistance();
    
}
