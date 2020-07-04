package com.locker.theme.scenes3d.newscreen.actors;

import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Disposable;
import com.locker.theme.scenes3d.newscreen.actions.AbsUnLockAction;
import com.locker.theme.scenes3d.newscreen.beans.ActorBean;
import com.locker.theme.scenes3d.newscreen.beans.CustomAccelerometerAnimation;
import com.locker.theme.scenes3d.newscreen.beans.CustomRotateAnimation;
import com.locker.theme.scenes3d.newscreen.beans.IcontinueAction;
import com.locker.theme.scenes3d.newscreen.core.ITween;
import com.locker.theme.scenes3d.newscreen.core.UnLockGestureListener;
import com.locker.theme.scenes3d.newscreen.core.UnLockViewGestureListener;
import com.locker.theme.scenes3d.newscreen.core.UnLockViewGroupGestureListener;
import com.locker.theme.scenes3d.newscreen.event.AnimationEventbean;
import com.locker.theme.scenes3d.newscreen.event.EventBean;
import com.locker.theme.scenes3d.newscreen.event.MessageEventbean;
import com.locker.theme.scenes3d.newscreen.event.OpenEventbean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xff on 2017/12/22.
 */

public abstract class View<T extends ActorBean> extends Widget implements ITween, Disposable {
    public T actonBean;
    public float z = 0;
    public float thickness = 1.0f;
    public float rotationX = 0, rotationY;
    public float scaleZ = 1.0F;
    public float originZ = 0;


    public float getThickness() {
        return thickness;
    }


    /**
     * 重置模型的或者View的各种属性
     */
    public void reset() {
        initAttributes(actonBean);
    }

    protected void initAttributes(T actorBean) {
        if (actorBean == null)
            return;

        setX(actorBean.x);
        setY(actorBean.y);
        setZ(actorBean.z);

        setRotationX(actorBean.rotationX);
        setRotationY(actorBean.rotationY);
        setRotation(actorBean.rotation);

        setScaleX(actorBean.scaleX);
        setScaleY(actorBean.scaleY);
        setScaleZ(actorBean.scaleZ);

        setWidth(actorBean.width);
        setHeight(actorBean.height);
        setThickness(actorBean.thickness);

        setOriginX(actorBean.originX);
        setOriginY(actorBean.originY);
        setOriginZ(actorBean.originZ);

        if(actorBean.color!=null)
        getColor().set(actorBean.color.r, actonBean.color.g, actonBean.color.b, getColor().a);
        //注册事件

        registEvent(actorBean);

    }

    protected List<IcontinueAction> icontinueActions = null;
    UnLockGestureListener flickScrollListener;

    protected void registEvent(T t) {

        if (t.eventAnimation != null) {
            for (EventBean eventBean : t.eventAnimation) {
                switch (eventBean.type) {
                    case EventBean.EVENT_CONTINUE:
                        if (icontinueActions == null)
                            icontinueActions = new ArrayList<>();
                        //触发的是动画
                        if (eventBean instanceof AnimationEventbean) {
                            AnimationEventbean animationEventBean = (AnimationEventbean) eventBean;
                            if (animationEventBean.trigger instanceof IcontinueAction) {
                                IcontinueAction icontinueAction = (IcontinueAction) animationEventBean.trigger;
                                icontinueAction.load();

                                if(!eventBean.isOnce) {
                                    icontinueActions.add(icontinueAction);
                                }
                            }
                        }
                        break;
                    case EventBean.EVENT_CLICK:
                        flickScrollListener = new UnLockViewGestureListener();
                        flickScrollListener.view = this;
                        flickScrollListener.unLockConfig=actonBean.unLockConfig;
                        addListener(flickScrollListener);
                        break;
                    case EventBean.EVENT_LOCK:

                        break;
                    case EventBean.EVENT_UNLOCK:

                        break;
                    case EventBean.EVENT_COMMON:

                        break;
                }
            }
        }
        if (actonBean != null && actonBean.unLockConfig != null && flickScrollListener == null) {
            flickScrollListener = new UnLockViewGestureListener();
            flickScrollListener.view = this;
            flickScrollListener.unLockConfig=actonBean.unLockConfig;
            addListener(flickScrollListener);
        }
    }


    //作用为 混合所有的icontinue的 旋转值
    protected float iRotateX = 0, iRotateY = 0, iRotateZ = 0;

