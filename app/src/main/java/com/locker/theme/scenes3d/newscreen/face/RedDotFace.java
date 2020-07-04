package com.locker.theme.scenes3d.newscreen.face;
import android.graphics.Color;
import com.badlogic.gdx.backends.android.TimeInfo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.locker.theme.scenes3d.Scenes3DCore;
import com.locker.theme.scenes3d.newscreen.core.ILifeCycleListem;
import com.locker.theme.scenes3d.newscreen.lib.TextImageUtils;
/**
 * 该表皮 用来 绘制 小红点
 * Created by xff on 2017/12/27.
 */

public class RedDotFace extends Face  implements ILifeCycleListem{
    public TextureRegion faceTextureRegion;
    public Texture faceTexture;
    public float width,height;
    public float x,y;
    public RedDotFace() {
        synchronized (Scenes3DCore.scenes3DCore.lifeCycleListems) {
            Scenes3DCore.scenes3DCore.lifeCycleListems.add(this);
        }
    }

    /**
     * 更新表皮
     */
    @Override
    public void update(Actor actor) {
        if(hasMsg){
            hasMsg=false;
            if(faceTexture!=null){
                faceTexture.dispose();
                faceTexture=null;
            }
            if(faceTextureRegion!=null){
                faceTextureRegion.setTexture(null);
                faceTextureRegion=null;
            }

            if(msg>0) {
                float w = width;
                float h = height;
                faceTexture= TextImageUtils.createRedDotTexture(Scenes3DCore.scenes3DCore.hostContext, Color.RED, Color.WHITE, w , h, msg);
                faceTextureRegion=new TextureRegion(faceTexture);
            }
        }


    }

    /**
     * 获取表皮
     *
     * @return
     */
    @Override
    public TextureRegion getFace() {
        return faceTextureRegion;
    }

    @Override
    public void dispose() {
        super.dispose();
        if(faceTextureRegion!=null){
            faceTextureRegion.setTexture(null);
        }
        faceTextureRegion = null;
        if (faceTexture != null) {
            faceTexture.dispose();
        }
        faceTexture = null;

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void create() {

    }

    @Override
    public void updateTime(TimeInfo timeInfo) {

    }
    int msg=0;
    boolean hasMsg=false;
    @Override
    public void msgUpdate(int msg) {
        hasMsg=true;
        this.msg=msg;
    }


}
