package com.locker.theme.scenes3d.newscreen.event;

/**
 *  如果该对象实现了 该接口，代表该对象，可以收到任何的事件
 * Created by xff on 2017/12/27.
 */

public interface IReceiveEvent {
    /**
     *
     * @param event
     * @param obj
     */
    void receiveEvent(String event,Object... obj);
    //收到事件后，需要做啥事前？ event,doEvent

    void init();
}
