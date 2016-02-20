/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battle.entity;

import battle.entity.RawUnit.Pose;
import com.jme3.math.Vector2f;


/**
 *
 * @author Krisz
 */
public interface IWeapon {

    public String getTexture();

    public void setTexture(String tex);

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
    
    public void attack(Pose pose, float acc, float dmg_mult, Vector2f source, float roatationAngle, int dx, int dy);
    

}
