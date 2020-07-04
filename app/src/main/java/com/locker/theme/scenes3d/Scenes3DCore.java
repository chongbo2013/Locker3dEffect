package com.locker.theme.scenes3d;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.backends.android.IHost;
import com.badlogic.gdx.backends.android.IMainListem;
import com.badlogic.gdx.backends.android.IPlugin;
import com.badlogic.gdx.backends.android.TimeInfo;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.locker.theme.scenes3d.newscreen.XmlParserUtils;
import com.locker.theme.scenes3d.newscreen.actions.AbsUnLockAction;
import com.locker.theme.scenes3d.newscreen.actors.ViewGroup;
import com.locker.theme.scenes3d.newscreen.core.CustomPolygonSpriteBatch;
import com.locker.theme.scenes3d.newscreen.core.CustomStage;
import com.locker.theme.scenes3d.newscreen.core.FontManage;
import com.locker.theme.scenes3d.newscreen.core.ILifeCycleListem;
import com.locker.theme.scenes3d.newscreen.core.TweenAction;
import com.locker.theme.scenes3d.newscreen.event.EventBean;
import com.locker.theme.scenes3d.newscreen.event.MessageInstance;
import com.locker.theme.scenes3d.newscreen.lib.TextImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aurelienribon.tweenengine.Tween;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;

/**
 * diy主题
 * 2017-12-28 16:46:11 by ferris.xu
 */
public class Scenes3DCore implements ApplicationListener, IPlugin {
    final static String vertexShader = "attribute vec4 a_position;\n" +
            "attribute vec4 a_color;\n" +
            "attribute vec2 a_texCoord0;\n" +
            "uniform mat4 u_projTrans;\n" +
            "varying vec4 v_color;\n" +
            "varying vec2 v_texCoords;\n" +
            "\n" +
            "void main(){\n" +
            "   v_color = a_color;\n" +
            "   v_color.a = v_color.a * (255.0/254.0);\n" +
            "   v_texCoords = a_texCoord0;\n" +
            "   gl_Position =  u_projTrans * a_position;\n" +
            "}";
    final static String fragmentShader = "#ifdef GL_ES\n" +
            "#define LOWP lowp\n" +
            "precision mediump float;\n" +
            "#else\n" +
            "#define LOWP\n" +
            "#endif\n" +
            "varying LOWP vec4 v_color;\n" +
            "varying vec2 v_texCoords;\n" +
            "uniform sampler2D u_texture;\n" +
            "uniform LOWP float radio;\n" +
            "uniform LOWP float normalstutas;\n" +
            "void main ()\n" +
            "{\n" +
            "            //0-1 radio\n" +
            "            vec4 temp = texture2D(u_texture, v_texCoords);\n" +
            "\n" +
            "            if (normalstutas < 1.0) {\n" +
            "\n" +
            "                if (v_texCoords.x <= 0.5) {\n" +
            "                    //0-0.5\n" +
            "                    float lenght = 0.5 / 1.0;\n" +
            "                    float moveStep = (0.5 + lenght) * radio;\n" +
            "                    float start = 0.5 - moveStep;\n" +
            "                    float end = start + lenght;\n" +
            "                    if (v_texCoords.x >= end) {\n" +
            "                        temp.a = temp.a * 1.0;\n" +
            "                    } else if (v_texCoords.x >= start && v_texCoords.x < end) {\n" +
            "                        float smootf = smoothstep(start, end, v_texCoords.x);\n" +
            "                        temp.a = temp.a * smootf;\n" +
            "                    } else {\n" +
            "                        temp.a = temp.a * 0.0;\n" +
            "                    }\n" +
            "\n" +
            "                } else {\n" +
            "                    float lenght = 0.5 / 1.0;\n" +
            "                    float moveStep = (0.5 + lenght) * radio;\n" +
            "                    float end = 0.5 + moveStep;\n" +
            "                    float start = end - lenght;\n" +
            "                    if (v_texCoords.x <= start) {\n" +
            "                        temp.a = temp.a * 1.0;\n" +
            "                    } else if (v_texCoords.x > start && v_texCoords.x <= end) {\n" +
            "                        float smootf = 1.0 - smoothstep(start, end, v_texCoords.x);\n" +
            "                        temp.a = temp.a * smootf;\n" +
            "                    } else {\n" +
            "                        temp.a = temp.a * 0.0;\n" +
            "                    }\n" +
            "\n" +
            "                }\n" +
            "            }\n" +
            "            gl_FragColor = v_color * temp;\n" +
            "  gl_FragColor.rgb*=gl_FragColor.a;\n" +
            "}";
    public static boolean isDebug = false;

