package city;

import city.building.Building;
import city.gfx.CityMesh;
import java.util.ArrayList;
import java.util.List;

/**
 * Must hold everything in the city view
 *
 * @author Krisz
 */
public class City {

    private CityMesh mesh;
    private final List<Building> builds = new ArrayList<>();

}
