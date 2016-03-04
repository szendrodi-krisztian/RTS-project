package battle.terrain.generator;

import battle.terrain.TerrainElement;

/**
 *
 * @author Krisz
 */
public interface IGenerator {

    public abstract void generate(TerrainElement terrain[], TerrainElement decoration[]);
}
