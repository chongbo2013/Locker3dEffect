package com.locker.theme.scenes3d.newscreen.widgets;

import android.view.animation.Interpolator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.locker.theme.scenes3d.Scenes3DCore;


/**
 * Created by xff on 2017/6/5.
 */

public class GLSwitchPagedView extends GLPageViewGroup {
    private static final int SNAP_VELOCITY = 550;
    private static final int INVALID_SCREEN = Integer.MAX_VALUE;
    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private static final int MOVE_LEFT = 0;
    private static final int MOVE_RIGHT = 1;
    private static final float mLayoutScale = 1.0f;
    private int mTouchState = TOUCH_STATE_REST;
    private float mLastMotionX;
    private float mLastDownX;
    private int mDirection;


    public static int mCurrentScreen = 0;
    private int mNextScreen = INVALID_SCREEN;
    private int[] mTempVisiblePagesRange = new int[2];
    private float mScrollingSpeed = 0.4F;
    private float mMoveThreshold = 0.3f;
    private GLLauncherScroller mScroller;
    private ViewSwitchListener mSwitchListener;
    private boolean mLoop = false;
    private boolean mAllowOverScroll = true;

    public interface ViewSwitchListener {

        void onSwitched(
                Actor view,
                int position);
    }

    ActorGestureListener flickScrollListener;
    float vx = 0;
    float vy = 0;
    float totalDeltaX = 0;

