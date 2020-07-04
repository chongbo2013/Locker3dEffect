package com.locker.theme.scenes3d.newscreen.event;

import com.badlogic.gdx.utils.Timer;

/**
 * 消息事件，发送消息
 * Created by xff on 2017/12/28.
 */

public class MessageEventbean extends EventBean<String> {

    public void start() {
        if(delay!=-1){
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    if(trigger!=null) {
                        ISendEvent sendMessageBean =MessageInstance.get().getMessage(trigger,ISendEvent.class);
                        if(sendMessageBean!=null){
                            sendMessageBean.sendEvent();
                        }
                    }
                }
            },delay);
        }else{
            if(trigger!=null) {
                ISendEvent sendMessageBean =MessageInstance.get().getMessage(trigger,ISendEvent.class);
                if(sendMessageBean!=null){
                    sendMessageBean.sendEvent();
                }
            }
        }

    }
}
