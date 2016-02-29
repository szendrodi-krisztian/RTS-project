package battle.gfx;

import battle.path.Path;
import com.jme3.math.Vector2f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;

/**
 *
 * @author Krisz
 */
public class PathMesh extends Mesh {

    private final Path path;

    public PathMesh(Path path, Vector2f start) {
        this.path = path;
        FloatBuffer vb = BufferUtils.createFloatBuffer(path.size() * 3 + 3);
        for (Vector2f v : path) {
            vb.put(v.x + 0.5f).put(0.001f).put(v.y + 0.5f);
        }
        vb.put(start.x + 0.5f).put(0.001f).put(start.y + 0.5f);
        setBuffer(VertexBuffer.Type.Position, 3, vb);
        setMode(Mesh.Mode.LineStrip);
        setLineWidth(2.f);
        updateBound();
    }

}
