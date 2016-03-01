package city.building;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Static loader for Upgrade trees. Here we track the progress.
 *
 * @author Krisz
 */
public final class Upgrades {

    private static final Upgrades instance = new Upgrades();

    private final List<Upgrade> inProgess = new ArrayList<>();

    public static Upgrades getInstance() {
        return instance;
    }

    public void buy(Upgrade upgrade) {
        inProgess.add(upgrade);
    }

    public void stepTime() {
        for (Iterator<Upgrade> it = inProgess.iterator(); it.hasNext();) {
            Upgrade u = it.next();
            if (u.getTurnsToWait() == 0) {
                u.giveEffect();
                it.remove();
                continue;
            }
            u.setTurnsToWait(u.getTurnsToWait() - 1);
        }
    }

}
