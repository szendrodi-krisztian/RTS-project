package battle.entity;

/**
 * Placeholder class for testing porpouses only.
 *
 * @author Krisz
 */
public class SimpleUnit extends Unit {

    public SimpleUnit(int x, int y) {
        super(new Foot(), new SimpleWeapon());
        pos.x = x;
        pos.y = y;
        dest.x = x;
        dest.y = y;
    }

    @Override
    public String getTexture() {
        return "unit";
    }

}
