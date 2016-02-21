/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battle.gfx;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 *
 * @author szend
 */
public class ClickMesh extends Mesh {

    public ClickMesh() {
        FloatBuffer vb = BufferUtils.createFloatBuffer(4 * 3);
        FloatBuffer tc = BufferUtils.createFloatBuffer(4 * 2);
        FloatBuffer nb = BufferUtils.createFloatBuffer(4 * 3);
        IntBuffer ib = BufferUtils.createIntBuffer(6);
        vb.put(0.5f).put(0).put(-0.5f);
        vb.put(0.5f).put(0).put(0.5f);
        vb.put(-0.5f).put(0).put(0.5f);
        vb.put(-0.5f).put(0).put(-0.5f);
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

}
