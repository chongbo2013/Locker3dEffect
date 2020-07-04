package com.locker.theme.scenes3d.newscreen.face;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.locker.theme.scenes3d.newscreen.clock.DrawParser;

/**
 * view的表皮，可以是，视频，gif，静态图 凹凸
 * Created by xff on 2017/12/27.
 */

public abstract class Face implements Disposable {
    public String name;
    public String objectName;//附着面
    public String resource;
    public DrawParser drawParser;
    public TextureAttribute textureFaceAttribute;
    //更新表皮信息
    public abstract void update(Actor view);
    //获取表皮
    public abstract TextureRegion getFace();

    @Override
    public void dispose() {
        if(drawParser!=null){
            drawParser.dispose();
        }
        drawParser=null;
        textureFaceAttribute=null;
    }

    public void reset(){

    }
}
