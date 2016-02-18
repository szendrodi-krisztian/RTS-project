package battle.terrain;

/**
 *
 * @author szend
 */
public class MyVector2f {

    public float x, y;

    public MyVector2f() {
        this(0, 0);
    }

    public MyVector2f(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public MyVector2f(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void initialise(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void initialise(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "MyVector2f{" + "x=" + x + ", y=" + y + '}';
    }
    
    

}
