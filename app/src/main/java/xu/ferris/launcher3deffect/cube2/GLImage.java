package xu.ferris.launcher3deffect.cube2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import xu.ferris.launcher3deffect.R;

public class GLImage
{
    public static Bitmap iBitmap;
    public static Bitmap jBitmap;
    public static Bitmap kBitmap;
    public static Bitmap lBitmap;
    public static Bitmap mBitmap;
    public static Bitmap nBitmap;
    public static Bitmap close_Bitmap;


    public static void load(Resources resources)
    {
        iBitmap = BitmapFactory.decodeResource(resources, R.drawable.splash);
        jBitmap = BitmapFactory.decodeResource(resources, R.drawable.splash);
        kBitmap = BitmapFactory.decodeResource(resources, R.drawable.splash);
        lBitmap = BitmapFactory.decodeResource(resources, R.drawable.splash);
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.splash);
        nBitmap = BitmapFactory.decodeResource(resources, R.drawable.splash);
        close_Bitmap = BitmapFactory.decodeResource(resources, R.drawable.splash);
    }
}
