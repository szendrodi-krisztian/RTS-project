/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battle.entity;

import com.jme3.texture.Texture2D;

/**
 *
 * @author Krisz
 */
public interface IVehicle {

    public int getMovementSpeed();

    public void setMovementSpeed(int newSpeed);

    public Texture2D getTexture();
    
    public void setTexture(Texture2D tex);
    
    

}
