package xu.ferris.launcher3deffect.cube;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

import xu.ferris.launcher3deffect.GLReader;

/**
 * Created by Administrator on 2016/2/15.
 */
public class CubeGLView extends GLSurfaceView {
    private CubeRenderer glReader;

    public CubeGLView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        glReader=new CubeRenderer(context);
        setRenderer(glReader);

    }
    public void setRadio(float radio){
        glReader.yrot=radio*360;
    }
}
