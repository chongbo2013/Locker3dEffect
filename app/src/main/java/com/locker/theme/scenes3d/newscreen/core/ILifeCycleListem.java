package com.locker.theme.scenes3d.newscreen.core;

import com.badlogic.gdx.backends.android.TimeInfo;

/**
 * 生命周期监听
 * Created by xff on 2017/12/27.
 */

public interface ILifeCycleListem {
     void dispose();
     void pause ();
     void resume ();
     void create();

     void updateTime(TimeInfo timeInfo);
     void msgUpdate(int msg);
}
