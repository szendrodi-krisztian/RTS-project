package battle.terrain.render;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * This class holds the unified textures for all terrain features. It also gives
 * access to the individual texture coordinates, looked up by names.
 *
 * @author Krisz
 */
public final class CustomTextureAtlas {

    public static final int ATLAS_SIZE = 2048;

    // texture name to actual texture
    private Map<String, Texture> nameTex = new HashMap<>();
    // texture name to actual texture with only alpha information.
    private Map<String, Texture> nameTexAlpha = new HashMap<>();
    // texture coordinates on the unified texture indexed by the same name as above.
    private Map<String, Vector2f> offsetMap = new HashMap<>();
    // the unified texture
    private Texture texture;
    // the unified alpha texture
    private Texture alphaTexture;
    // state info
    private boolean created = false;

    /**
     * Add a given Texture to the atlas. Once {@link battle.terrain.render.CustomTextureAtlas#create()
     * } is called further calls here WILL BE IGNORED.
     *
     * @param t The texture
     * @param name The name of this texture, it will be accessible by this name
     * @param alpha is it a grayscale texture holding only alpha information.
     */
    public final void addTexture(Texture t, String name, boolean alpha) {
        if (created) {
            throw new UnsupportedOperationException("TextureAtlas already created, only add textures BEFORE the create(); call");
        }
        if (alpha) {
            nameTexAlpha.put(name, t);
        } else {
            nameTex.put(name, t);
        }
    }

    /**
     * Builds the unified texture. Once built dont add any more textures to this
     * atlas.
     * The build is simple: we take all the textures, and copy the image data into a grid on the big texture.
     */
    public final void create() {
        if (created) {
            throw new UnsupportedOperationException("Cannot create the texture atlas twice.");
        }
        byte b[] = new byte[ATLAS_SIZE * ATLAS_SIZE * 4];
        byte ba[] = new byte[ATLAS_SIZE * ATLAS_SIZE * 4];
        Image.Format f = nameTex.get("grass").getImage().getFormat();
        Image img = new Image(f, ATLAS_SIZE, ATLAS_SIZE, BufferUtils.createByteBuffer(b));
        Image imgAlpha = new Image(f, ATLAS_SIZE, ATLAS_SIZE, BufferUtils.createByteBuffer(ba));
        ImageRaster mainRaster = ImageRaster.create(img);
        ImageRaster alphaRaster = ImageRaster.create(imgAlpha);

        int x = 0, y;
        int ry = 0;

        for (String key : nameTex.keySet()) {
            Texture t = nameTex.get(key);
            Image ti = t.getImage();
            ImageRaster raster = ImageRaster.create(ti);

            Texture alp = nameTexAlpha.get(key);
            Image talp;
            ImageRaster rasterAlp = null;
            if (alp != null) {
                talp = alp.getImage();
                rasterAlp = ImageRaster.create(talp);
            }

            int tw = raster.getWidth(), th = raster.getHeight();

            if (x + tw > ATLAS_SIZE) {
                ry += th;
                x = 0;
            }
            offsetMap.put(key, new Vector2f(x, ry));
            for (int tx = 0; tx < tw; tx++) {
                y = ry;
                for (int ty = 0; ty < th; ty++) {
                    if (rasterAlp != null) {
                        alphaRaster.setPixel(x, y, rasterAlp.getPixel(tx, ty));
                    } else {
                        alphaRaster.setPixel(x, y, ColorRGBA.White);
                    }
                    mainRaster.setPixel(x, y, raster.getPixel(tx, ty));
                    y++;
                }
                x++;
            }

        }
        texture = new Texture2D(img);
        alphaTexture = new Texture2D(imgAlpha);
        created = true;
    }

    public final Vector2f getOffset(String name) {
        if (!created) {
            throw new UnsupportedOperationException("TextureAtlas not ready (call create() first)");
        }
        return offsetMap.get(name);
    }

    public final Texture getTexture() {
        if (!created) {
            throw new UnsupportedOperationException("TextureAtlas not ready (call create() first)");
        }
        return texture;
    }

    public final Texture getAlphaTexture() {
        if (!created) {
            throw new UnsupportedOperationException("TextureAtlas not ready (call create() first)");
        }
        return alphaTexture;
    }
}
