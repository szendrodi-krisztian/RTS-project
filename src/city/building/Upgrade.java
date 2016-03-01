package city.building;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * One node of the upgrade tree of one building
 *
 * @author Krisz
 */
public abstract class Upgrade {

    private final Upgrade parent;
    private final List<Upgrade> childred = new ArrayList<>();
    private final int cost; // ???
    private int turns_to_wait;
    private boolean inProgress;

    public Upgrade(int turns_to_wait, int cost, Upgrade parent, Upgrade... childred) {
        this.cost = cost;
        this.turns_to_wait = turns_to_wait;
        this.parent = parent;
        this.childred.addAll(Arrays.asList(childred));
        inProgress = false;
    }

    public final List<Upgrade> getChildren() {
        return Collections.unmodifiableList(childred);
    }

    public final Upgrade getParent() {
        return parent;
    }

    public final void buy() {
        if (inProgress) {
            throw new RuntimeException("Already bought");
        }
        inProgress = true;
        Upgrades.getInstance().buy(this);
    }

    public abstract void giveEffect();

    public int getTurnsToWait() {
        return turns_to_wait;
    }

    public void setTurnsToWait(int n) {
        turns_to_wait = n;
    }

}
