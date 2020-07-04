package com.locker.theme.scenes3d;


import android.content.Intent;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.SurfaceView;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.IHost;
import com.badlogic.gdx.backends.android.TimeInfo;

import java.io.File;


/**
 * 调试3d场景
 * Created by xff on 2017-4-18 14:42:48
 */

public class Cube3dActivity extends AndroidApplication implements IHost {

    static final boolean isUseMoni=false;
    Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Scenes3DCore.isDebug=true;
        if(!isUseMoni) {
            Scenes3DCore scenes3DCore = new Scenes3DCore();
            scenes3DCore.hostContext = this;
            TimeInfo info = new TimeInfo();
            info.hour = 12;
            info.minute = 34;
            info.year = 2017;
            info.month = 12;
            info.dayOfMonth = 11;
            info.dayOfWeek = 1;
            info.temperature = "22º";
            info.weather = "晴";
            scenes3DCore.pluginEvent("updateTime", info);

            AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
            cfg.r = cfg.g = cfg.b = cfg.a = 8;

            //通过libGDX提供的initializeForView方法得到android framework层上的绘制层view
            View view = initializeForView(scenes3DCore, cfg);
            setContentView(view);
            //绘制置顶、透明设置
            if (view instanceof SurfaceView) {
                GLSurfaceView glView = (GLSurfaceView) graphics.getView();
                glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
                glView.setZOrderMediaOverlay(true);
                glView.setZOrderOnTop(true);
            }



        }else{

            Scenes3DCore scenes3DCore=new Scenes3DCore();
            scenes3DCore.hostContext=this;
            scenes3DCore.setApplication(getApplication());
            scenes3DCore.setHost(this);
            scenes3DCore.setFixPath(Environment.getExternalStorageDirectory() + File.separator + "droid4xShare/assets");

            AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
            cfg.r = cfg.g = cfg.b = cfg.a = 8;

            //通过libGDX提供的initializeForView方法得到android framework层上的绘制层view
            View view = initializeForView(scenes3DCore, cfg);
            //绘制置顶、透明设置
            if (view instanceof SurfaceView) {
                GLSurfaceView glView = (GLSurfaceView) graphics.getView();
                glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
                glView.setZOrderMediaOverlay(true);
                glView.setZOrderOnTop(true);
            }
            setContentView(view);
            TimeInfo info = new TimeInfo();
            info.hour = 12;
            info.minute = 34;
            info.year = 2017;
            info.month = 12;
            info.dayOfMonth = 11;
            info.dayOfWeek = 1;
            info.temperature = "22º";
            info.weather = "晴";
            scenes3DCore.pluginEvent("updateTime",info);
        }




    }


    @Override
    public boolean hostEvent(Object... objects) {
        return false;
    }

    @Override
    public boolean hostIntentEvent(Intent intent) {
        return false;
    }

    @Override
    public boolean unLocker(long l) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, l);
        return false;
    }
}
