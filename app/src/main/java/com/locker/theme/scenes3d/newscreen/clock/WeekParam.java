package com.locker.theme.scenes3d.newscreen.clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.locker.theme.scenes3d.Scenes3DCore;
import com.locker.theme.scenes3d.newscreen.core.FontManage;

import java.util.Locale;

/**
 * Created by xff on 2018/1/5.
 */

public class WeekParam extends Param {
    public String colorFormat;
    public String gravity;
    public int week = 0;

    public String format;
    String fontName = "defaultFont";

    String stroke="dontStroke";
    int strokeSize=0;
    @Override
    public void draw(Canvas canvas, Paint paint) {
        format = alias;
        gravity = suffix;
        if (TextUtils.isEmpty(format) || TextUtils.isEmpty(gravity)) {
            return;
        }
        String[] dateFormats = format.split("\\|");
        colorFormat = dateFormats[0];
        if (dateFormats.length >= 2) {
            String fordFormat = dateFormats[1];
            if (!TextUtils.isEmpty(fordFormat)) {
                fontName = fordFormat;
            }
        }
        if(!"".equals(fontName)) {
            Typeface typeface=FontManage.get().getFont(fontName);
            if(typeface!=null){
                paint.setTypeface(typeface);
            }
        }else{
            paint.setTypeface(null);
        }

        if(dateFormats.length>=3){
            String thirdFormat = dateFormats[2];
            if(!TextUtils.isEmpty(thirdFormat)){
                stroke=thirdFormat;
            }
        }

        if(dateFormats.length>=4){
            String thirdFormat = dateFormats[3];
            if(!TextUtils.isEmpty(thirdFormat)){
                strokeSize=Integer.parseInt(thirdFormat);
            }
        }

        canvas.save();
        if (isFlipV && isFlipH) {
            canvas.scale(-1, -1, dst.centerX(), dst.centerY());
        } else if (isFlipV && !isFlipH) {

            canvas.scale(1, -1, dst.centerX(), dst.centerY());

        } else if (!isFlipV && isFlipH) {
            canvas.scale(-1, 1, dst.centerX(), dst.centerY());
        }


        String targetFormat = getWeek(!isZh(Scenes3DCore.scenes3DCore.hostContext), week);

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
            paint.setColor(Color.parseColor(colorFormat));
            canvas.drawText(targetFormat,x+ offsetX, y+offsetY+theight, paint);
        }else{
            paint.setStrokeWidth(0);// 描边宽度
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.parseColor(colorFormat));
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


    String getWeek(boolean isEn, int index) {
        String week = "";
        switch (index) {
            case 1:
                week = isEn ? "Monday" : "星期一";
                break;
            case 2:
                week = isEn ? "Tuesday" : "星期二";
                break;
            case 3:
                week = isEn ? "Wednesday" : "星期三";
                break;
            case 4:
                week = isEn ? "Thursday" : "星期四";
                break;
            case 5:
                week = isEn ? "Friday" : "星期五";
                break;
            case 6:
                week = isEn ? "Saturday " : "星期六";
                break;
            case 7:
                week = isEn ? "Sunday" : "星期日";
                break;

        }
        return week;
    }

    //需要描边
    public boolean needStroke(){
        return !"dontStroke".equals(stroke);
    }
}
