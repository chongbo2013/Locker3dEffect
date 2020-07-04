package com.locker.theme.scenes3d.newscreen.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.text.TextPaint;
import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.locker.theme.scenes3d.Scenes3DCore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * 文字工具
 * Created by xff on 2017/4/20.
 */

public class TextImageUtils {

    static Canvas canvas;
    static Bitmap bitmapTime,bitmapMsgText,bitmapRedDot;

    /**
     * 文字转图片
     *
     * @param mContext
     * @param color
     * @param widgetW
     * @param widgetH
     * @param date
     * @param week
     * @param weather
     * @return
     */
    public synchronized static Texture createTimeTexture(Context mContext, int color, float widgetW, float widgetH, String date, String week, String weather) {
        float screenW = Gdx.graphics.getWidth();
        TextPaint mPanit = new TextPaint();
        Typeface type = Scenes3DCore.scenes3DCore.iHost == null ? Typeface.createFromAsset(mContext.getAssets(), "font.ttf") : Typeface.createFromFile(Scenes3DCore.scenes3DCore.fixPath + "font.ttf");
        if (type != null) {
            mPanit.setTypeface(type);
        }
        mPanit.setAntiAlias(true);
        mPanit.setColor(color);
        mPanit.setTextSize(screenW * 0.038f);
        mPanit.setTextAlign(Paint.Align.LEFT);
        float dateWidth = (int) mPanit.measureText(date);
        Paint.FontMetrics fontMetrics = mPanit.getFontMetrics();
        float dateHeight = fontMetrics.bottom - fontMetrics.top;
        float dateBaseY = widgetH - (widgetH - dateHeight) / 2 - fontMetrics.bottom;

        float weekWidth = (int) mPanit.measureText(week);
        fontMetrics = mPanit.getFontMetrics();
        float weekHeight = fontMetrics.bottom - fontMetrics.top;
        float weekBaseY = widgetH - (widgetH - weekHeight) / 2 - fontMetrics.bottom;

        float weatherWidth = (int) mPanit.measureText(weather);
        fontMetrics = mPanit.getFontMetrics();
        float weatherHeight = fontMetrics.bottom - fontMetrics.top;
        float weatherBaseY = widgetH - (widgetH - weatherHeight) / 2 - fontMetrics.bottom;

        if(bitmapTime==null||bitmapTime.isRecycled()) {
            bitmapTime = Bitmap.createBitmap((int) widgetW, (int) widgetH, Bitmap.Config.ARGB_8888);
        }
        if(canvas==null){
            canvas=new Canvas();
        }

        bitmapTime.eraseColor(Color.TRANSPARENT);
        canvas .setBitmap(bitmapTime);
        canvas.save();
        float space = widgetW - dateWidth - weekWidth - weatherWidth;
        float startSpace = space * 0.1f;
        float endSpace = space * 0.1f;
        float date_week = (space - startSpace - endSpace) * 0.55f;


        canvas.drawText(date, startSpace, dateBaseY, mPanit);
        if (TextUtils.isEmpty(weather)) {
            canvas.drawText(week, widgetW - weekWidth - endSpace, weekBaseY, mPanit);
        } else {
            canvas.drawText(week, startSpace + dateWidth + date_week, weekBaseY, mPanit);
            canvas.drawText(weather, widgetW - weatherWidth - endSpace, weatherBaseY, mPanit);
        }
        canvas.restore();
        return createTexture(bitmapTime);
    }


    /**
     * view to texture
     *
     * @param mContext
     * @param color
     * @param msg
     * @return
     */
    public synchronized static Texture createMsgText(Context mContext, int color, String msg) {
        float screenW = Gdx.graphics.getWidth();
        TextPaint mPanit = new TextPaint();
        mPanit.setAntiAlias(true);
        mPanit.setColor(color);
        mPanit.setTextSize(screenW * 0.038f);
        mPanit.setTextAlign(Paint.Align.LEFT);

        float dateWidth = (int) mPanit.measureText(msg);
        Paint.FontMetrics fontMetrics = mPanit.getFontMetrics();
        float dateHeight = fontMetrics.bottom - fontMetrics.top;


        float dateBaseY = dateHeight - fontMetrics.bottom;
        if(bitmapMsgText==null||bitmapMsgText.isRecycled()) {
            bitmapMsgText = Bitmap.createBitmap((int) dateWidth, (int) dateHeight, Bitmap.Config.ARGB_8888);
        }
        if(canvas==null){
            canvas=new Canvas();
        }
        bitmapMsgText.eraseColor(Color.TRANSPARENT);
        canvas .setBitmap(bitmapMsgText);
        canvas.save();
        float space = dateWidth - dateWidth;
        canvas.drawText(msg, space / 2, dateBaseY, mPanit);
        canvas.restore();
        return createTexture(bitmapMsgText);
    }