    public GLSwitchPagedView() {
        super();
        init();
        flickScrollListener = new ActorGestureListener() {
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
                totalDeltaX += deltaX;
                float absDeltaX = Math.abs(deltaX);
                float absDeltaY = Math.abs(deltaY);
                if (mTouchState != TOUCH_STATE_SCROLLING && absDeltaX > absDeltaY) {
                    mTouchState = TOUCH_STATE_SCROLLING;
                }else{
//                    if(getCurrentScreen()==2&&VideoCellLayout.clockActor!=null){
//                        VideoCellLayout.clockActor.sendTouch(MotionEvent.ACTION_MOVE,x,y);
//                    }
                }
                if (mTouchState == TOUCH_STATE_SCROLLING) {
                    if (totalDeltaX > 0)
                        mDirection = MOVE_RIGHT;
                    else
                        mDirection = MOVE_LEFT;
                    mLastMotionX = x;
                    scrollBy(-deltaX, 0);
                }

            }

            public void fling(InputEvent event, float vx2, float vy2, int button) {
                vx = vx2;
                vy = vy2;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer > 0)
                    return;
                if (mTouchState == TOUCH_STATE_SCROLLING) {

                    float velocityabsX = Math.abs(vx);
                    int whichScreen = 0;
                    final float screenWidth = getWidth();
                    final float movex = Math.abs(mLastMotionX - mLastDownX);
                    if (mDirection == MOVE_LEFT && movex > screenWidth * mMoveThreshold) {
                        whichScreen = Math.max((mLoop ? -1 : 0), Math.min(mCurrentScreen + 1, getChildCount() - (mLoop ? 0 : 1)));
                    } else if (mDirection == MOVE_RIGHT && movex > screenWidth * mMoveThreshold) {
                        whichScreen = Math.max((mLoop ? -1 : 0), Math.min(mCurrentScreen - 1, getChildCount() - (mLoop ? 0 : 1)));
                    } else {
                        whichScreen = mCurrentScreen;
                    }

                    final float scrolledPos = getScrollX() / screenWidth;
                    if (velocityabsX > SNAP_VELOCITY && mDirection == MOVE_RIGHT && mCurrentScreen > (mLoop ? -1 : 0)) {
                        final int bound = scrolledPos < whichScreen ? mCurrentScreen - 1 : mCurrentScreen;
                        snapToScreen(Math.min(whichScreen, bound));
                    } else if (velocityabsX > SNAP_VELOCITY && mDirection == MOVE_LEFT && mCurrentScreen < getChildCount() - (mLoop ? 0 : 1)) {
                        final int bound = scrolledPos > whichScreen ? mCurrentScreen + 1 : mCurrentScreen;
                        snapToScreen(Math.max(whichScreen, bound));
                    } else {
                        snapToDestination();
                    }

                }else{
                    Actor actor=GLSwitchPagedView.super.hit(x,y,true);
                    if(actor!=null){
                            for (EventListener eventListener : actor.getListeners()) {
                                if (eventListener instanceof ClickListener) {
                                    ((ClickListener) eventListener).clicked(event, x, y);
                                }
                            }
                    }
                }

                mTouchState = TOUCH_STATE_REST;

//                if(getCurrentScreen()==2&&VideoCellLayout.clockActor!=null){
//                    VideoCellLayout.clockActor.sendTouch(MotionEvent.ACTION_UP,x,y);
//                }
            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pointer > 0)
                    return;
                totalDeltaX = 0;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastDownX = mLastMotionX = x;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
//                if(getCurrentScreen()==2&&VideoCellLayout.clockActor!=null){
//                    VideoCellLayout.clockActor.sendTouch(MotionEvent.ACTION_DOWN,x,y);
//                }
            }

        };
        addListener(flickScrollListener);
    }

    public static final int MIN_SNAP_VELOCITY = 150;

    private void init() {
        mScroller = new GLLauncherScroller(Scenes3DCore.scenes3DCore.hostContext, new ScrollInterpolator());
        float mDensity = Scenes3DCore.scenes3DCore.hostContext.getResources().getDisplayMetrics().density;
        mMinSnapVelocity = (int) (MIN_SNAP_VELOCITY * mDensity);
    }

    private static class ScrollInterpolator implements Interpolator {
        public ScrollInterpolator() {
        }

        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1;
        }
    }

    @Override
    public void layout() {
        super.layout();
        float left = 0, top = 0, right = getWidth(), bottom = getHeight();
        float width = right - left;
        float height = bottom - top;
        float l = 0, t = 0, r = width, b = bottom - top;
        for (int i = 0; i < getChildCount(); i++) {
            Actor widget = getChildAt(i);
            widget.setBounds(l, t, width, height);
            l += width;
        }

    }


    @Override
    public void act(float delta) {
        super.act(delta);
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        } else if (mNextScreen != INVALID_SCREEN) {
            if (mNextScreen == -1 && mLoop) {
                mCurrentScreen = getChildCount() - 1;
                scrollTo(mCurrentScreen * getWidth(), getScrollY());
            } else if (mNextScreen == getChildCount() && mLoop) {
                mCurrentScreen = 0;
                scrollTo(0, getScrollY());
            } else {

            }
            mNextScreen = INVALID_SCREEN;
        }
    }

    public Actor hit(float x, float y, boolean touchable) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) return null;
        return this;
