package com.locker.theme.scenes3d.newscreen.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.locker.theme.scenes3d.newscreen.beans.ModelBean;
import com.locker.theme.scenes3d.newscreen.face.VideoFace;

/**
 * 3D模型类型View
 * Created by xff on 2017/12/22.
 */

public class ModelView extends View<ModelBean> {
    public ModelLoadBase modelLoadBase;
    public ModelView() {

    }

    @Override
    protected void load(ModelBean modelBean) {
        if (modelLoadBase == null) {
            modelLoadBase = new ModelLoadBase(modelBean);
        }

        initAttributes(modelBean);
    }


    @Override
    public void reset() {
        super.reset();
    }

    //初始化基本属性
    @Override
    protected void initAttributes(ModelBean actorBean) {
        super.initAttributes(actorBean);
    }

    @Override
    public void layout() {
        super.layout();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Color color = this.getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        if(modelLoadBase!=null&&actonBean!=null) {
            batch.flush();
            batch.end();
            // batch,  camera,  x,  y,  z,  width,  height,  thickness,  scacleX,  scacleY,  scacleZ,  ratationX,  ratationY,  ratationZ,  originWidth,  originHeight,  originThickness,  parentAlpha
            modelLoadBase.render(this,batch, getStage().getCamera(), getX() + getWidth() / 2, getY() + getHeight() / 2, getZ(), getWidth(), getHeight(),getThickness(), getScaleX(), getScaleY(), getScaleZ(), getRotationX()+iRotateX,getRotationY()+iRotateY, getRotation()+iRotateZ,actonBean.originWidth,actonBean.originHeight,actonBean.originThickness,actonBean.scaleMode,actonBean.lerp, batch.getColor().a);
            batch.begin();
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        modelLoadBase.dispose();
        modelLoadBase=null;
    }
}