    public static Scenes3DCore scenes3DCore;
    //延迟多秒解锁
    public static long unLockDelay = 500l;

    CustomPolygonSpriteBatch batch;
    CustomStage stage;
    public Context hostContext;
    public String fixPath;
    public IHost iHost;
    public boolean isCantouch = true;
    public boolean isDisableTouch = false;
    //debug
    IMainListem iMainListem;
    ArrayList<Runnable> mBindOnResumeCallbacks = new ArrayList<>();
    private boolean mPaused = true;
    ShaderProgram shader;
    //资源加载
    public AssetManager assets;
    public ArrayList<ViewGroup> viewGroups;

    public List<ILifeCycleListem> lifeCycleListems = new ArrayList<>();
    private BitmapFont font;

    public Scenes3DCore() {

    }

    @Override
    public void create() {
        scenes3DCore = this;
        MessageInstance.get().dispose();
        FontManage.get().dispose();
        if (iHost == null) {
            assets = new AssetManager();
        } else {
            assets = new AssetManager(new AbsoluteFileHandleResolver());
        }
        Texture.setAssetManager(assets);
        Cubemap.setAssetManager(assets);

        Tween.registerAccessor(Actor.class, new TweenAction());
        shader = new ShaderProgram(vertexShader, fragmentShader);
        ShaderProgram.pedantic = false;
        batch = new CustomPolygonSpriteBatch(1000, shader);
        stage = new CustomStage(new ScreenViewport(), batch);
        final String path = getFileString("theme_defalut.xml");

        viewGroups = XmlParserUtils.getDiyThemesFromXml(path);



        for (ViewGroup viewGroup : viewGroups) {
            viewGroup.load();
        }

        //添加壁纸层
        if (viewGroups.size() >= 1)
            stage.getRoot().addActor(viewGroups.get(0));
        //添加主页
        if (viewGroups.size() >= 2)
            stage.getRoot().addActor(viewGroups.get(1));


//        View cloclView=XmlParserUtils.getDiyThemeViewFromXml(getFileString("theme_defalut_single.xml"));
//        cloclView.load();
//        stage.getRoot().addActor(cloclView);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
        stage.getRoot().setOrigin(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        stage.getRoot().getColor().a = 0;
//        remindKey = getFileString("remind.png");
//        assets.load(remindKey, Texture.class);

        if (isDebug) {
            font = new BitmapFont();
            TimeInfo info = new TimeInfo();
            info.hour = 12;
            info.minute = 34;
            info.year = 2017;
            info.month = 12;
            info.dayOfMonth = 11;
            info.dayOfWeek = 1;
            info.temperature = "22º";
            info.weather = "晴";

            synchronized (lifeCycleListems) {
                for (ILifeCycleListem lifeCycleListem : lifeCycleListems) {
                    lifeCycleListem.updateTime(info);
                }
            }
            reset();
        }

    }

    /**
     * 获取FileHandle
     *
     * @param fileName
     * @return
     */
    public static FileHandle getFileHandleString(String fileName) {
        return scenes3DCore.iHost == null ? Gdx.files.internal(fileName) : Gdx.files.absolute(scenes3DCore.fixPath + fileName);
    }

    /**
     * 获取filename
     *
     * @param fileName
     * @return
     */
    public static String getFileString(String fileName) {
        return scenes3DCore.iHost == null ? fileName : scenes3DCore.fixPath + fileName;
    }

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h);
    }


    @Override
    public void render() {
        onGameOnResume();
        assets.update();
        if (TweenAction.manager != null)
            TweenAction.manager.update(Gdx.graphics.getDeltaTime());
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        //处理消息
        if (msgCount <= 0 && msgCount != -1) {
            dispatchEvent(EventBean.EVENT_NO_MSG);
            msgCount = -1;
        }

        if(font!=null) {
            batch.setProjectionMatrix(stage.getCamera().combined);
            batch.begin();
            font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
            batch.end();
        }
    }

    @Override
    public void pause() {
        mPaused = true;

        synchronized (lifeCycleListems) {
            for (ILifeCycleListem lifeCycleListem : lifeCycleListems) {
                lifeCycleListem.pause();
            }
        }

    }


    public boolean isResumeTexture = false;

    public void onGameOnResume() {
        if (isResumeTexture) {
            isResumeTexture = false;
            internalResume();
        }
    }

    public void internalResume() {
//        Texture.invalidateAllTextures(Gdx.app);
        Cubemap.invalidateAllCubemaps(Gdx.app);
//        Mesh.invalidateAllMeshes(Gdx.app);
//        TextureArray.invalidateAllTextureArrays(Gdx.app);
//        ShaderProgram.invalidateAllShaderPrograms(Gdx.app);
//        FrameBuffer.invalidateAllFrameBuffers(Gdx.app);
        assets.finishLoading();
        if (mBindOnResumeCallbacks != null) {
            for (Runnable r : mBindOnResumeCallbacks) {
                r.run();
            }
            mBindOnResumeCallbacks.clear();
        }

        synchronized (lifeCycleListems) {
            for (ILifeCycleListem lifeCycleListem : lifeCycleListems) {
                lifeCycleListem.resume();
            }
        }

//        DefaultTextureBinder
//                Texture texture;
//        BaseShader
    }


    @Override
    public void resume() {
        mPaused = false;
        isResumeTexture = true;
    }


    @Override
    public void dispose() {
        synchronized (lifeCycleListems) {
            for (ILifeCycleListem lifeCycleListem : lifeCycleListems) {
                lifeCycleListem.dispose();
            }
            lifeCycleListems.clear();
        }
        stage.dispose();
        if(font!=null){font.dispose();}
        fixPath = null;
        iHost = null;
        hostContext = null;
        if (viewGroups != null) {
            for (ViewGroup viewGroup : viewGroups) {
                viewGroup.dispose();
            }
            viewGroups.clear();
        }
        viewGroups = null;
//        ModelLoadBase.modelDispose();
        MessageInstance.get().dispose();
        TextImageUtils.clean();
        if (assets != null)
            assets.dispose();
        assets = null;

        FontManage.get().dispose();


    }

    /**
     * 根据事件类型分发事件
     *
     * @param event
     */
    public void dispatchEvent(int event) {
        if (viewGroups != null) {
            for (ViewGroup viewGroup : viewGroups) {
                viewGroup.doEvents(event);
            }
        }
    }


    //是否有密码
    public static boolean isPswd = false;
    public int msgCount = -1;

    @Override
    public boolean pluginEvent(final Object... objects) {
        Runnable r = new Runnable() {
            public void run() {
                pluginEvent(objects);
            }
        };
        if (waitUntilResume(r)) {
            return true;
        }
        Runnable gdx_r = new Runnable() {
            public void run() {
                if (objects != null) {
                    String key = (String) objects[0];
                    switch (key) {
                        case "startUnlockAnim"://执行解锁动画
                            dispatchEvent(EventBean.EVENT_UNLOCK);
                            if (stage != null) {
                                stage.getRoot().clearActions();
                                stage.getRoot().addAction(fadeOut(0.5f));
                            }
                            break;
                        case "updateTime"://更新时间
                            TimeInfo timeInfo = (TimeInfo) objects[1];
                            if (timeInfo != null) {
                                synchronized (lifeCycleListems) {
                                    for (ILifeCycleListem lifeCycleListem : lifeCycleListems) {
                                        lifeCycleListem.updateTime(timeInfo);
                                    }
                                }
                            }
                            break;
                        case "resetGame"://重置状态
                            if (objects.length == 2 && objects[1] != null) {
                                isPswd = (boolean) objects[1];
                            }
                            reset();
                            break;
                        case "PsdAnimation"://密码页面显示
                            isCantouch = false;
                            dispatchEvent(EventBean.EVENT_PSWD_SHOW);
                            break;
                        case "NolAnimation"://密码页面消失
                            isCantouch = true;
                            dispatchEvent(EventBean.EVENT_PSWD_HIDE);
                            break;
                        case "AudioStatus":
                            isSystemMute = (boolean) objects[1];
                            break;
                        case "updateMsg":
                            msgCount = (int) objects[1];
                            if (msgCount > 0)
                                dispatchEvent(EventBean.EVENT_NEW_MSG);
                            else
                                dispatchEvent(EventBean.EVENT_NO_MSG);

                            synchronized (lifeCycleListems) {
                                for (ILifeCycleListem lifeCycleListem : lifeCycleListems) {
                                    lifeCycleListem.msgUpdate(msgCount);
                                }
                            }

                            break;
                    }
                }
            }
        };
        //post gdx线程
        Gdx.app.postRunnable(gdx_r);
        return false;
    }

    boolean waitUntilResume(Runnable run, boolean deletePreviousRunnables) {
        if (mPaused) {

            if (deletePreviousRunnables) {
                while (mBindOnResumeCallbacks.remove(run)) {
                }
            }
            mBindOnResumeCallbacks.add(run);
            return true;
        } else {
            return false;
        }
    }

    private boolean waitUntilResume(Runnable run) {
        return waitUntilResume(run, false);
    }

    @Override
    public boolean pluginIntentEvent(Intent intent) {
        return false;
    }

    @Override
    public void setHost(IHost iHost) {
        this.iHost = iHost;
    }

    //storage/emulated/0/Android/data/com.locker.host/files/assetsLibs/assets
    @Override
    public void setFixPath(String fixPath) {
        this.fixPath = fixPath + File.separator;
    }

    @Override
    public void setApplication(Context context) {
        this.hostContext = context;
    }

    //播放解锁动画
    @Override
    public boolean unLocker(long time) {
        if (iHost != null) {
            iHost.hostEvent("unlock", unLockDelay);
        } else {
            if (iMainListem != null) {
                iMainListem.unLocker(time);
            }
        }
        return true;
    }

    @Override
    public void reset() {
//        show = true;
        if (stage != null) {
            stage.getRoot().clearActions();
            stage.getRoot().addAction(fadeIn(0.5f));
        }
        isCantouch = true;
        isDisableTouch = false;
        dispatchEvent(EventBean.EVENT_LOCK);

//        synchronized (lifeCycleListems) {
//            TimeInfo info = getSpineTimeInfo();
//            if(info!=null) {
//                for (ILifeCycleListem lifeCycleListem : lifeCycleListems) {
//                    lifeCycleListem.updateTime(info);
//                }
//            }
//        }

    }
    public  TimeInfo getSpineTimeInfo(){
        if(hostContext==null)
            return null;
        TimeInfo info = new TimeInfo();
        Calendar cal = Calendar.getInstance();
        int hour;
        if(DateFormat.is24HourFormat(hostContext)){
            hour = cal.get(Calendar.HOUR_OF_DAY);
        }else{
            hour = cal.get(Calendar.HOUR) == 0 ? 12 :cal.get(Calendar.HOUR);
        }
        info.hour = hour;
        info.minute = cal.get(Calendar.MINUTE);
        info.second = cal.get(Calendar.SECOND);
        info.year = cal.get(Calendar.YEAR);
        info.month = cal.get(Calendar.MONTH)+1;
        info.dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        info.dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? 7:cal.get(Calendar.DAY_OF_WEEK)-1;
        info.weather = "";
        info.temperature = "";
        return info;
    }

    public void setIMainListem(IMainListem mainActivity) {
        this.iMainListem = mainActivity;
    }

    boolean isSystemMute = false;

    public void clickMsg() {
        if (isCantouch && iHost != null)
            iHost.hostEvent("clickMsgView");
    }

    public void findUnLockActions(List<AbsUnLockAction> unLockActions) {
        if (viewGroups != null) {
            for (ViewGroup viewGroup : viewGroups) {
                viewGroup.findUnLockActions(unLockActions);
            }
        }
    }
}
