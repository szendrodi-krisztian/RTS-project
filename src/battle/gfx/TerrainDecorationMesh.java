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
public class TerrainDecorationMesh extends Mesh {

    private final int n, m;

    // TODO: implement batching in the sense of neighbours or even all grass etc..
    public TerrainDecorationMesh(int n, int m, ArrayList<TerrainElement> grid[]) {
        super();
        this.n = n;
        this.m = m;
        FloatBuffer vertexbuffer = BufferUtils.createFloatBuffer(n * m * 4 * 3);
        FloatBuffer normalbuffer = BufferUtils.createFloatBuffer(n * m * 4 * 3);
        FloatBuffer texCoords = BufferUtils.createFloatBuffer(n * m * 4 * 3);
        IntBuffer indecies = BufferUtils.createIntBuffer(n * m * 6);

        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i * m + j].get(Terrain.DECORATION_LAYER) != null) {
                    Vector2f base_tex = TerrainElementManager.getInstance(null).getTextureOffset(grid[i * m + j].get(Terrain.DECORATION_LAYER).getName());
                    if (base_tex == null) {
                        continue;
                    }
                    vertexbuffer.put(i).put(0).put(j);
                    vertexbuffer.put(i + 1).put(0).put(j);
                    vertexbuffer.put(i + 1).put(0).put(j + 2);
                    vertexbuffer.put(i).put(0).put(j + 2);

                    normalbuffer.put(0).put(1).put(0);
                    normalbuffer.put(0).put(1).put(0);
                    normalbuffer.put(0).put(1).put(0);
                    normalbuffer.put(0).put(1).put(0);

                    indecies.put(index + 1).put(index).put(index + 2);
                    indecies.put(index + 2).put(index).put(index + 3);
                    index += 4;

                    float bleed = 0.01f;
                    texCoords.put((base_tex.x + 1 * 256) / 2048f - bleed).put((base_tex.y + 0 * 512) / 2048f + bleed);
                    texCoords.put((base_tex.x + 0 * 256) / 2048f + bleed).put((base_tex.y + 0 * 512) / 2048f + bleed);
                    texCoords.put((base_tex.x + 0 * 256) / 2048f + bleed).put((base_tex.y + 1 * 512) / 2048f - bleed);
                    texCoords.put((base_tex.x + 1 * 256) / 2048f - bleed).put((base_tex.y + 1 * 512) / 2048f - bleed);
                }
            }
        }

        setBuffer(VertexBuffer.Type.Position, 3, vertexbuffer);
        setBuffer(VertexBuffer.Type.Normal, 3, normalbuffer);
        setBuffer(VertexBuffer.Type.Index, 3, indecies);
        setBuffer(VertexBuffer.Type.TexCoord, 2, texCoords);
        updateBound();

    }

    public void update(ArrayList<TerrainElement> grid[]) {
        FloatBuffer vertexbuffer = BufferUtils.createFloatBuffer(n * m * 4 * 3);
        FloatBuffer normalbuffer = BufferUtils.createFloatBuffer(n * m * 4 * 3);
        FloatBuffer texCoords = BufferUtils.createFloatBuffer(n * m * 4 * 3);
        IntBuffer indecies = BufferUtils.createIntBuffer(n * m * 6);

        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i * m + j].get(Terrain.DECORATION_LAYER) != null) {
                    Vector2f base_tex = TerrainElementManager.getInstance(null).getTextureOffset(grid[i * m + j].get(Terrain.DECORATION_LAYER).getName());
                    if (base_tex == null) {
                        continue;
                    }
                    vertexbuffer.put(i).put(0).put(j);
                    vertexbuffer.put(i + 1).put(0).put(j);
                    vertexbuffer.put(i + 1).put(0).put(j + 2);
                    vertexbuffer.put(i).put(0).put(j + 2);

                    normalbuffer.put(0).put(1).put(0);
                    normalbuffer.put(0).put(1).put(0);
                    normalbuffer.put(0).put(1).put(0);
                    normalbuffer.put(0).put(1).put(0);

                    indecies.put(index + 1).put(index).put(index + 2);
                    indecies.put(index + 2).put(index).put(index + 3);
                    index += 4;

                    float bleed = 0.01f;
                    texCoords.put((base_tex.x + 1 * 128) / 2048f - bleed).put((base_tex.y + 0 * 256) / 2048f + bleed);
                    texCoords.put((base_tex.x + 0 * 128) / 2048f + bleed).put((base_tex.y + 0 * 256) / 2048f + bleed);
                    texCoords.put((base_tex.x + 0 * 128) / 2048f + bleed).put((base_tex.y + 1 * 256) / 2048f - bleed);
                    texCoords.put((base_tex.x + 1 * 128) / 2048f - bleed).put((base_tex.y + 1 * 256) / 2048f - bleed);
                }
            }
        }

        setBuffer(VertexBuffer.Type.Position, 3, vertexbuffer);
        setBuffer(VertexBuffer.Type.Normal, 3, normalbuffer);
        setBuffer(VertexBuffer.Type.Index, 3, indecies);
        setBuffer(VertexBuffer.Type.TexCoord, 2, texCoords);
        updateBound();

    }
}
