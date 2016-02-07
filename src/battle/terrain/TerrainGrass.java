package battle.terrain;

/**
 *
 * @author Krisz
 */
public class TerrainGrass extends TerrainElement{


    @Override
    public boolean isAccesible() {
        return true;
    }

    @Override
    public float getMovementModifier() {
        return 1.0f;
    }

    @Override
    public float getProjectileResistance() {
        return 0.0f;
    }

    @Override
    public String getName() {
        return "grass";
    }
    
}
