package util;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 *
 * @author szend
 */
public final class Util {

    private static AssetManager assets;

    public static void init(AssetManager assets) {
        Util.assets = assets;
    }

    public static AssetManager assets() {
        return assets;
    }

    public static float angleToPositiveToOctave(float angle) {
        float newangle;
        newangle = (angle < 0) ? 360 + angle : angle;
        if (newangle % 45 < 22) {
            newangle -= (newangle % 45);
        } else {
            newangle += (45 - newangle % 45);
        }
        if (newangle == 360) {
            newangle = 0;
        }
        return newangle;
    }

    public static void dumpGraph(Spatial root, String pref) {
        System.out.println(pref + root.getName());
        if (root instanceof Node) {
            List<Spatial> list = ((Node) root).getChildren();
            for (Spatial s : list) {
                dumpGraph(s, pref + " ");
            }
        }
    }

}
