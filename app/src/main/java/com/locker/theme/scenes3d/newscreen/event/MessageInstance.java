package com.locker.theme.scenes3d.newscreen.event;

import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息管理
 * Created by xff on 2017/12/29.
 */

public class MessageInstance implements Disposable{
    public static MessageInstance instance;
    public static MessageInstance get(){
        if(instance==null){
            synchronized (MessageInstance.class){
                instance=new MessageInstance();
            }
        }
        return instance;
    }
    //接收和发送
    public List<MessageBean>  messageBeans=new ArrayList<>();

    public <T> T getMessage(String trigger,Class<T> classType) {
        for(MessageBean sendEvent:messageBeans){
            if(ISendEvent.class.isAssignableFrom(classType) ){
                if(sendEvent instanceof ISendEvent) {
                    if (sendEvent.getName().equals(trigger)) {
                        return (T) sendEvent;
                    }
                }
            }else{
                if(sendEvent instanceof IReceiveEvent) {
                    if (sendEvent.getName().equals(trigger)) {
                        return (T) sendEvent;
                    }
                }
            }

        }
        return null;
    }
    public void putMessage(MessageBean event){
        synchronized (messageBeans) {
            if (!messageBeans.contains(event)) {
                messageBeans.add(event);
            }
        }
    }
    //发送消息
    public void sendMessage(String name){
        synchronized (messageBeans) {
            for (MessageBean iSendEvent : messageBeans) {
                if (iSendEvent instanceof IReceiveEvent) {
                    IReceiveEvent iSendEvent1 = (IReceiveEvent) iSendEvent;
                    iSendEvent1.receiveEvent(name);
                }
            }
        }
    }

    @Override
    public void dispose() {
        messageBeans.clear();
    }
}
