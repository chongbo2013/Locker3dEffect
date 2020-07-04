package com.locker.theme.scenes3d.newscreen.face;

import android.graphics.Bitmap;

import com.badlogic.gdx.backends.android.TimeInfo;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.locker.theme.scenes3d.Scenes3DCore;
import com.locker.theme.scenes3d.newscreen.clock.ClockParser;
import com.locker.theme.scenes3d.newscreen.clock.DrawParser;
import com.locker.theme.scenes3d.newscreen.core.GLAsyncTask;
import com.locker.theme.scenes3d.newscreen.core.ILifeCycleListem;
import com.locker.theme.scenes3d.newscreen.lib.TextImageUtils;



/**
 * 该表皮，是用来修改 时间日期天气的漫反射颜色
 * Created by xff on 2017/12/27.
 */

public class TimeFace extends Face implements ILifeCycleListem {
    public TextureRegion faceTextureRegion;
    public Texture faceTexture;
    public Texture newTexture;
    public TimeFace() {
        synchronized (Scenes3DCore.scenes3DCore.lifeCycleListems) {
            Scenes3DCore.scenes3DCore.lifeCycleListems.add(this);
        }
    }

    /**
     * 更新表皮
     */
    @Override
    public void update(Actor actor) {
        if(newTexture!=null){

            if(faceTextureRegion==null){
                faceTextureRegion=new TextureRegion();
            }
            faceTextureRegion.setRegion(newTexture);
            if(faceTexture!=null){
                if(faceTexture.getTextureData()!=null){
                    Pixmap pixmap=  faceTexture.getTextureData().consumePixmap();
                    if(pixmap!=null){
                        pixmap.dispose();
                    }
                }
                faceTexture.dispose();
            }
            faceTexture=newTexture;
            newTexture=null;
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
        faceTextureRegion = null;
        if (faceTexture != null) {
            if(faceTexture.getTextureData()!=null){
                Pixmap pixmap=  faceTexture.getTextureData().consumePixmap();
                if(pixmap!=null){
                    pixmap.dispose();
                }
            }
            faceTexture.dispose();

        }
        faceTexture = null;

        if(newTexture!=null){
            if(newTexture.getTextureData()!=null){
                Pixmap pixmap=  newTexture.getTextureData().consumePixmap();
                if(pixmap!=null){
                    pixmap.dispose();
                }
            }
            newTexture.dispose();
        }
        newTexture=null;

    }

    @Override
    public void pause() {
        //清理


    }

    @Override
    public void resume() {

    }

    @Override
    public void create() {

    }

    boolean isTexLoading=false;

    @Override
    public void updateTime(TimeInfo timeInfo) {
        if(isTexLoading)
            return;
        isTexLoading=true;
        if (drawParser == null) {
            return;
        }
        if (drawParser instanceof ClockParser) {
            ((ClockParser) drawParser).update(timeInfo);
        }

        //异步处理图片
        new GLAsyncTask<DrawParser, Integer, Pixmap>() {
            @Override
            protected Pixmap doInBackground(DrawParser... clockParsers) {
                DrawParser drawParser = clockParsers[0];
                if (drawParser != null) {
                    Bitmap bitmap = drawParser.draw();
                    if (bitmap != null) {
                        Pixmap pixmap=TextImageUtils.createPixmapAsyncRecycle(bitmap);
                        drawParser.clear();
                        return pixmap;
                    }
                }
                return null;

            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                isTexLoading=false;
            }

            @Override
            protected void onPostExecute(Pixmap pixmap) {
                super.onPostExecute(pixmap);
                if (pixmap != null) {
                    PixmapTextureData pixmapTextureData=new PixmapTextureData(pixmap, (Pixmap.Format)null, false, false,true);
                    newTexture = new Texture(pixmapTextureData);
//                    pixmap.dispose();
                }
                isTexLoading=false;
            }
        }.executeOnExecutor(GLAsyncTask.THREAD_POOL_EXECUTOR, drawParser);
    }

    @Override
    public void msgUpdate(int msg) {

    }
}
