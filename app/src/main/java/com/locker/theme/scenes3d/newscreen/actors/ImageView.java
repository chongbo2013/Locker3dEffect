package com.locker.theme.scenes3d.newscreen.actors;

import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.locker.theme.scenes3d.newscreen.beans.ImageBean;
import com.locker.theme.scenes3d.newscreen.face.Face;
import com.locker.theme.scenes3d.newscreen.face.RedDotFace;

/**
 * 图片类型的View
 * 2d 动画 只支持  屏幕的2维 缩放等
 * Created by xff on 2017/12/22.
 */
public class ImageView extends View<ImageBean> {
    //使用渐变效果
    boolean useGradient = false;

    float animationValue = 0.0f;
    //动画是否显示
    boolean show = true;
    boolean hideEffect=false;
    float time = 0;
    float totalTime = 1.5f;
    boolean flipTime = false;

    @Override
    protected void load(ImageBean imageBean) {
        initAttributes(imageBean);
        if (!TextUtils.isEmpty(imageBean.effect) && imageBean.effect.equals("Gradient")) {
            useGradient = true;
        }
    }

    //暂时不实现 rotationYZ等3维的旋转，防止性能下降
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = this.getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if (useGradient) {
            hideEffect=Gdx.input.isTouched();
            batch.flush();
            float radio;
            if (batch.getShader().isCompiled()) {
                time += Gdx.graphics.getDeltaTime() * (flipTime ? -1 : 1);
                radio = MathUtils.clamp(time / totalTime, 0, 1);
                if(hideEffect){//
                    flipTime=false;
                }else if (show) {
                    if (radio == 0) {
                        flipTime = false;
                    } else if (radio == 1) {
                        flipTime = true;
                    }
                } else {
                    flipTime=true;
                }
                batch.getShader().setUniformf("radio", radio);
                batch.getShader().setUniformf("normalstutas", 0.0f);
            }
        }
        if (actonBean.face != null) {
            for (Face face : actonBean.face) {
                face.update(this);
                TextureRegion textureRegion = face.getFace();
                if (textureRegion != null) {
                    if (face instanceof RedDotFace) {
                        RedDotFace redDotFace = (RedDotFace) face;
                        //小红点
                        batch.draw(textureRegion, getX() + redDotFace.x, getY() + redDotFace.y, redDotFace.width / 2, redDotFace.height / 2, redDotFace.width, redDotFace.height, getScaleX(), getScaleY(), getRotation() + iRotateZ);
                    } else {
                        //float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation
                        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() + iRotateZ);
                    }
                }

            }
        }

        if (useGradient) {
            batch.flush();
            batch.getShader().setUniformf("normalstutas", 1.0f);
        }
    }
}
