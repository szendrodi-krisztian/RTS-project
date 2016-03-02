package city.building;

import city.gfx.BuildingMesh;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 * Represents one building.
 *
 * @author Krisz
 */
public class Building {

    private final String name;
    private final Upgrade upgradeRoot;
    private final BuildingMesh mesh;
    private final BuildingWindowState windowState;
    private final Node root;

    public Building(String name, Upgrade upgradeRoot, BuildingWindowState windowState, Node root, AssetManager assets) {
        this(name, upgradeRoot, new BuildingMesh(), windowState, root, assets);
    }

    public Building(String name, Upgrade upgradeRoot, BuildingMesh mesh, BuildingWindowState windowState, Node root, AssetManager assets) {
        this.name = name;
        this.upgradeRoot = upgradeRoot;
        this.mesh = mesh;
        this.windowState = windowState;
        this.root = root;
        Geometry g = new Geometry(name, mesh);
        Material m = new Material(assets, "Common/MatDefs/Light/Lighting.j3md");
        m.setTexture("DiffuseMap", assets.loadTexture(new TextureKey("Textures/city/buildings/" + name + ".png", false)));
        m.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        g.setMaterial(m);
        g.move(6, 0.0001f, 8);
        g.setLocalScale(2);
        root.attachChild(g);
    }

}
