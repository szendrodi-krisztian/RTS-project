/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battle.projectile;

import battle.BattleMap;
import java.util.ArrayList;

/**
 *
 * @author Auror
 */
public class ProjectileList extends ArrayList<Projectile>{
    private final BattleMap map;
    public ProjectileList(BattleMap map) {
        this.map=map;
    }
    
    public void moveAll()
    {
        for (Projectile thi : this) {
            thi.move();
            collision(thi);
        }
    }
    
    private void collision(Projectile projectile)
    {
        
    }
}