    public void resetIcontiniue() {
        iRotateX = 0;
        iRotateY = 0;
        iRotateZ = 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(flickScrollListener!=null){
            //绘制透明度渐变效果
            flickScrollListener.draw();
        }
        super.draw(batch, parentAlpha);
        if (icontinueActions != null) {
            resetIcontiniue();
            for (int i = 0; i < icontinueActions.size(); i++) {
                IcontinueAction icontinueAction = icontinueActions.get(i);
                icontinueAction.draw(this, Gdx.graphics.getDeltaTime());
                if (icontinueAction instanceof CustomRotateAnimation) {
                    iRotateX += icontinueAction.getRotateX();
                    iRotateY += icontinueAction.getRotateY();
                    iRotateZ += icontinueAction.getRotateZ();
                }else if(icontinueAction instanceof CustomAccelerometerAnimation){
                    iRotateX += icontinueAction.getRotateX();
                    iRotateY += icontinueAction.getRotateY();
                    iRotateZ += icontinueAction.getRotateZ();
                    x+=icontinueAction.getX();
                    y+=icontinueAction.getY();
                    z+=icontinueAction.getZ();
                }
                icontinueAction.checkBound(this);
            }
        }
    }

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    public View() {
    }

    protected abstract void load(T t);

    public void load() {
        load(actonBean);
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public float getOriginZ() {
        return originZ;
    }

    @Override
    public void setOriginZ(float originZ) {
        this.originZ = originZ;
    }

    @Override
    public float getScaleZ() {
        return scaleZ;
    }

    @Override
    public void setScaleZ(float scalez) {
        this.scaleZ = scalez;
    }

    public float getZ() {
        return z;
    }


    @Override
    public void setRotationX(float x) {
        this.rotationX = x;
    }

    @Override
    public float getRotationX() {
        return rotationX;
    }

    @Override
    public void setRotationY(float y) {
        this.rotationY = y;
    }

    @Override
    public float getRotationY() {
        return rotationY;
    }

    @Override
    public void dispose() {
        if (icontinueActions != null)
            icontinueActions.clear();
        icontinueActions = null;

        if (actonBean != null) {
            actonBean.dispose();
        }
        actonBean = null;



    }

    public void doEvents(int eventStrs) {
        if (actonBean != null && actonBean.eventAnimation != null && actonBean.eventAnimation.size() > 0) {
            for (EventBean eventBean : actonBean.eventAnimation) {
                if (eventBean.type == eventStrs) {
                    if (eventBean instanceof AnimationEventbean) {//动画事件
                        AnimationEventbean animationEventBean = (AnimationEventbean) eventBean;
                        animationEventBean.start();
                    } else if (eventBean instanceof MessageEventbean) {//消息
                        MessageEventbean animationEventBean = (MessageEventbean) eventBean;
                        animationEventBean.start();
                    }else if (eventBean instanceof OpenEventbean) {//消息
                        OpenEventbean animationEventBean = (OpenEventbean) eventBean;
                        animationEventBean.start();
                    }
                }

            }
        }
    }

    public void doEvents(String[] eventStrs) {
        if (actonBean != null && actonBean.eventAnimation != null && actonBean.eventAnimation.size() > 0) {
            for (EventBean eventBean : actonBean.eventAnimation) {
                for (String eventName : eventStrs) {
                    if (eventName.equals(eventBean.name)) {//执行
                        if (eventBean instanceof AnimationEventbean) {//动画事件
                            AnimationEventbean animationEventBean = (AnimationEventbean) eventBean;
                            animationEventBean.start();
                        } else if (eventBean instanceof MessageEventbean) {//消息
                            MessageEventbean animationEventBean = (MessageEventbean) eventBean;
                            animationEventBean.start();
                        }else if (eventBean instanceof OpenEventbean) {//消息
                            OpenEventbean animationEventBean = (OpenEventbean) eventBean;
                            animationEventBean.start();
                        }

                    }
                }
            }
        }
    }

    public void findUnLockActions(List<AbsUnLockAction> unLockActions) {
        if(actonBean==null)
            return;
        if(TextUtils.isEmpty(actonBean.name))
            return;
        for(AbsUnLockAction absUnLockAction:unLockActions){
            if(actonBean.name.equals(absUnLockAction.target)){
                absUnLockAction.actor=this;
            }
        }
    }
}
