package battle.entity;

/**
 *
 * @author Krisz
 */
public class Foot implements IVehicle{
    
    private float speed;
    private String textureName;

    public Foot() {
        speed = 30.0f;
        textureName = "foot";
    }

    @Override
    public float getMovementSpeed() {
        return speed;
    }

    @Override
    public void setMovementSpeed(int newSpeed) {
        speed = newSpeed;
    }

    @Override
    public String getTexture() {
        return textureName;
    }

    @Override
    public void setTexture(String tex) {
        textureName = tex;
    }

  


    
}
