package battle.terrain.generator;

import battle.terrain.TerrainElement;
import java.util.ArrayList;

/**
 *
 * @author Krisz
 */
public interface IGenerator {

    public abstract void generate(ArrayList<TerrainElement> elements[]);
}
