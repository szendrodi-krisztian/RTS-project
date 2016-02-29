package battle.gfx;

import battle.terrain.Terrain;
import battle.terrain.TerrainElement;
import battle.terrain.TerrainElementManager;
import com.jme3.math.Vector2f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;

/**
 *
 * @author Krisz
 */
public class TerrainGridMesh extends Mesh {

    private final int n, m;

    // TODO: implement batching in the sense of neighbours or even all grass etc..
    public TerrainGridMesh(int n, int m, ArrayList<TerrainElement> grid[]) {
        super();
        this.n = n;
        this.m = m;
        FloatBuffer vertexbuffer = BufferUtils.createFloatBuffer(n * m * 4 * 3);
        FloatBuffer normalbuffer = BufferUtils.createFloatBuffer(n * m * 4 * 3);
        FloatBuffer texCoords = BufferUtils.createFloatBuffer(n * m * 4 * 2);
        IntBuffer indecies = BufferUtils.createIntBuffer(n * m * 6);

        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                float off = 0.2f;
                Vector2f base_tex;
                base_tex = TerrainElementManager.getInstance(null).getTextureOffset(grid[i * m + j].get(Terrain.TERRAIN_LAYER).getName());

                if (base_tex == null) {
                    continue;
                }

                vertexbuffer.put(i - off).put(0).put(j - off);
                vertexbuffer.put(i + 1 + off).put(0).put(j - off);
                vertexbuffer.put(i + 1 + off).put(0).put(j + 1 + off);
                vertexbuffer.put(i - off).put(0).put(j + 1 + off);

                normalbuffer.put(0).put(1).put(0);
                normalbuffer.put(0).put(1).put(0);
                normalbuffer.put(0).put(1).put(0);
                normalbuffer.put(0).put(1).put(0);

                indecies.put(index + 1).put(index).put(index + 2);
                indecies.put(index + 2).put(index).put(index + 3);
                index += 4;

                float bleed = 0.0001f;
                texCoords.put((base_tex.x + 0 * 128) / 2048f + bleed).put((base_tex.y + 0 * 128) / 2048f + bleed);
                texCoords.put((base_tex.x + 0 * 128) / 2048f + bleed).put((base_tex.y + 1 * 128) / 2048f - bleed);
                texCoords.put((base_tex.x + 1 * 128) / 2048f - bleed).put((base_tex.y + 1 * 128) / 2048f - bleed);
                texCoords.put((base_tex.x + 1 * 128) / 2048f - bleed).put((base_tex.y + 0 * 128) / 2048f + bleed);

            }
        }

        setBuffer(VertexBuffer.Type.Position, 3, vertexbuffer);
        setBuffer(VertexBuffer.Type.Normal, 3, normalbuffer);
        setBuffer(VertexBuffer.Type.Index, 3, indecies);
        setBuffer(VertexBuffer.Type.TexCoord, 2, texCoords);
        updateBound();
    }

    public void update(ArrayList<TerrainElement> grid[], int x, int y) {
        FloatBuffer texCoords = (FloatBuffer) getBuffer(VertexBuffer.Type.TexCoord).getData();
        texCoords.rewind();
        texCoords.position(8 * (x * m + y));
        Vector2f base_tex;
        base_tex = TerrainElementManager.getInstance(null).getTextureOffset(grid[x * m + y].get(Terrain.TERRAIN_LAYER).getName());

        if (base_tex == null) {
            return;
        }

        float bleed = 0.0001f;
        texCoords.put((base_tex.x + 0 * 128) / 2048f + bleed).put((base_tex.y + 0 * 128) / 2048f + bleed);
        texCoords.put((base_tex.x + 0 * 128) / 2048f + bleed).put((base_tex.y + 1 * 128) / 2048f - bleed);
        texCoords.put((base_tex.x + 1 * 128) / 2048f - bleed).put((base_tex.y + 1 * 128) / 2048f - bleed);
        texCoords.put((base_tex.x + 1 * 128) / 2048f - bleed).put((base_tex.y + 0 * 128) / 2048f + bleed);

        setBuffer(VertexBuffer.Type.TexCoord, 2, texCoords);
    }

}
