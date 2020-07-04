package com.locker.theme.scenes3d.newscreen.event;

import com.locker.theme.scenes3d.newscreen.core.ITween;

/**
 * Created by x002 on 2017/12/29.
 */

public abstract class MessageBean {
    //事件的名称，唯一
    public String eventId;
    //什么时候才允许接收
    public String whenFunction;
    //接收后做什么事情
    public String things;
    //view 角色引用
    public ITween view;

    public abstract void init();


    public String getName() {
        return eventId;
    }
}
