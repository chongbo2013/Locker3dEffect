package com.locker.theme.scenes3d.newscreen.lib;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.GdxSurfaceTexture;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OESTexture;
import com.locker.theme.scenes3d.Scenes3DCore;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 *实现3D模型上播放视频效果
 * @author xff
 */
public class GdxVideoPlayer implements VideoPlayer, OnFrameAvailableListener {

    public GdxSurfaceTexture videoTexture=new GdxSurfaceTexture();


    private MediaPlayer player;
    private boolean prepared = false;
    private boolean frameAvailable = false;
    private boolean done = false;

    VideoSizeListener sizeListener;
    CompletionListener completionListener;


    /**
     * Used for sending mediaplayer tasks to the Main Looper
     */
    private static Handler handler;




    public Runnable runnable;
    public GdxVideoPlayer(Runnable runnable) {
        this.runnable=runnable;
        setupRenderTexture();
        initializeMediaPlayer();
    }



    private void initializeMediaPlayer() {
        if(handler == null)
            handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                    player = new MediaPlayer();
                    Gdx.app.postRunnable(runnable);
            }
        });
    }

    @Override public boolean play (final FileHandle file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("Could not find file: " + file.path());
        }

        player.reset();
        done = false;

        player.setOnPreparedListener(new OnPreparedListener() {
            @Override public void onPrepared (MediaPlayer mp) {
                //视频填充屏幕中 by ferris.xu
                float videoWidth = mp.getVideoWidth();
                float videoHeight = mp.getVideoHeight();

                float sceenWidth= Gdx.graphics.getWidth();
                float sceenHeight= Gdx.graphics.getHeight();


                float x = -sceenWidth / 2;
                float y = -sceenHeight / 2;


                float radioW =  sceenWidth /videoWidth;
                float radioH =  sceenHeight / videoHeight;
                float maxRadio = Math.max(radioW, radioH);
                float newW = videoWidth * maxRadio;
                float newH = videoHeight * maxRadio;
                float left=Math.abs(Math.abs((int) ((sceenWidth - newW) / 2)));
                float top=Math.abs(Math.abs((int) ((sceenHeight - newH) / 2)));
                //通过修改uv顶点，来裁剪图像
                float u1=left/newW;
                float v1=top/newH;
                float u2=u1+sceenWidth/newW;
                float v2=v1+sceenHeight/newH;


                prepared = true;
                if (sizeListener != null) {
                    sizeListener.onVideoSize(sceenWidth, sceenHeight);
                }

                mp.setLooping(loop);
                setSoundStatus(!mute);
                mp.start();

            }
        });
        player.setOnErrorListener(new OnErrorListener() {
            @Override public boolean onError (MediaPlayer mp, int what, int extra) {
                done = true;
                Log.e("VideoPlayer", String.format("Error occured: %d, %d\n", what, extra));
                return false;
            }
        });

        player.setOnCompletionListener(new OnCompletionListener() {
            @Override public void onCompletion (MediaPlayer mp) {
                done = true;
                if (completionListener != null) {
                    completionListener.onCompletionListener(file);
                }
            }
        });

        try {
            if (file.type() == FileType.Classpath || (file.type() == FileType.Internal && !file.file().exists())) {
                AssetManager assets = Scenes3DCore.scenes3DCore.hostContext.getAssets();
                AssetFileDescriptor descriptor = assets.openFd(file.name());
                player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            } else {
                player.setDataSource(file.file().getAbsolutePath());
            }
            if(videoTexture!=null&&videoTexture.videoTexture!=null)
            player.setSurface(new Surface((SurfaceTexture) videoTexture.videoTexture));
            player.prepareAsync();
            player.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override public void resize (int width, int height) {

    }
    public void setSoundStatus(boolean hasSound) {
        if (player != null) {
            player.setVolume(hasSound ? 1 : 0, hasSound ? 1 : 0);
        }
    }
    @Override public boolean render () {
        if (done) {
            return false;
        }
        if (!prepared) {
            return true;
        }
        synchronized (this) {
            if (frameAvailable) {
                if(videoTexture!=null&&videoTexture.videoTexture!=null)
                    ((SurfaceTexture)videoTexture.videoTexture).updateTexImage();
                frameAvailable = false;
            }
        }


        return !done;
    }

    /**
     * For android, this will return whether the prepareAsync method of the android MediaPlayer is done with preparing.
     *
     * @return whether the buffer is filled.
     */
    @Override public boolean isBuffered () {
        return prepared;
    }

    @Override public void stop () {
        if (player != null && player.isPlaying()) {
            player.stop();
        }
        prepared = false;
        done = true;
    }

    private void setupRenderTexture () {
        // Generate the actual texture
        OESTexture texture=new OESTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(texture.glTarget, texture.glHandle);
        videoTexture=new GdxSurfaceTexture(texture,new SurfaceTexture(texture.glHandle));
        ((SurfaceTexture)videoTexture.videoTexture).setOnFrameAvailableListener(this);
    }

    @Override public void onFrameAvailable (SurfaceTexture surfaceTexture) {
        synchronized (this) {
            frameAvailable = true;
        }
    }

    @Override public void pause () {
        // If it is running
        if (prepared) {
            if(player.isPlaying())
            player.pause();
        }
    }

    @Override public void resume () {
        // If it is running
        if (prepared) {
            if(!player.isPlaying())
            player.start();
        }
    }

    @Override public void dispose () {
        stop();
        if(player != null)
            player.release();

        if(videoTexture!=null&&videoTexture.videoTexture!=null){
            ((SurfaceTexture)videoTexture.videoTexture).detachFromGLContext();

            if(videoTexture.texture!=null){
                videoTexture.texture.dispose();
            }
            videoTexture=null;
        }


    }

    @Override public void setOnVideoSizeListener (VideoSizeListener listener) {
        sizeListener = listener;
    }

    @Override public void setOnCompletionListener (CompletionListener listener) {
        completionListener = listener;
    }

    @Override public int getVideoWidth () {
        if (!prepared) {
            throw new IllegalStateException("Can't get width when video is not yet buffered!");
        }
        return player.getVideoWidth();
    }

    @Override public int getVideoHeight () {
        if (!prepared) {
            throw new IllegalStateException("Can't get height when video is not yet buffered!");
        }
        return player.getVideoHeight();
    }

    @Override public boolean isPlaying () {
        return player.isPlaying();
    }

    public boolean mute=true;
    public boolean loop=true;
    public void setConfig(boolean mute, boolean loop) {
        this.mute=mute;
        this.loop=loop;
    }
}
