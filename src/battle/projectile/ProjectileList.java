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
 * TODO: In some edge cases the projectile does not get destroyed. This is due
 * to the fact that with a given speed and a low enugh fps the movement in one
 * tick may be bigger than one square and it skips the destroy trigger. May be
 * reproducible by mashing the spacebar.
 *
 * @author Auror
 */
public class ProjectileList extends ArrayList<Projectile> {

    private final BattleMap map;

    public ProjectileList(BattleMap map) {
        this.map = map;
    }

    public void moveAll(float tpf) {
        for (int i = 0; i < this.size(); i++) {
            this.get(i).move(tpf);
            if (this.get(i).movedToAnotherGrid() && this.get(i).canCollide()) {
                collision(this.get(i));
            }
        }
    }

    private void collision(Projectile projectile) {
        if (!map.getUnitsAt(projectile.getPosXi(), projectile.getPosZi()).isEmpty()) {
            for (Iterator<Unit> it = map.getUnitsAt(projectile.getPos()).iterator(); it.hasNext();) {
                Unit thi = it.next();
                thi.getHit(projectile.getDamage());
            }
            projectile.destroy();
            this.remove(projectile);
        } else if (map.getTerrainResistance(projectile.getPosXi(), projectile.getPosZi()) != 0) {
            projectile.destroy();
            this.remove(projectile);
        }
    }
}
