package com.locker.theme.scenes3d.newscreen.clock;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.locker.theme.scenes3d.newscreen.core.FontManage;

/**
 * 绘制天气
 * Created by xff on 2018/1/5.
 */

public class WeatherParam extends Param {

    public String temperature;
    public String gravity;
    public int color;
    public String weather;
    boolean isDrawWeather=true;
    boolean isInit=false;
    String fontName="defaultFont";
    String stroke="dontStroke";
    int strokeSize=0;
    @Override
    public void draw(Canvas canvas, Paint paint) {
        String format=alias;
        gravity=suffix;


        if(!isInit) {
            isInit=true;
            //解析
            String[] dateFormats = format.split("\\|");
            String firstFormat = dateFormats[0];
            String secondFormat = dateFormats[1];
            if ("temperature".equals(firstFormat)) {
                isDrawWeather = false;
            }
            color = Color.parseColor(secondFormat);
            if(dateFormats.length>=3){
                String thirdFormat = dateFormats[2];
                if(!TextUtils.isEmpty(thirdFormat)){
                    fontName=thirdFormat;
                }
            }

            if(dateFormats.length>=4){
                String thirdFormat = dateFormats[3];
                if(!TextUtils.isEmpty(thirdFormat)){
                    stroke=thirdFormat;
                }
            }

            if(dateFormats.length>=5){
                String thirdFormat = dateFormats[4];
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

        String targetFormat =isDrawWeather?weather:temperature;

        paint.setTextSize(height);
        //获取文字的大小
        paint.getTextBounds(targetFormat, 0, targetFormat.length(), src);
        int twidth = src.width();
        int theight = src.height();
        int offsetX = 0;
        int  offsetY = (height - theight) / 2;
        if (gravity.equals("right")) {
            offsetX = width - twidth;
        } else if (gravity.equals("center")){
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
    //需要描边
    public boolean needStroke(){
        return !"dontStroke".equals(stroke);
    }
}
