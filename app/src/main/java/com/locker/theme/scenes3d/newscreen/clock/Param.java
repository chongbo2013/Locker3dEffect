package com.locker.theme.scenes3d.newscreen.clock;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.badlogic.gdx.utils.Disposable;
import com.locker.theme.scenes3d.Scenes3DCore;
import com.locker.theme.scenes3d.newscreen.core.AssetUtil;

/**
 * Created by xff on 2017/12/27.
 */

public  class Param  implements Disposable {
    //类型
    public int type=-1;
    protected Bitmap bitmap=null;
    //获取图片资源
    //文件后缀
    public String suffix;
    //文件别名
    public String alias;
    //图片标识
    public String spot;

    public String resource;

    public boolean isFlipV=false;
    public boolean isFlipH=false;

    public int x;
    public int y;
    public int width;
    public int height;

    public static final Rect src=new Rect();
    public static final  Rect dst=new Rect();

    public String getImageFile(String numble){
        return Scenes3DCore.getFileString(resource+alias+numble+"."+suffix);
    }

    public Bitmap getBitmap(){
        bitmap= Scenes3DCore.scenes3DCore.iHost==null? AssetUtil.bitmapFromAssets(Scenes3DCore.scenes3DCore.hostContext,getImageFile(spot)):BitmapFactory.decodeFile(getImageFile(spot));
        return bitmap;
    }

    @Override
    public void dispose() {
        if(bitmap!=null&&!bitmap.isRecycled()){
            bitmap.recycle();
        }
        bitmap=null;
    }

    public void draw(Canvas canvas, Paint paint) {
        Bitmap temp = getBitmap();
        if (temp != null) {
            canvas.save();
            src.set(0,0,temp.getWidth(),temp.getHeight());
            dst.set(x,y,x+width,y+height);
            if(isFlipV&&isFlipH){
                canvas.scale(-1, -1, dst.centerX(), dst.centerY());
            }else if(isFlipV&&!isFlipH){
                canvas.scale(1, -1, dst.centerX(), dst.centerY());
            }else if(!isFlipV&&isFlipH){
                canvas.scale(-1, 1, dst.centerX(), dst.centerY());
            }
            canvas.drawBitmap(temp, src, dst, paint);
            canvas.restore();
        }
    }
}
