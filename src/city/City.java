package city;

import city.building.Building;
import city.building.BuildingWindowState;
import city.gfx.CityMesh;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Must hold everything in the city view
 *
 * @author Krisz
 */
public class City {

    private final CityMesh mesh;
    private final List<Building> builds = new ArrayList<>();
    private final Node root;

    public City(Node root, AssetManager assets) {
        this.root = root;
        mesh = new CityMesh();
        Geometry cg = new Geometry("CityGeom", mesh);
        Material m = new Material(assets, "Common/MatDefs/Misc/Unshaded.j3md");
        m.setTexture("ColorMap", assets.loadTexture(new TextureKey("Textures/city/city.png", false)));
        cg.setLocalScale(16, 1, 14);
        cg.setMaterial(m);
        root.attachChild(cg);

        builds.add(new Building("building", null, new BuildingWindowState(), root, assets));

    }

    public List<Building> getBuildings() {
        return Collections.unmodifiableList(builds);
    }

}
