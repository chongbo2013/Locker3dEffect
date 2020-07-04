package com.locker.theme.scenes3d.newscreen.actors;

import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Disposable;
import com.locker.theme.scenes3d.newscreen.actions.AbsUnLockAction;
import com.locker.theme.scenes3d.newscreen.beans.ActorBean;
import com.locker.theme.scenes3d.newscreen.beans.CustomAccelerometerAnimation;
import com.locker.theme.scenes3d.newscreen.beans.CustomRotateAnimation;
import com.locker.theme.scenes3d.newscreen.beans.IcontinueAction;
import com.locker.theme.scenes3d.newscreen.core.ITween;
import com.locker.theme.scenes3d.newscreen.core.TweenAction;
import com.locker.theme.scenes3d.newscreen.core.UnLockViewGroupGestureListener;
import com.locker.theme.scenes3d.newscreen.event.AnimationEventbean;
import com.locker.theme.scenes3d.newscreen.event.EventBean;
import com.locker.theme.scenes3d.newscreen.event.MessageEventbean;
import com.locker.theme.scenes3d.newscreen.event.OpenEventbean;

import java.util.ArrayList;
import java.util.List;

/**
 *  容器
 * Created by xff on 2017/12/22.
 */

public abstract class ViewGroup<T extends ActorBean> extends WidgetGroup implements ITween,Disposable {
    public T actonBean;

    public float thickness = 1.0f;
    protected float originZ=0;
    protected List<IcontinueAction> icontinueActions = null;
    //作用为 混合所有的icontinue的 旋转值
    protected float iRotateX = 0, iRotateY = 0, iRotateZ = 0;
    float z=0;
    float rotationX=0,rotationY;
    float scaleZ=0;
    UnLockViewGroupGestureListener flickScrollListener;
    public ViewGroup(){

    }

    protected abstract void load(T t);

    public void setThickness(float thickness) {
        this.thickness = thickness;
    }

    @Override
    public float getScaleZ() {
        return scaleZ;
    }

    @Override
    public void setScaleZ(float scalez) {
        this.scaleZ=scaleZ;
    }

    public float getZ(){
        return z;
    }

    public void setZ(float z){
        this.z=z;
    }

    @Override
    public float getOriginZ() {
        return originZ;
    }

    @Override
    public void setOriginZ(float originZ) {
        this.originZ=originZ;
    }

    @Override
    public float getRotationX() {
        return rotationX;
    }

    @Override
    public void setRotationX(float x) {
        this.rotationX=x;
    }

    @Override
    public float getRotationY() {
        return rotationY;
    }

    @Override
    public void setRotationY(float y) {
        this.rotationY=y;
    }

    @Override
    public void dispose() {
        int size=getChildren().size;
        for(int i=0;i<size;i++){
            Actor actor=getChildren().get(i);
            if(actor instanceof View){
                ((View)actor).dispose();
            }else if(actor instanceof ViewGroup){
                ((ViewGroup)actor).dispose();
            }
        }


        if (icontinueActions != null)
            icontinueActions.clear();
        icontinueActions = null;

        if (actonBean != null) {
            actonBean.dispose();
        }
        actonBean = null;
    }

    public void load() {
        load(actonBean);
        int size=getChildren().size;
        for(int i=0;i<size;i++){
            Actor actor=getChildren().get(i);
            if(actor instanceof View){
                ((View)actor).load();
            }else if(actor instanceof ViewGroup){
                ((ViewGroup)actor).load();
            }
        }
    }

    /**
     * 重置模型的或者View的各种属性
     */
    public void reset() {
        initAttributes(actonBean);
    }

    protected void initAttributes(T actorBean) {
        if(actorBean==null)
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
        getColor().set(actorBean.color.r, actonBean.color.g, actonBean.color.b, color.a);
        //注册事件

        registEvent(actorBean);
    }

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
                                icontinueActions.add(icontinueAction);
                            }
                        }
                        break;
                    case EventBean.EVENT_CLICK:
                        flickScrollListener = new UnLockViewGroupGestureListener();
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

        if(actonBean!=null&&actonBean.unLockConfig!=null&&flickScrollListener==null){
            flickScrollListener = new UnLockViewGroupGestureListener();
            flickScrollListener.view=this;
            flickScrollListener.unLockConfig=actonBean.unLockConfig;
            addListener(flickScrollListener);
        }
    }

    public void resetIcontiniue(){
        iRotateX = 0;
        iRotateY = 0;
        iRotateZ = 0;
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(flickScrollListener!=null){
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

    public void doEvents(String[] eventStrs) {
        doEventsInternal(eventStrs);
        int size=getChildren().size;
        for(int i=0;i<size;i++){
            Actor actor=getChildren().get(i);
            if(actor instanceof View){
                ((View)actor).doEvents(eventStrs);
            }else if(actor instanceof ViewGroup){
                ((ViewGroup)actor).doEvents(eventStrs);
            }
        }
    }

    public void doEvents(int eventStrs) {
        doEventsInternal(eventStrs);
        int size=getChildren().size;
        for(int i=0;i<size;i++){
            Actor actor=getChildren().get(i);
            if(actor instanceof View){
                ((View)actor).doEvents(eventStrs);
            }else if(actor instanceof ViewGroup){
                ((ViewGroup)actor).doEvents(eventStrs);
            }
        }
    }

    void doEventsInternal(int eventStrs) {
        if (actonBean != null && actonBean.eventAnimation != null && actonBean.eventAnimation.size() > 0) {
            for (EventBean eventBean : actonBean.eventAnimation) {
                if(eventBean.type==eventStrs){
                    if (eventBean instanceof AnimationEventbean) {//动画事件
                        AnimationEventbean animationEventBean = (AnimationEventbean) eventBean;
                        animationEventBean.start();
                    }  else if (eventBean instanceof MessageEventbean) {//消息
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
     void doEventsInternal(String[] eventStrs) {
        if (actonBean != null && actonBean.eventAnimation != null && actonBean.eventAnimation.size() > 0) {
            for (EventBean eventBean : actonBean.eventAnimation) {
                for (String eventName : eventStrs) {
                    if (eventName.equals(eventBean.name)) {//执行
                        if (eventBean instanceof AnimationEventbean) {//动画事件
                            AnimationEventbean animationEventBean = (AnimationEventbean) eventBean;
                            animationEventBean.start();
                        }  else if (eventBean instanceof MessageEventbean) {//消息
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
        findUnLockActionsViewGroup(unLockActions);
        int size=getChildren().size;
        for(int i=0;i<size;i++){
            Actor actor=getChildren().get(i);
            if(actor instanceof ViewGroup){
                ((ViewGroup)actor).findUnLockActions(unLockActions);
            }else  if(actor instanceof View){
                ((View)actor).findUnLockActions(unLockActions);
            }
        }
    }

    public void findUnLockActionsViewGroup(List<AbsUnLockAction> unLockActions) {
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
