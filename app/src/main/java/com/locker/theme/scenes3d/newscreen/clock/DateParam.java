package com.locker.theme.scenes3d.newscreen.clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.locker.theme.scenes3d.Scenes3DCore;
import com.locker.theme.scenes3d.newscreen.core.FontManage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xff on 2018/1/5.
 */

public class DateParam extends Param {
    public String dateFormat;
    public String gravity;
    boolean isInit = false;
    public int color;
    String targetFormatParam = "";
    String fontName = "defaultFont";

    String stroke="dontStroke";
    int strokeSize=0;
    @Override
    public void draw(Canvas canvas, Paint paint) {
        dateFormat = alias;
        gravity = suffix;
        if (TextUtils.isEmpty(dateFormat) || TextUtils.isEmpty(gravity)) {
            return;
        }
        if (!isInit) {
            isInit = true;
            String[] dateFormats = dateFormat.split("\\|");
            if (isZh(Scenes3DCore.scenes3DCore.hostContext)) {
                targetFormatParam = dateFormats[0];
            } else {
                targetFormatParam = dateFormats[1];
            }
            color = Color.parseColor(dateFormats[2]);
            if (dateFormats.length >= 4) {
                String fordFormat = dateFormats[3];
                if (!TextUtils.isEmpty(fordFormat)) {
                    fontName = fordFormat;
                }
            }

            if(dateFormats.length>=5){
                String thirdFormat = dateFormats[4];
                if(!TextUtils.isEmpty(thirdFormat)){
                    stroke=thirdFormat;
                }
            }

            if(dateFormats.length>=6){
                String thirdFormat = dateFormats[5];
                if(!TextUtils.isEmpty(thirdFormat)){
                    strokeSize=Integer.parseInt(thirdFormat);
                }
            }

        }

        if(!"".equals(fontName)) {
            Typeface typeface= FontManage.get().getFont(fontName);
            if(typeface!=null){
                paint.setTypeface(typeface);
            }
        }else{
            paint.setTypeface(null);
        }

        canvas.save();
        if (isFlipV && isFlipH) {
            canvas.scale(-1, -1, dst.centerX(), dst.centerY());
        } else if (isFlipV && !isFlipH) {

            canvas.scale(1, -1, dst.centerX(), dst.centerY());

        } else if (!isFlipV && isFlipH) {
            canvas.scale(-1, 1, dst.centerX(), dst.centerY());
        }

        String targetFormat = formatDate(targetFormatParam);
        paint.setTextSize(height);
        //获取文字的大小
        paint.getTextBounds(targetFormat, 0, targetFormat.length(), src);
        int twidth = src.width();
        int theight = src.height();
        int offsetX = 0;
        int offsetY = (height - theight) / 2;
        if (gravity.equals("right")) {
            offsetX = width - twidth;
        } else if (gravity.equals("center")) {
            offsetX = (width - twidth) / 2;
        }

        if(needStroke()){
            paint.setStrokeWidth(strokeSize);// 描边宽度
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.parseColor(stroke));
            canvas.drawText(targetFormat,x+ offsetX, y+offsetY+theight, paint);

            paint.setStrokeWidth(0);// 描边宽度
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            canvas.drawText(targetFormat,x+ offsetX, y+offsetY+theight, paint);
        }else{
            paint.setStrokeWidth(0);// 描边宽度
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            canvas.drawText(targetFormat,x+ offsetX, y+offsetY+theight, paint);
        }


        canvas.restore();
    }

    public static boolean isZh(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    public static String formatDate(String format) {
        SimpleDateFormat sdf2 = new SimpleDateFormat(format);
        Date d = new Date();
        return sdf2.format(d);
    }

    //需要描边
    public boolean needStroke(){
        return !"dontStroke".equals(stroke);
    }
}
