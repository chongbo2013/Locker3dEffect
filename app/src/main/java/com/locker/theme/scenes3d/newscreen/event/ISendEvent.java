package com.locker.theme.scenes3d.newscreen.event;

/**
 * 如果有对象实现了这个接口，代表这个对象，可以发送事件
 * Created by xff on 2017/12/27.
 */

public interface ISendEvent {


    void init();

//    啥时候需要发送事件？click后，需要发送事件， event,send,obj....
    //需要发送消息吗
    boolean needSendMessage();
    //发送消息
    void sendEvent();

    String getName();
}
