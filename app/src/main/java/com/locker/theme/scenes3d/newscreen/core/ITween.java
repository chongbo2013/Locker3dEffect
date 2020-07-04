package com.locker.theme.scenes3d.newscreen.core;

import com.badlogic.gdx.graphics.Color;

/**
 * 如果需要某个 actor实现 3D动画，需要实现该接口
 * 暂时只实现 Z深度，后面待扩展
 * Created by xff on 2017/12/5.
 */

public interface ITween {
    float getX();
    void setX(float x);

    float getY();
    void setY(float y);

    float getZ();
    void setZ(float z);

    float getWidth();
    float getHeight();


    float getScaleX();
    void setScaleX(float scaleX);

    float getScaleY();
    void setScaleY(float scaleY);

    float getOriginZ();
    void setOriginZ(float originZ);

    float getScaleZ();
    void setScaleZ(float scalez);

    float getRotation();

    Color getColor();

    void setPosition(float x,float y);
    void setScale(float x,float y);
    void setRotation(float z);

    void setRotationX(float x);
    float getRotationX();

    void setRotationY(float y);
    float getRotationY();

    void setColor(Color color);


}
