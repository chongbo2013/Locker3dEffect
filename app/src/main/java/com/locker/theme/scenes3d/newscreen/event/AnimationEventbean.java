package com.locker.theme.scenes3d.newscreen.event;

import com.badlogic.gdx.utils.Timer;
import com.locker.theme.scenes3d.newscreen.beans.CustomAccelerometerAnimation;
import com.locker.theme.scenes3d.newscreen.beans.CustomRotateAnimation;
import com.locker.theme.scenes3d.newscreen.core.TweenAction;
import com.locker.theme.scenes3d.newscreen.tween.BaseParserTween;

/**
 * Created by xff on 2017/12/28.
 */

public class AnimationEventbean extends EventBean<BaseParserTween> {
    //响应的本身事件或者全局的事件
//     <!--extra 可选 Global 屏幕的任意地方，默认为Local点击控件自己,Parent 点击父控件-->
    public String extra="Local";

    public void start() {
        if(delay!=-1){
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    if(trigger!=null) {
                        if (!(trigger instanceof CustomRotateAnimation)&&!(trigger instanceof CustomAccelerometerAnimation)) {
                            trigger.build().start(TweenAction.manager);
                        }
                    }
                }
            },delay);
        }else{
            if(trigger!=null) {
                if (!(trigger instanceof CustomRotateAnimation)&&!(trigger instanceof CustomAccelerometerAnimation)) {
                    trigger.build().start(TweenAction.manager);
                }
            }
        }

    }
}
