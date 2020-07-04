package com.locker.theme.scenes3d.newscreen.beans;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;
import com.locker.theme.scenes3d.newscreen.event.EventBean;
import com.locker.theme.scenes3d.newscreen.event.IReceiveEvent;
import com.locker.theme.scenes3d.newscreen.event.ISendEvent;
import com.locker.theme.scenes3d.newscreen.face.Face;
import com.locker.theme.scenes3d.newscreen.face.NormalFace;
import com.locker.theme.scenes3d.newscreen.tween.BaseParserTween;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xff on 2017/12/23.
 */

public class ActorBean implements Disposable {
    public String name;
    //厚度 模型
    //缩放模式 0 正常模式 1 根据宽，2 根据高
    public int scaleMode = 0;
    public float x;
    public float y;
    public float z = 0.0f;
    public float width;
    public float height;
    public float thickness = 1.0f;

    public float originWidth;
    public float originHeight;
    public float originThickness;

    public float scaleX = 1.0f;
    public float scaleY = 1.0f;
    public float scaleZ = 1.0f;

    public float originX;
    public float originY;
    public float originZ;

    public float rotation;
    public float rotationX;
    public float rotationY;

    public Color color;

    public float lerp = 0.05f;
    //资源路径
    public String resource;
    //动画集合
    public Map<String, BaseParserTween> animations;

    //根据事件执行  组合动画
    public List<EventBean> eventAnimation;

    //漫反射纹理表皮
    public List<Face> face;
    //修改凹凸纹理表皮
    public List<NormalFace> normalFace;

    //独立出视频face
    public List<Face> videoface;

    //解锁配置
    public UnLockConfig unLockConfig;

    public String effect;

    //是否有cutmap
    public boolean enablecubemap=true;
    //是否开启深度测试
    public boolean enableDepthTest=false;
    //透明度测试
    public float alphaTest=0.05f;
    //背面裁剪
    public boolean isCullBackFace=true;

    @Override
    public void dispose() {
        if (eventAnimation != null)
            eventAnimation.clear();
        eventAnimation = null;
        if (animations != null) {
            for (BaseParserTween v : animations.values()) {
                v.dispose();
            }
            animations.clear();
        }
        animations = null;
        if (face != null) {
            for (Face faceItem : face) {
                faceItem.dispose();
            }
            face.clear();
            face = null;
        }
        if (normalFace != null) {
            for (Face faceItem : normalFace) {
                faceItem.dispose();
            }
            normalFace.clear();
            normalFace = null;
        }
        if (videoface != null) {
            for (Face faceItem : videoface) {
                faceItem.dispose();
            }
            videoface.clear();
            videoface = null;
        }

        if(unLockConfig!=null){
            unLockConfig.dispose();
        }
        unLockConfig=null;
    }
}
