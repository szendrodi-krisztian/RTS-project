/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battle.projectile;

import battle.BattleMap;
import battle.entity.Unit;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
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
        // Temporary code to fix exceptions
        if (projectile.getPosXi() < 0 || projectile.getPosZi() < 0 || projectile.getPosXi() >= map.mapWidth || projectile.getPosZi() >= map.mapHeight) {
            projectile.destroy();
            return;
        }
        // End of temp code
        if (!map.getUnitsAt(projectile.getPosXi(), projectile.getPosZi()).isEmpty()) {
            Vector2f asd = projectile.getPos().clone();
            asd.x = FastMath.floor(asd.x);
            asd.y = FastMath.floor(asd.y);
            for (Iterator<Unit> it = map.getUnitsAt(asd).iterator(); it.hasNext();) {
                Unit thi = it.next();
                thi.getHit(projectile.getDamage());
                if (thi.getHealth() <= 0) {
                    thi.destroy();
                    it.remove();
                    map.removeUnit(thi);
                }
            }
            projectile.destroy();
            this.remove(projectile);
        } else if (map.getTerrainResistance(projectile.getPosXi(), projectile.getPosZi()) != 0) {
            projectile.destroy();
            this.remove(projectile);
        }
    }
}
