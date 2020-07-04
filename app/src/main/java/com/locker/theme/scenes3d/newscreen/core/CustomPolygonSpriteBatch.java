package com.locker.theme.scenes3d.newscreen.core;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 *
 * 自定义Batch解决透明度混合问题
 * Created by xufeifan on 2017/4/26.
 */

public class CustomPolygonSpriteBatch extends PolygonSpriteBatch {
   private int customValue=NORNAL_BLEAN;
    public CustomPolygonSpriteBatch(int size, ShaderProgram defaultShader){
            super(size,defaultShader);
    }
    public CustomPolygonSpriteBatch(){
        super();
    }
    /**
     * 自定义混合模式
     * @param value
     */
    public void setCustomBlend(int value){
        this.customValue=value;

            switch (customValue){
                //stage
                case STAGE_BLEAN:
                    setBlendFunction(GL20.GL_ONE,GL20.GL_ONE_MINUS_SRC_ALPHA);
                    break;
                //粒子效果
                case PARTICLE_BLEAN:
                    setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE);
                    break;
                case NORNAL_BLEAN:
                    //正常混合模式
                    setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
                    break;

            }

    }
    public static final int STAGE_BLEAN=1;
    public static final int PARTICLE_BLEAN=2;
    public static final int NORNAL_BLEAN=3;

    public void setBlendFunction(int srcFunc, int dstFunc) {
         srcFunc=GL20.GL_SRC_ALPHA;
         dstFunc= GL20.GL_ONE_MINUS_SRC_ALPHA;
        switch (customValue){
            case STAGE_BLEAN:
                srcFunc=GL20.GL_ONE;
                dstFunc=GL20.GL_ONE_MINUS_SRC_ALPHA;
                break;
            case PARTICLE_BLEAN:
                srcFunc=GL20.GL_SRC_ALPHA;
                dstFunc=GL20.GL_ONE;
                break;
            case NORNAL_BLEAN:
                srcFunc=GL20.GL_SRC_ALPHA;
                dstFunc=GL20.GL_ONE_MINUS_SRC_ALPHA;
                break;

        }
       super.setBlendFunction(srcFunc, dstFunc);
    }
}
