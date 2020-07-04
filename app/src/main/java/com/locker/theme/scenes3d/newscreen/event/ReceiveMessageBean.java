package com.locker.theme.scenes3d.newscreen.event;

import com.locker.theme.scenes3d.Scenes3DCore;
import com.locker.theme.scenes3d.newscreen.actors.ViewGroup;

import java.util.ArrayList;

/**
 * 接收消息的模型
 * Created by xff on 2017/12/29.
 */

public class ReceiveMessageBean extends MessageBean implements IReceiveEvent {

    //初始化
    @Override
    public void init() {

    }
    @Override
    public void receiveEvent(String eventId, Object... obj) {
        if (needReceiveMessage(eventId)) {
            //允许接收消息了
            String[] eventStrs=things.split(",");
            if(eventStrs!=null) {
                ArrayList<ViewGroup> viewGroups = Scenes3DCore.scenes3DCore.viewGroups;
                if (viewGroups != null) {
                    for (ViewGroup viewGroup : viewGroups) {
                        viewGroup.doEvents(eventStrs);
                    }
                }
            }

        }
    }

    //通过  whenfunction 判别是否需要 接收消息
    public boolean needReceiveMessage(String eventId) {
        return true;
    }

}
