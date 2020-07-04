package com.locker.theme.scenes3d.newscreen.actions;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by x002 on 2018/9/13.
 */

public abstract class AbsUnLockAction implements Disposable {
    //操作目标
    public Actor actor;
    public String target;
    public String param1;
    public String param2;
    public abstract void update(float radio);

    @Override
    public void dispose() {
        actor=null;
    }
}
