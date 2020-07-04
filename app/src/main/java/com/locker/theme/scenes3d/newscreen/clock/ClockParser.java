package com.locker.theme.scenes3d.newscreen.clock;

import android.graphics.Bitmap;

import com.badlogic.gdx.backends.android.TimeInfo;

/**
 * 时钟绘制解析
 * Created by xff on 2017/12/27.
 */

public class ClockParser extends DrawParser{


    /**
     * 更新时间信息
     * @param timeInfo
     */
    public void update(TimeInfo timeInfo){
        String hour = timeInfo.hour < 10 ? "0" + timeInfo.hour : String.valueOf(timeInfo.hour);
        String minute = timeInfo.minute < 10 ? "0" + timeInfo.minute : String.valueOf(timeInfo.minute);
        char[] hours = hour.toCharArray();
        char[] minutes = minute.toCharArray();
        for(Param p:params){
            switch (p.type){
                case 0://time1
                    p.spot="bg";
                    break;
                case 1://time1
                    p.spot=String.valueOf(hours[0]);
                    break;
                case 2://time2
                    p.spot=String.valueOf(hours[1]);
                    break;
                case 3://time1
                    p.spot="dot";
                    break;
                case 4://time3
                    p.spot=String.valueOf(minutes[0]);
                    break;
                case 5://time4
                    p.spot=String.valueOf(minutes[1]);
                    break;
                case 7://周
                    ((WeekParam)p).week=timeInfo.dayOfWeek;
                    break;
                case 8://周
                    ((WeatherParam)p).temperature=timeInfo.temperature;
                    ((WeatherParam)p).weather=timeInfo.weather;
                    break;
            }
        }
    }
    @Override
    public Bitmap draw() {
        return super.draw();
    }
}
