package com.locker.theme.scenes3d.newscreen.event;

import com.badlogic.gdx.utils.Timer;
import com.locker.theme.scenes3d.Scenes3DCore;

/**
 * 打开消息等
 * Created by xff on 2017/12/28.
 */

public class OpenEventbean extends EventBean<String> {

    public void start() {
        if(delay!=-1){
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    if(trigger!=null) {
                       switch (trigger){
                           case "open_msg":
                               Scenes3DCore.scenes3DCore.clickMsg();
                               break;
                       }
                    }
                }
            },delay);
        }else{
            if(trigger!=null) {
                if(trigger!=null) {
                    switch (trigger){
                        case "open_msg":
                            Scenes3DCore.scenes3DCore.clickMsg();
                            break;
                    }
                }
            }
        }

    }
}
