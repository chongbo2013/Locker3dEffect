package com.locker.theme.scenes3d.newscreen.beans;

import com.badlogic.gdx.utils.Disposable;
import com.locker.theme.scenes3d.newscreen.actions.AbsUnLockAction;

import java.util.ArrayList;
import java.util.List;

import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.equations.Linear;

/**
 * diy解锁配置
 * Created by xff on 2017/12/30.
 */

public class UnLockConfig implements Disposable{
    public String type;
    public float fromX;
    public float fromY;
    public float toX;
    public float toY;
    public float threshold;
    public float speed=3500;
    public float distance=300;
    public float time=0.2f;
    public boolean fbo=false;
    public TweenEquation tweenEquation = Linear.INOUT;
    public float fromAlpha=1;
    public float toAlpha=1;
    public float alphaSpeex=1;
    //解锁关联动画
    public List<AbsUnLockAction> unLockActions=new ArrayList<>();

    @Override
    public void dispose() {
        for(AbsUnLockAction absUnLockAction:unLockActions){
            absUnLockAction.dispose();
        }
        unLockActions.clear();
    }
}
