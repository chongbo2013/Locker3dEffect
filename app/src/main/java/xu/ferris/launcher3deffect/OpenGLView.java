package xu.ferris.launcher3deffect;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

/**
 * Created by Administrator on 2016/2/15.
 */
public class OpenGLView extends GLSurfaceView {
    private GLReader glReader;

    public OpenGLView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        glReader=new GLReader();
        setRenderer(glReader);

        setEGLConfigChooser(8, 8, 8, 8, 16, 0);

       getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
    }
    public void setRadio(float radio){
        glReader.yrot=radio*360;
    }
}
