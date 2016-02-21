/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battle.projectile;

import battle.BattleMap;
import battle.entity.Unit;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Auror
 */
public class ProjectileList extends ArrayList<Projectile>{
    private final BattleMap map;
    public ProjectileList(BattleMap map) {
        this.map=map;
    }
    
    public void moveAll(float tpf)
    {
        for (Projectile thi : this) {
            thi.move(tpf);
            if(thi.movedToAnotherGrid() && thi.canCollide() && thi.outOfTheGun) {
                collision(thi);
            }
        }
    }
    
    private void collision(Projectile projectile)
    {
        if(map.getUnitsAt(projectile.getPosXi(), projectile.getPosZi())!=null){
            for (Iterator<Unit> it = map.getUnitsAt(projectile.getPos()).iterator(); it.hasNext();) {
                Unit thi = it.next();
                thi.getHit(projectile.getDamage());
            }
            projectile.destroy();
        }
        else if(true){
            
        }
    }
}
