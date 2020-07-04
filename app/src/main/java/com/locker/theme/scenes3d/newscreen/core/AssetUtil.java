package com.locker.theme.scenes3d.newscreen.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.locker.theme.scenes3d.Scenes3DCore;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * Created by xff on 2017/5/31.
 */

public class AssetUtil {
    public static AssetManager mgr(Context context) {
        return context.getAssets();
    }

    public static InputStream read(Context context,String name) throws IOException {
        return readBuffer(context,name);
    }

    public static InputStream readBuffer(Context context,String name) throws IOException {
        return mgr(context).open(name, AssetManager.ACCESS_BUFFER);
    }

    public static InputStream readStream(Context context,String name) throws IOException {
        return mgr(context).open(name, AssetManager.ACCESS_STREAMING);
    }

    public static ZipInputStream readZip(Context context,String name) throws IOException {
        InputStream is = readStream(context,name);
        BufferedInputStream bis = new BufferedInputStream(is, 32 * 1024);
        return new ZipInputStream(bis);
    }

    public static Uri uri(String filename) {
        return Uri.parse("file:///android_asset/" + filename);
    }

    public static String[] list(Context context,String path) {
        try {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            return mgr(context).list(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[] {};
    }



    // 读取图片 , 适合小图片!! 设置密度是hdpi, 不支持9png!!!!
    public static Bitmap bitmapFromAssets(Context context, String path) {
        InputStream is = null;
        try {
            is = readBuffer(context,path);
            if (is != null) {
                Bitmap bmp = BitmapFactory.decodeStream(is);
//                if (bmp != null) {
//                    bmp.setDensity(DisplayMetrics.DENSITY_HIGH);
//                }
                return bmp;
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            close(is);
        }
        return null;
    }

    // //读取图片 , 适合小图片!! 设置密度是hdpi, 支持9png
    public static Drawable drawable(Context context,String path) {
        InputStream is = null;
        try {
            is = readBuffer(context,path);
            if (is != null) {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inScreenDensity = DisplayMetrics.DENSITY_HIGH;
                return Drawable.createFromResourceStream(context.getResources(), null, is, null, opts);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            close(is);
        }
        return null;
    }


    public static void close(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
        }
    }

    public static Bitmap readBitmapFromSdcard(String path){
        Bitmap bitmap = null;
        try {
            File f = new File(path);
            if (!f.exists()){
                return null;
            }
//            FileInputStream is = new FileInputStream(f);
//            byte[] b = new byte[is.available()];
//            is.read(b);
//            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            if(bitmap==null){
                bitmap=BitmapFactory.decodeFile(path);
            }
        } catch (Exception e) {
            bitmap=BitmapFactory.decodeFile(path);
            return  bitmap;

        }

        return bitmap;
    }
    public static void saveMyBitmap(Bitmap mBitmap, String path) {
        if(mBitmap==null||mBitmap.isRecycled())
            return;
        File f = new File(path);
        try {
            f.deleteOnExit();
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (mBitmap != null) {
// 保存格式为PNG 质量为100
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getNewPath(){
        return Scenes3DCore.scenes3DCore.iHost==null? Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/"+System.currentTimeMillis()+"3d.png":Scenes3DCore.scenes3DCore.fixPath+"/graduate.png";
    }
}
