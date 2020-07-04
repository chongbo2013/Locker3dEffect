package com.locker.theme.scenes3d.newscreen.event;

import android.text.TextUtils;

import com.locker.theme.scenes3d.Scenes3DCore;
import com.locker.theme.scenes3d.newscreen.actors.ViewGroup;

import java.util.ArrayList;

/**
 * 发送消息
 * Created by xff on 2017/12/29.
 */

public class SendMessageBean extends MessageBean implements ISendEvent{
    @Override
    public void init() {

    }

    @Override
    public boolean needSendMessage() {
        return true;
    }

    @Override
    public void sendEvent() {
        if(needSendMessage()){
            //遍历 获取所有的接收者，
            MessageInstance.get().sendMessage(getName());

            if(TextUtils.isEmpty(things))
                return;

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

}
