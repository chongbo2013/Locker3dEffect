package com.locker.theme.scenes3d.newscreen.face;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.locker.theme.scenes3d.Scenes3DCore;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态表皮
 * Created by xff on 2017/12/27.
 */

public class GifFace extends Face {
    public Texture faceTexture;
    boolean load = true;
    public String split;
    public String animations;
    Animation<TextureRegion> currentAnimation;
    Map<String, Animation<TextureRegion>> animationMap;
    float currentFrameTime;


    public Animation<TextureRegion> getAnimationByName(String name) {
        if (animationMap == null)
            return null;
        if (animationMap.containsKey(name)) {
            return animationMap.get(name);
        }
        return null;
    }

    /**
     * 更新表皮
     */
    @Override
    public void update(Actor actor) {
        if (load) {
            if (!Scenes3DCore.scenes3DCore.assets.isLoaded(resource, Texture.class))
                Scenes3DCore.scenes3DCore.assets.load(resource, Texture.class);
            load = false;
        }

        if (faceTexture == null) {
            if (Scenes3DCore.scenes3DCore.assets.isLoaded(resource, Texture.class)) {
                faceTexture = Scenes3DCore.scenes3DCore.assets.get(resource, Texture.class);

                if(split==null)
                    return;
                String[] splitStr = split.split(",");
                TextureRegion[][] regions = TextureRegion.split(faceTexture, Integer.parseInt(splitStr[0]), Integer.parseInt(splitStr[1]));
                if(animations==null)
                    return;
                animationMap = new HashMap<>();
                String[] animationsStr = animations.split("\\|");
                for (int i = 0; i < animationsStr.length; i++) {
                    String items=animationsStr[i];
                    String[] nameIndexStr=items.split(",");
                    String key=nameIndexStr[0];
                    int index=Integer.parseInt(nameIndexStr[1]);
                    float speed=Float.parseFloat(nameIndexStr[2]);
                    Animation  testAnimation = new Animation<TextureRegion>(speed, regions[index]);

                    if(nameIndexStr.length==4){
                        Animation.PlayMode playMode= Animation.PlayMode.LOOP;
                        String playModeKey=nameIndexStr[3];
                        switch (playModeKey){
                            case "NORMAL":
                                playMode= Animation.PlayMode.NORMAL;
                                break;
                            case "REVERSED":
                                playMode= Animation.PlayMode.REVERSED;
                                break;
                            case "LOOP":
                                playMode= Animation.PlayMode.LOOP;
                                break;
                            case "LOOP_REVERSED":
                                playMode= Animation.PlayMode.LOOP_REVERSED;
                                break;
                            case "LOOP_PINGPONG":
                                playMode= Animation.PlayMode.LOOP_PINGPONG;
                                break;
                            case "LOOP_RANDOM":
                                playMode= Animation.PlayMode.LOOP_RANDOM;
                                break;
                        }
                        testAnimation.setPlayMode(playMode);
                    }
                    testAnimation.setPlayMode(Animation.PlayMode.REVERSED);
                    animationMap.put(key,testAnimation);
                    if(currentAnimation==null){//第一个动画为默认动画
                        currentAnimation=testAnimation;
                    }
                }

            }
        }

        currentFrameTime += Gdx.graphics.getDeltaTime();
    }

    /**
     * 获取表皮
     *
     * @return
     */
    @Override
    public TextureRegion getFace() {
        if (currentAnimation == null)
            return null;
        return currentAnimation.getKeyFrame(currentFrameTime, true);

    }

    @Override
    public void dispose() {
        super.dispose();
        if (faceTexture != null) {
            faceTexture.dispose();
        }
        faceTexture = null;


        currentAnimation=null;

        if(animationMap!=null)
            animationMap.clear();
        animationMap=null;
    }

    @Override
    public void reset() {
        super.reset();
        currentFrameTime=0;
    }
}
