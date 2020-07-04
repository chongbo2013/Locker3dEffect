package com.locker.theme.scenes3d.newscreen.core;

import com.badlogic.gdx.graphics.Color;
import com.locker.theme.scenes3d.newscreen.core.ITween;

import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;


/**
 *
 * http://robertpenner.com/easing/easing_demo.html 插值器参考
 * 扩展 Z深度
 * rotation X Y
 * Created by xff on 2017/11/30.
 */
public class TweenAction implements TweenAccessor<ITween> {
    public static final int POS_XY = 1;
    public static final int CPOS_XY = 2;
    public static final int SCALE_XY = 3;
    public static final int ROTATION = 4;
    public static final int OPACITY = 5;
    public static final int TINT = 6;
    public static final int POS_Z = 7;
    public static final int ROTATION_XY = 8;
    public static final int SCALE_Z = 9;
    public static final int POS_XYZ = 10;
    public static final int SCALE_XYZ = 11;
    public static final int ROTATION_XYZ = 12;
    public static final int POS_X = 13;
    public static final int POS_Y = 14;
    public final static TweenManager manager = new TweenManager();

    @Override
    public int getValues(
            ITween target,
            int tweenType,
            float[] returnValues) {
        switch (tweenType) {
            case POS_XY:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            case CPOS_XY:
                returnValues[0] = target.getX() + target.getWidth() / 2;
                returnValues[1] = target.getY() + target.getHeight() / 2;
                return 2;
            case SCALE_XY:
                returnValues[0] = target.getScaleX();
                returnValues[1] = target.getScaleY();
                return 2;
            case ROTATION:
                returnValues[0] = target.getRotation();
                return 1;
            case OPACITY:
                returnValues[0] = target.getColor().a;
                return 1;
            case TINT:
                returnValues[0] = target.getColor().r;
                returnValues[1] = target.getColor().g;
                returnValues[2] = target.getColor().b;
                return 3;
            case POS_Z:
                returnValues[0] = target.getZ();
                return 1;
            case POS_X:
                returnValues[0] = target.getX();
                return 1;
            case POS_Y:
                returnValues[0] = target.getY();
                return 1;
            case SCALE_Z:
                returnValues[0] = target.getScaleZ();
                return 1;
            case ROTATION_XY:
                returnValues[0] = target.getRotationX();
                returnValues[1] = target.getRotationY();
                return 2;
            case POS_XYZ:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                returnValues[2] = target.getZ();
                return 3;
            case SCALE_XYZ:
                returnValues[0] = target.getScaleX();
                returnValues[1] = target.getScaleY();
                returnValues[2] = target.getScaleZ();
                return 3;

            case ROTATION_XYZ:
                returnValues[0] = target.getRotationX();
                returnValues[1] = target.getRotationY();
                returnValues[2] = target.getRotation();
                return 3;

            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(
            ITween target,
            int tweenType,
            float[] newValues) {
        switch (tweenType) {
            case POS_XY:
                target.setPosition(newValues[0], newValues[1]);
                break;
            case CPOS_XY:
                target.setPosition(newValues[0] - target.getWidth() / 2, newValues[1] - target.getHeight() / 2);
                break;
            case SCALE_XY:
                target.setScale(newValues[0], newValues[1]);
                break;
            case ROTATION:
                target.setRotation(newValues[0]);
                break;
            case OPACITY:
                Color c = target.getColor();
                c.set(c.r, c.g, c.b, newValues[0]);
                target.setColor(c);
                break;
            case TINT:
                c = target.getColor();
                c.set(newValues[0], newValues[1], newValues[2], c.a);
                target.setColor(c);
                break;
            case POS_Z:
                target.setZ(newValues[0]);
                break;
            case POS_X:
                target.setX(newValues[0]);
                break;
            case POS_Y:
                target.setY(newValues[0]);
                break;
            case SCALE_Z:
                target.setScaleZ(newValues[0]);
                break;
            case ROTATION_XY:
                target.setRotationX(newValues[0]);
                target.setRotationY(newValues[1]);
                break;
            case POS_XYZ:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
                target.setZ(newValues[2]);
                break;
            case SCALE_XYZ:
                target.setScaleX(newValues[0]);
                target.setScaleY(newValues[1]);
                target.setScaleZ(newValues[2]);
                break;
            case ROTATION_XYZ:
                target.setRotationX(newValues[0]);
                target.setRotationY(newValues[1]);
                target.setRotation(newValues[2]);
                break;
            default:
                assert false;
        }
    }


    public void test(){
        //串行
//        Timeline.createSequence()
//                .beginSequence()
//                .push(Tween.to(image, ActorAccessor.POS_XY,1.0f).target(100,
//                        100))
//                .push(Tween.to(image, ActorAccessor.POS_XY,1.0f).target(200,
//                        20)).start(tweenManager);
        //并行
//        Timeline.createParallel()
//                .beginParallel()
//                .push(Tween.to(image, ActorAccessor.CPOS_XY,1.0f).target(
//                        vector3.x, vector3.y))
//                .push(Tween.to(image, ActorAccessor.ROTATION,1.0f).target(360))
//                .push(Tween.to(image, ActorAccessor.SCALE_XY,1.0f).target(
//                        1.5f,1.5f)).end().start(tweenManager);
    }
}