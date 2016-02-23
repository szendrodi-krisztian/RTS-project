package battle.gfx;

import com.google.common.collect.ImmutableMap;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;

/**
 *
 * @author szend
 */
public class UnitMesh extends Mesh {

    private final FloatBuffer tc;
    private float angle = 0;

    private static final Map<Float, Integer> angleToIndexMap = new ImmutableMap.Builder<Float, Integer>()
            .put(0f, 0)
            .put(45f, 0)
            .put(90f, 3)
            .put(135f, 3)
            .put(180f, 2)
            .put(225f, 2)
            .put(270f, 1)
            .put(315f, 1)
            .build();
    
    

    public UnitMesh() {
        FloatBuffer vb = BufferUtils.createFloatBuffer(4 * 3);
        tc = BufferUtils.createFloatBuffer(4 * 2);
        FloatBuffer nb = BufferUtils.createFloatBuffer(4 * 3);
        IntBuffer ib = BufferUtils.createIntBuffer(6);
        vb.put(0).put(0).put(0);
        vb.put(0).put(0).put(1);
        vb.put(1).put(0).put(1);
        vb.put(1).put(0).put(0);
        ib.put(0).put(2).put(3);
        ib.put(0).put(1).put(2);
        tc.put(0).put(1);
        tc.put(0).put(0);
        tc.put(1).put(0);
        tc.put(1).put(1);
        nb.put(0).put(1).put(0);
        nb.put(0).put(1).put(0);
        nb.put(0).put(1).put(0);
        nb.put(0).put(1).put(0);
        vb.rewind();
        ib.rewind();
        tc.rewind();
        nb.rewind();

        setBuffer(VertexBuffer.Type.Position, 3, vb);
        setBuffer(VertexBuffer.Type.Index, 3, ib);
        setBuffer(VertexBuffer.Type.TexCoord, 2, tc);
        setBuffer(VertexBuffer.Type.Normal, 3, nb);

        updateBound();
    }

    public void rotate(float newAngle) {
        if (newAngle == angle) {
            return;
        }
        angle = newAngle;
        int index = angleToIndexMap.get(angle);
        tc.rewind();
        tc.put(index/4f).put(1);
        tc.put(index/4f).put(0);
        tc.put((index+1)/4f).put(0);
        tc.put((index+1)/4f).put(1);
        setBuffer(VertexBuffer.Type.TexCoord, 2, tc);
    }

}
