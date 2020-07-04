package com.locker.theme.scenes3d.newscreen.face;
import com.badlogic.gdx.backends.android.TimeInfo;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.locker.theme.scenes3d.Scenes3DCore;
import com.locker.theme.scenes3d.newscreen.core.ILifeCycleListem;
import com.locker.theme.scenes3d.newscreen.lib.GdxVideoPlayer;
import java.io.FileNotFoundException;


/**
 * 视频动态表皮
 * Created by xff on 2017/12/27.
 */

public class VideoFace extends Face implements ILifeCycleListem{
    //如果存在多个相同的视频，可能需要做处理，防止创建多个视频，以及更新多个视频纹理，导致性能下降和内存消耗
    public GdxVideoPlayer gdxVideoPlayer;
    public boolean mute=true;
    public boolean loop=true;

    public VideoFace(){
        synchronized (Scenes3DCore.scenes3DCore.lifeCycleListems) {
            Scenes3DCore.scenes3DCore.lifeCycleListems.add(this);
        }

            gdxVideoPlayer = new GdxVideoPlayer(new Runnable() {
                @Override
                public void run() {
                    gdxVideoPlayer.setConfig(mute,loop);
                    try {
                        gdxVideoPlayer.play(Scenes3DCore.getFileHandleString(resource));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
    }
    /**
     * 更新表皮
     */
    @Override
    public void update(Actor actor) {




        if(gdxVideoPlayer!=null)
            gdxVideoPlayer.render();
    }

    /**
     * 获取表皮
     * @return
     */
    @Override
    public TextureRegion getFace() {
        return null;
    }



    public void pause() {
        if(gdxVideoPlayer!=null) gdxVideoPlayer.pause();
    }

    public void resume() {
        if(gdxVideoPlayer!=null)
            gdxVideoPlayer.resume();
    }

    @Override
    public void create() {

    }

    @Override
    public void updateTime(TimeInfo timeInfo) {

    }

    @Override
    public void msgUpdate(int msg) {

    }

    @Override
    public void dispose() {
        super.dispose();
        if(gdxVideoPlayer!=null)
            gdxVideoPlayer.dispose();
        gdxVideoPlayer=null;

    }
}