    /**
     *
     * @param mContext
     * @param colorBg
     * @param colorText
     * @param widgetW
     * @param widgetH
     * @param numble
     * @return
     */
    public synchronized static Texture createRedDotTexture(Context mContext, int colorBg,int colorText, float widgetW, float widgetH,int numble) {
        float screenW = Gdx.graphics.getWidth();
        TextPaint mPanit = new TextPaint();
       // Typeface type = Scenes3DCore.scenes3DCore.iHost == null ? Typeface.createFromAsset(mContext.getAssets(), "font.ttf") : Typeface.createFromFile(Scenes3DCore.scenes3DCore.fixPath + "font.ttf");
       // mPanit.setTypeface(type);
        mPanit.setAntiAlias(true);

        mPanit.setTextSize(screenW * 0.030f);
        mPanit.setTextAlign(Paint.Align.LEFT);
        float dateWidth = (int) mPanit.measureText(String.valueOf(numble));
        Paint.FontMetrics fontMetrics = mPanit.getFontMetrics();
        float dateHeight = fontMetrics.bottom - fontMetrics.top;
        float dateBaseY = widgetH - (widgetH - dateHeight) / 2 - fontMetrics.bottom;

        if(bitmapRedDot==null||bitmapRedDot.isRecycled()) {
            bitmapRedDot = Bitmap.createBitmap((int) widgetW, (int) widgetH, Bitmap.Config.ARGB_8888);
        }
        bitmapRedDot.eraseColor(Color.TRANSPARENT);
        if(canvas==null) {
            canvas = new Canvas();
        }

        canvas.setBitmap(bitmapRedDot);
        canvas.save();
        float space = widgetW - dateWidth ;
        float startSpace=space/2f;
        mPanit.setColor(colorBg);
        canvas.drawCircle(widgetW/2,widgetH/2,widgetW/2,mPanit);
        mPanit.setColor(colorText);
        canvas.drawText(String.valueOf(numble), startSpace, dateBaseY, mPanit);
        canvas.restore();
        return createTexture(bitmapRedDot);
    }

    /**
     * FreeListenerz
     * 回收图片
     *
     * @param bitmap
     * @return
     */
    private static Texture createTexture(Bitmap bitmap) {

//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,100, outputStream);
//        byte[] encodedData = outputStream.toByteArray();
//        Pixmap pixmap = new Pixmap(encodedData, 0, encodedData.length);
//        Texture texture = new Texture(pixmap);
//        if (bitmap != null && !bitmap.isRecycled()) {
//            bitmap.recycle();
//            bitmap = null;
//        }

        Texture tex = new Texture(bitmap.getWidth(), bitmap.getHeight(), Pixmap.Format.RGBA8888);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex.getTextureObjectHandle());
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
       // bitmap.recycle();

        return tex;

    }


    public static void clean(){
        canvas=null;
        if(bitmapTime!=null&&!bitmapTime.isRecycled()){
            bitmapTime.recycle();
            bitmapTime=null;
        }

        if(bitmapMsgText!=null&&!bitmapMsgText.isRecycled()){
            bitmapMsgText.recycle();
            bitmapMsgText=null;
        }

        if(bitmapRedDot!=null&&!bitmapRedDot.isRecycled()){
            bitmapRedDot.recycle();
            bitmapRedDot=null;
        }

    }

    public synchronized static Pixmap createPixmapAsyncRecycle(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        byte[] encodedData = outputStream.toByteArray();
        if (bitmap != null)
            bitmap.recycle();
        Pixmap pixmap = new Pixmap(encodedData, 0, encodedData.length);
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        encodedData = null;
        outputStream = null;
        return pixmap;

    }
}
