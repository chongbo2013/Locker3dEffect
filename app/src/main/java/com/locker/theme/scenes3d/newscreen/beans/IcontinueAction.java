package com.locker.theme.scenes3d.newscreen.beans;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.locker.theme.scenes3d.newscreen.actors.ViewGroup;

/**
 * Created by xff on 2017/12/25.
 */

public interface IcontinueAction <T extends Actor> {
     void   draw(T t,float time);

     void load();

      float getRotateX();

      float getRotateY();
      float getRotateZ();

     void checkBound(T t);

    float getX();

    float getY();

    float getZ();
}