//        return super.hit(x, y, touchable);
    }


    int getPageCount() {
        return getChildCount();
    }

    Actor getPageAt(
            int index) {
        return getChildAt(index);
    }


    public void scrollTo(
            int x,
            int y) {
        if (mAllowOverScroll) {
            super.scrollTo(x, y);
            return;
        }
        final float pageWidth = getScaledMeasuredWidth(getPageAt(0));
        final float mMaxScrollX = (getChildCount() - 1) * pageWidth;
        if (x < 0) {
            super.scrollTo(0, y);
        } else if (x > mMaxScrollX) {
            super.scrollTo(mMaxScrollX, y);
        } else {
            super.scrollTo(x, y);
        }
    }

    public int getViewsCount() {
        return getChildCount();
    }

    public int getCurrentScreen() {
        return mCurrentScreen;
    }

    public boolean isFinished() {
        return this.mScroller.isFinished();
    }

    public void snapToDestination() {
        final float screenWidth = getWidth();
        final int whichScreen = (int) ((getScrollX() + (screenWidth / 2)) / screenWidth);
        snapToScreen(whichScreen);
    }

    public void InitToScreen(
            int screen,
            int pagewidth) {
        mCurrentScreen = screen;
        super.scrollTo(mCurrentScreen * pagewidth, 0);
    }

    protected int mMinSnapVelocity;

    public void snapToScreen(
            int whichScreen) {
        whichScreen = Math.max((mLoop ? -1 : 0), Math.min(whichScreen, getChildCount() - (mLoop ? 0 : 1)));
        mNextScreen = whichScreen;
        final float newX = whichScreen * getWidth();
        final float delta = newX - getScrollX();
        vx = Math.abs(vx);
        vx = Math.max(mMinSnapVelocity, vx);

        float time=(int) (Math.abs(delta) * mScrollingSpeed);
        mScroller.startScroll(getScrollX(), 0, delta, 0, (int)time);
        if (mNextScreen != INVALID_SCREEN) {
            if (mNextScreen == -1 && mLoop) {
                mCurrentScreen = getChildCount() - 1;
            } else if (mNextScreen == getChildCount() && mLoop) {
                mCurrentScreen = 0;
            } else {
                mCurrentScreen = Math.max(0, Math.min(mNextScreen, getChildCount() - 1));
            }
            if (mSwitchListener != null)
                mSwitchListener.onSwitched(getChildAt(mCurrentScreen), mCurrentScreen);
            pageSelect(mCurrentScreen);
        }

        if (!Gdx.graphics.isContinuousRendering()) {
            Gdx.graphics.requestRendering();
        }
    }



    protected void pageSelect(int currentPage) {


    }

    //Jone add end
    protected float getScaledRelativeChildOffset(
            int index) {
        final float offset = (getWidth() - getScaledMeasuredWidth(getPageAt(index))) / 2;
        return offset;
    }

    protected float getScaledMeasuredWidth(
            Actor child) {
        final float measuredWidth = child.getWidth();
        final float minWidth = 0;
        final float maxWidth = (minWidth > measuredWidth) ? minWidth : measuredWidth;
        return maxWidth * mLayoutScale + 0.5f;
    }

    protected void getVisiblePages(
            int[] range) {
        final int pageCount = getChildCount();
        if (pageCount > 0) {
            final float pageWidth = getScaledMeasuredWidth(getPageAt(0));
            final float screenWidth = getWidth();
            float x = getScaledRelativeChildOffset(0) + pageWidth;
            int leftScreen = 0;
            int rightScreen = 0;
            if (mLoop && getScrollX() > screenWidth * (pageCount - 1) || getScrollX() < 0) {
                rightScreen = 0;
                leftScreen = pageCount - 1;
            } else {
                while (x <= getScrollX() && leftScreen < pageCount - 1) {
                    leftScreen++;
                    x += getScaledMeasuredWidth(getPageAt(leftScreen));
                }
                rightScreen = leftScreen;
                while (x < getScrollX() + screenWidth && rightScreen < pageCount - 1) {
                    rightScreen++;
                    x += getScaledMeasuredWidth(getPageAt(rightScreen));
                }
            }
            range[0] = leftScreen;
            range[1] = rightScreen;
        } else {
            range[0] = -1;
            range[1] = -1;
        }
    }


    @Override
    public void dispatchDraw(Batch batch, float parentAlpha) {
        final int pageCount = getChildCount();
        getVisiblePages(mTempVisiblePagesRange);
        final int leftScreen = mTempVisiblePagesRange[0];
        final int rightScreen = mTempVisiblePagesRange[1];
        if (leftScreen == -1 || rightScreen == -1)
            return;
        float screenCenter = (Math.min(Math.max(getScrollX(), 0f), getWidth() * (getChildCount() - 1)) / getWidth());
        int fixleftScreen = pageCount - 1;
        int fixrightScreen = (int) screenCenter;
        if (fixleftScreen == fixrightScreen) {
            getPageAt(fixleftScreen).draw(batch, parentAlpha);
        } else {
            getPageAt(rightScreen).draw(batch, parentAlpha);
            getPageAt(leftScreen).draw(batch, parentAlpha);
        }
    }
}
