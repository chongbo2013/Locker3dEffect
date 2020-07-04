package com.locker.theme.scenes3d.newscreen.widgets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

/**
 * Created by xff on 2017/8/5.
 */

public abstract class GLPageViewGroup extends WidgetGroup {

   protected WidgetContaint widget;

    public GLPageViewGroup() {
        widget=new WidgetContaint();
        addActor(widget);
    }

    public WidgetGroup getView() {
        return widget;
    }


    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
    }

    @Override
    public void layout() {
        super.layout();
        widget.setBounds(widget.getX(), widget.getY(), getWidth() * getChildCount(), getHeight());

    }

    public void scrollBy(float x, float y) {
        scrollTo(getScrollX() + x, getScrollY() + y);
    }

    public void scrollTo(float x, float y) {
        widget.setPosition(-x, -y);
    }


    public float getScrollX() {
        return -widget.getX();
    }

    public float getScrollY() {
        return -widget.getY();
    }

    public int getChildCount() {
        return widget.getChildren().size;
    }

    public Actor getChildAt(int index) {
        if (index < widget.getChildren().size)
            return widget.getChildren().get(index);

        return null;
    }

    public void addView(Actor actor) {
        widget.addActor(actor);
    }

    public void addViewAt (int index, Actor actor){
        widget.addActorAt(index,actor);
    }
    public void removeView(Actor actor){
        widget.removeActor(actor);
    }
    @Override
    public Matrix4 computeTransform() {
        return super.computeTransform();
    }


    public void applyTransform(Batch batch, Matrix4 transform) {
        super.applyTransform(batch,transform);
    }


    public void resetTransform(Batch batch) {
        super.resetTransform(batch);
    }



    public class WidgetContaint extends WidgetGroup {


        @Override
        public void layout() {
            super.layout();

        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            validate();
            if (isTransform()) {
                this.applyTransform(batch, this.computeTransform());
            }
            final int pageCount = getChildCount();
            if (pageCount > 0) {
                dispatchDraw(batch,parentAlpha);
            }
            if (isTransform()) {
                this.resetTransform(batch);
            }



        }
    }

    public  void dispatchDraw(Batch batch, float parentAlpha){
//            widget.drawChildren(batch,parentAlpha);
    }

}
