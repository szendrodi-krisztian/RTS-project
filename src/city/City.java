package city;

import city.building.Building;
import city.gfx.CityMesh;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.ArrayList;
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
        Material m = new Material(assets, "Common/MatDefs/Light/Lighting.j3md");
        m.setTexture("DiffuseMap", assets.loadTexture("Textures/terrain/grass.png"));
        cg.setLocalScale(10);
        cg.setMaterial(m);
        root.attachChild(cg);
    }

}
