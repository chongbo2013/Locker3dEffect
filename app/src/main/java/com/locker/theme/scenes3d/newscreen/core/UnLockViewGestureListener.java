package com.locker.theme.scenes3d.newscreen.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.locker.theme.scenes3d.Scenes3DCore;
import com.locker.theme.scenes3d.newscreen.actions.AbsUnLockAction;
import com.locker.theme.scenes3d.newscreen.actors.View;
import com.locker.theme.scenes3d.newscreen.event.AnimationEventbean;
import com.locker.theme.scenes3d.newscreen.event.EventBean;
import com.locker.theme.scenes3d.newscreen.event.MessageEventbean;
import com.locker.theme.scenes3d.newscreen.event.OpenEventbean;

import java.util.List;

import aurelienribon.tweenengine.Tween;

/**
 *
 * Created by xff on 2017/12/30.
 */

public  class UnLockViewGestureListener extends UnLockGestureListener<View> {
   float vx = 0, vy = 0;
   boolean slideV = true;
   float totalX = 0, totalY = 0;

   boolean isEnter = false;

   public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
      if (view != null && unLockConfig != null) {
         totalX += deltaX;
         totalY += deltaY;
         if (slideV && Math.abs(totalY) > Math.abs(totalX)) {
            float px = view.getX();
            if(unLockConfig.fromY<unLockConfig.toY) {
               float py = MathUtils.clamp(view.getY() + deltaY, unLockConfig.fromY, unLockConfig.toY);
               view.setPosition(px, py);
            }else{
               float py = MathUtils.clamp(view.getY() + deltaY, unLockConfig.toY, unLockConfig.fromY);
               view.setPosition(px, py);
            }
         } else if (Math.abs(totalX) > Math.abs(totalY)) {
            float py = view.getY();
            if(unLockConfig.fromX<unLockConfig.toX) {
               float px = MathUtils.clamp(view.getX() + deltaX, unLockConfig.fromX, unLockConfig.toX);
               view.setPosition(px, py);
            }else{
               float px = MathUtils.clamp(view.getX() + deltaX, unLockConfig.toX, unLockConfig.fromX);
               view.setPosition(px, py);
            }
         }


         //竖向滑动
         if (slideV) {

            boolean isSwipeDown=unLockConfig.fromY>unLockConfig.toY;
            boolean threshould=isSwipeDown?view.getY()<unLockConfig.threshold:view.getY() > unLockConfig.threshold;
            if (!isEnter&&threshould) {
               isEnter=true;
               Scenes3DCore.scenes3DCore.dispatchEvent(EventBean.EVENT_UNLOCK_DRAG_ENTER);
            } else if (isEnter&&threshould) {
               isEnter=false;
               Scenes3DCore.scenes3DCore.dispatchEvent(EventBean.EVENT_UNLOCK_DRAG_EXIT);
            }
         } else {
            boolean isSwipeDown=unLockConfig.fromX>unLockConfig.toX;
            boolean threshould=isSwipeDown?view.getX()<unLockConfig.threshold:view.getX() > unLockConfig.threshold;
            if (!isEnter&&threshould) {
               isEnter=true;
               Scenes3DCore.scenes3DCore.dispatchEvent(EventBean.EVENT_UNLOCK_DRAG_ENTER);
            } else if (isEnter&&threshould) {
               isEnter=false;
               Scenes3DCore.scenes3DCore.dispatchEvent(EventBean.EVENT_UNLOCK_DRAG_EXIT);
            }
         }


      }
   }

   //vy 上正 下负  vx右正 左负
   public void fling(InputEvent event, float vx2, float vy2, int button) {
      this.vx = vx2;
      this.vy = vy2;
   }

   @Override
   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
      if (pointer > 0)
         return;

      if (view != null && unLockConfig != null) {
         //垂直滑动


         if (slideV) {
            //有速度并且滑动足够距离也可以解锁
            //下滑解锁
            boolean isSwipeDown=unLockConfig.fromY>unLockConfig.toY;
            boolean threshould=isSwipeDown?view.getY()<unLockConfig.threshold:view.getY() > unLockConfig.threshold;
            boolean hasSpeed=isSwipeDown?vy <- unLockConfig.speed:vy>unLockConfig.speed;
            boolean hasDis=Math.abs(totalY) > unLockConfig.distance;
            if (threshould ||hasSpeed && hasDis) {

               //如果有密码
               if (Scenes3DCore.isPswd) {//回到原来位置
                  Tween.to(view, TweenAction.POS_Y, unLockConfig.time).target(unLockConfig.fromY).start(TweenAction.manager);
               } else {
                  //解锁
                  Tween.to(view, TweenAction.POS_Y, unLockConfig.time).target(unLockConfig.toY).start(TweenAction.manager);
               }

               Scenes3DCore.scenes3DCore.unLocker(Scenes3DCore.unLockDelay);
            } else {//滚回到原来位置
               Tween.to(view, TweenAction.POS_Y, unLockConfig.time).target(unLockConfig.fromY).start(TweenAction.manager);
            }
         } else {
            boolean isSwipeDown=unLockConfig.fromX>unLockConfig.toX;
            boolean threshould=isSwipeDown?view.getX()<unLockConfig.threshold:view.getX() > unLockConfig.threshold;
            boolean hasSpeed=isSwipeDown?vx <- unLockConfig.speed:vx>unLockConfig.speed;
            boolean hasDis=Math.abs(totalX) > unLockConfig.distance;
            if (threshould ||hasSpeed && hasDis) {
               //如果有密码
               if (Scenes3DCore.isPswd) {
                  Tween.to(view, TweenAction.POS_X, unLockConfig.time).target(unLockConfig.fromX).start(TweenAction.manager);
               } else {
                  Tween.to(view, TweenAction.POS_X, unLockConfig.time).target(unLockConfig.toX).start(TweenAction.manager);
               }

               Scenes3DCore.scenes3DCore.unLocker(Scenes3DCore.unLockDelay);
            } else {//滚回到原来位置
               Tween.to(view, TweenAction.POS_X, unLockConfig.time).target(unLockConfig.fromX).start(TweenAction.manager);
            }
         }

         Scenes3DCore.scenes3DCore.dispatchEvent(EventBean.EVENT_UNLOCK_TOUCH_UP);
         totalX = 0;
         totalY = 0;
         vx = 0;
         vy = 0;
      }
   }

   @Override
   public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
      if (pointer > 0)
         return;
      isEnter = false;
      totalX = 0;
      totalY = 0;
      vx = 0;
      vy = 0;
      if (view != null && unLockConfig != null) {
         slideV = Math.abs(unLockConfig.fromX - unLockConfig.toX) < Math.abs(unLockConfig.fromY - unLockConfig.toY);
      }

      Scenes3DCore.scenes3DCore.dispatchEvent(EventBean.EVENT_UNLOCK_TOUCH_DOWN);
   }

   @Override
   public void tap(InputEvent event, float x, float y, int count, int button) {
      super.tap(event, x, y, count, button);

      if (view != null && view.actonBean != null && view.actonBean.eventAnimation != null) {
         List<EventBean> events = view.actonBean.eventAnimation;
         for (EventBean eventBean : events) {
            if (eventBean.type == EventBean.EVENT_CLICK) {
               if (eventBean instanceof AnimationEventbean) {//动画事件
                  AnimationEventbean animationEventBean = (AnimationEventbean) eventBean;
                  animationEventBean.start();
               } else if (eventBean instanceof MessageEventbean) {//消息
                  MessageEventbean animationEventBean = (MessageEventbean) eventBean;
                  animationEventBean.start();
               } else if (eventBean instanceof OpenEventbean) {//消息
                  OpenEventbean animationEventBean = (OpenEventbean) eventBean;
                  animationEventBean.start();
               }
            }
         }
      }
   }

   @Override
   public void draw() {
      if(unLockConfig==null&&view!=null)
         return;
      float alpha;
      slideV = Math.abs(unLockConfig.fromX - unLockConfig.toX) < Math.abs(unLockConfig.fromY - unLockConfig.toY);
      if(slideV){
         boolean isSwipeDown=unLockConfig.fromY>unLockConfig.toY;
         if(isSwipeDown){
             alpha=unLockConfig.fromAlpha-(1f-(view.getY()-unLockConfig.toY)/(unLockConfig.fromY-unLockConfig.toY))*(unLockConfig.fromAlpha-unLockConfig.toAlpha)*unLockConfig.alphaSpeex;
             alpha=MathUtils.clamp(alpha,0,1);
             view.getColor().a=alpha;
         }else{
             alpha=unLockConfig.fromAlpha-(view.getY()-unLockConfig.fromY)/(unLockConfig.toY-unLockConfig.fromY)*(unLockConfig.fromAlpha-unLockConfig.toAlpha)*unLockConfig.alphaSpeex;
             alpha=MathUtils.clamp(alpha,0,1);
             view.getColor().a=alpha;
         }
      }else{
         boolean isSwipeDown=unLockConfig.fromX>unLockConfig.toX;
         if(isSwipeDown){
             alpha=unLockConfig.fromAlpha-(1f-(view.getX()-unLockConfig.toX)/(unLockConfig.fromX-unLockConfig.toX))*(unLockConfig.fromAlpha-unLockConfig.toAlpha)*unLockConfig.alphaSpeex;
            alpha=MathUtils.clamp(alpha,0,1);
             view.getColor().a=alpha;
         }else{
            alpha=unLockConfig.fromAlpha-(view.getX()-unLockConfig.fromX)/(unLockConfig.toX-unLockConfig.fromX)*(unLockConfig.fromAlpha-unLockConfig.toAlpha)*unLockConfig.alphaSpeex;
            alpha=MathUtils.clamp(alpha,0,1);
            view.getColor().a=alpha;
         }
      }

      if(unLockConfig.unLockActions.size()>0){
         initLockActions();
         for(AbsUnLockAction absUnLockAction:unLockConfig.unLockActions){
            absUnLockAction.update(alpha);
         }
      }
   }
   boolean isInitActions=false;
    void initLockActions(){
       if(isInitActions)
          return;
       isInitActions=true;
       Scenes3DCore.scenes3DCore.findUnLockActions(unLockConfig.unLockActions);
   }
}
