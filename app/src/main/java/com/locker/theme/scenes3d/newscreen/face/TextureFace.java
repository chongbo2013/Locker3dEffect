package com.locker.theme.scenes3d.newscreen.face;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.locker.theme.scenes3d.Scenes3DCore;

/**
 * 普通的表皮，只有静态的纹理
 * Created by xff on 2017/12/27.
 */

public class TextureFace extends Face  {
    public TextureRegion faceTextureRegion;
    public Texture faceTexture;
    boolean load=true;
    /**
     * 更新表皮
     */
    @Override
    public void update(Actor actor) {
        if(load) {
            if(!Scenes3DCore.scenes3DCore.assets.isLoaded(resource, Texture.class))
                Scenes3DCore.scenes3DCore.assets.load(resource, Texture.class);
            load=false;
        }

        if (faceTexture == null) {
            if(Scenes3DCore.scenes3DCore.assets.isLoaded(resource, Texture.class))
                faceTexture = Scenes3DCore.scenes3DCore.assets.get(resource, Texture.class);
        }
        if (faceTextureRegion == null && faceTexture != null) {
            faceTextureRegion = new TextureRegion(faceTexture);
        }
    }

    /**
     * 获取表皮
     * @return
     */
    @Override
    public TextureRegion getFace() {
        return faceTextureRegion;
    }

    @Override
    public void dispose() {
        super.dispose();
        faceTextureRegion=null;
        if(faceTexture!=null){
            faceTexture.dispose();
        }
        faceTexture=null;
    }
}
