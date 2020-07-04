package com.locker.theme.scenes3d.newscreen.actions;

/**
 * Created by xff on 2018/9/13.
 */

public class AlphaUnLockAction extends AbsUnLockAction {

    @Override
    public void update(float radio) {
        if(actor==null)
            return;
        actor.getColor().a=radio;
    }


}
