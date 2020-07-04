package com.locker.theme.scenes3d.newscreen.core;

import android.graphics.Typeface;

import com.badlogic.gdx.utils.Disposable;
import com.locker.theme.scenes3d.Scenes3DCore;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xff on 2018/9/12.
 */

public class FontManage implements Disposable {
   final Map<String,Typeface> typefaceMap=new HashMap<>();

   public static FontManage manage;
   public static FontManage get(){
        if(manage==null){
            manage=new FontManage();
        }
       return manage;
   }

   public Typeface getFont(String key){
       if(typefaceMap.containsKey(key))
           return typefaceMap.get(key);

       try {
           String fontPath = Scenes3DCore.getFileString("font" + File.separator + key);
           if (Scenes3DCore.scenes3DCore.iHost == null&&hasFileAsset()) {
               Typeface typeface = Typeface.createFromAsset(Scenes3DCore.scenes3DCore.hostContext.getAssets(), fontPath);
               if (typeface != null) {
                   typefaceMap.put(key, typeface);
                   return typeface;
               }

           }

           File file = new File(fontPath);
           if (file.exists()) {
               Typeface typeface = Typeface.createFromFile(file.getAbsolutePath());
               if (typeface != null) {
                   typefaceMap.put(key, typeface);
                   return typeface;
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }
       return null;
   }

    @Override
    public void dispose() {
        typefaceMap.clear();
    }

    public boolean hasFileAsset(){
        try {
            String[] fonts=Scenes3DCore.scenes3DCore.hostContext.getAssets().list("font");
            return fonts!=null&&fonts.length>0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
