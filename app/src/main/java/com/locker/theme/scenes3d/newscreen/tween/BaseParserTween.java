package com.locker.theme.scenes3d.newscreen.tween;

import com.badlogic.gdx.utils.Disposable;
import com.locker.theme.scenes3d.newscreen.actors.View;
import com.locker.theme.scenes3d.newscreen.beans.IcontinueAction;
import com.locker.theme.scenes3d.newscreen.core.TweenAction;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.TweenEquation;

/**
 * 动态创建动画
 * Created by xff on 2017/12/26.
 */

public class BaseParserTween implements IcontinueAction<View> ,Disposable {
    public  String[] targetValues;
    public int type;
    public Object target;
    public float duration=0;
    public TweenEquation equation;
    // repeat repeatYoyo
    public String reverse="";
    public float delay=0.0f;
    public String fromTo="to";


    public BaseParserTween target(String targetValue) {
        if(targetValues==null){
            targetValues=new String[1];
        }
        this.targetValues[0] = targetValue;
        return this;
    }

    public BaseParserTween target(String targetValue1, String targetValue2) {
        if(targetValues==null){
            targetValues=new String[2];
        }
        this.targetValues[0] = targetValue1;
        this.targetValues[1] = targetValue2;
        return this;
    }

    public BaseParserTween target(String targetValue1, String targetValue2, String targetValue3) {
        if(targetValues==null){
            targetValues=new String[3];
        }
        this.targetValues[0] = targetValue1;
        this.targetValues[1] = targetValue2;
        this.targetValues[2] = targetValue3;
        return this;
    }

    public  BaseParserTween to(Object target, int tweenType, float duration) {
        this.target=target;
        this.type=tweenType;
        this.duration=duration;
        fromTo="to";
        return this;
    }

    public  BaseParserTween from(Object target, int tweenType, float duration) {
        this.target=target;
        this.type=tweenType;
        this.duration=duration;
        fromTo="from";
        return this;
    }

    public BaseParserTween delay(float delay) {
        this.delay = delay;
        return this;
    }

    public BaseParserTween ease(TweenEquation easeEquation) {
        this.equation = easeEquation;
        return this;
    }
    public int repeatCount=0;
    public float repeatDelay=0f;
    public BaseParserTween repeat(int count, float delay) {
        this.repeatCount=count;
        this.repeatDelay=delay;
        return this;
    }

    public BaseParserTween repeatYoyo(int count, float delay) {
        this.repeatCount=count;
        this.repeatDelay=delay;
          return this;
    }


    public BaseTween build() {
        return null;
    }

    @Override
    public void draw(View view, float time) {

    }

    @Override
    public void load() {
        build().start(TweenAction.manager);
    }

    @Override
    public float getRotateX() {
        return 0;
    }

    @Override
    public float getRotateY() {
        return 0;
    }

    @Override
    public float getRotateZ() {
        return 0;
    }

    @Override
    public void checkBound(View view) {

    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float getZ() {
        return 0;
    }

    @Override
    public void dispose() {

    }
}
