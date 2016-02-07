package battle.entity;

/**
 * Placeholder class for testing porpouses only.
 * @author Krisz
 */
public class SimpleUnit extends Unit{

    public SimpleUnit() {
        super(new Foot(), new SimpleWeapon());
    }

    @Override
    public String getTexture() {
        return "unit";
    }
    
}
