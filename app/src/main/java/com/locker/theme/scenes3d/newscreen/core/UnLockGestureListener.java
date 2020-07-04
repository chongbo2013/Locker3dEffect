package com.locker.theme.scenes3d.newscreen.core;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.locker.theme.scenes3d.newscreen.beans.UnLockConfig;

/**
 *
 * Created by xff on 2017/12/30.
 */

public abstract class UnLockGestureListener<T extends Actor> extends ActorGestureListener {
   public UnLockConfig unLockConfig;
   public T view;
   public abstract void draw();
}
