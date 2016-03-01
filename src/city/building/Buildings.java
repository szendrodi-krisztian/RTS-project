package city.building;

import java.util.HashMap;
import java.util.Map;

/**
 * Static Loader for buildings. All buildings should be able to fit in memory
 * immeadietly, to something like a hashtable.
 *
 * @author Krisz
 */
public class Buildings {

    private static final Buildings instance = new Buildings();

    public static Buildings getInstance() {
        return instance;
    }

    Map<String, Building> builds = new HashMap<>();

    public Building getByName(String name) {
        return builds.get(name);
    }

}
