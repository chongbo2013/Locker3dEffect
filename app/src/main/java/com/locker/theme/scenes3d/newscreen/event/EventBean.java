package com.locker.theme.scenes3d.newscreen.event;

/**
 * Created by xff on 2017/12/28.
 */

public  abstract class EventBean<T>{
    //持续性动画会在 draw中一直绘制
    public static final int EVENT_CONTINUE=0;
    //点击控件触发
    public static final int EVENT_CLICK=1;
    //锁屏的时候触发
    public static final int EVENT_LOCK=2;
    //解锁的时候触发
    public static final int EVENT_UNLOCK=3;
    //普通的事件
    public static final int EVENT_COMMON=4;
    //触摸拖拽等触发
    public static final int EVENT_TOUCH=5;
    //密码页面显示
    public static final int EVENT_PSWD_SHOW=6;
    //密码页面隐藏
    public static final int EVENT_PSWD_HIDE=7;
    //来新消息了
    public static final int EVENT_NEW_MSG=8;



    //触摸解锁控件触发
    public static final int EVENT_UNLOCK_TOUCH_DOWN=9;
    //进入解锁区域
    public static final int EVENT_UNLOCK_DRAG_ENTER=10;
    //退出解锁区域
    public static final int EVENT_UNLOCK_DRAG_EXIT=11;
    //手指抬起
    public static final int EVENT_UNLOCK_TOUCH_UP=12;

    //没有消息了
    public static final int EVENT_NO_MSG=13;

    //事件的类型 点击，解锁，等等等
    public int type;
    //事件的名称
    public String name;
    //事件触发器 点击事件，等等，又可以是事件本身
    public T trigger;
    //延时执行
    public  float delay=-1;
    //执行事件
    public abstract void start();
    //是否只执行一次
    public boolean isOnce=false;
}
