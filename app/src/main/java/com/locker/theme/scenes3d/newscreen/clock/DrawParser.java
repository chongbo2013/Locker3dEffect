package com.locker.theme.scenes3d.newscreen.clock;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xff on 2017/12/27.
 */

public abstract class DrawParser implements Disposable {
    public int width = 0;
    public int height = 0;
    public Bitmap bitmap;
    protected List<Param> params = new ArrayList<>();

    public void addParam(Param param) {
        params.add(param);
    }

    public Bitmap draw() {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        canvas.save();
        for (Param p : params) {
            p.draw(canvas,paint);
        }
        canvas.restore();
        canvas.setBitmap(null);
        return bitmap;
    }


    @Override
    public void dispose() {
        for (Param p : params) {
            p.dispose();
        }
        params.clear();
        params = null;

        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
        bitmap = null;
    }
    public void clear() {
        for (Param p : params) {
            p.dispose();
        }
        if (bitmap != null && !bitmap.isRecycled())
            bitmap.recycle();
        bitmap = null;
    }
}
