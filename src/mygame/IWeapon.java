/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.texture.Texture2D;
import mygame.Unit.Pose;

/**
 *
 * @author Krisz
 */
public interface IWeapon {

    public Texture2D getTexture();

    public void setTexture(Texture2D tex);

    public int getMinDamage();

    public void setMinDamage(int dmg);

    public int getMaxDamage();

    public void setMaxDamage(int dmg);

    public int getAccuracy();

    public void setAccuracy(int newacc);

    public int getRange();

    public void setRange(int newrange);

    public int getCooldown();

    public void setCooldown(int newacc);

    public float getSwitchOut();

    public void setSwitchOut(float x);

    public float getSwitchIn();

    public void setSwitchIn(float x);

    public boolean isReady();

    public void setReady(boolean r);
    
    public void attack(Pose pose, float acc, float dmg_mult, int sx, int sy, int dx, int dy);
    

}
