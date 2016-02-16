package xu.ferris.launcher3deffect.cube2;

import android.content.Context;
import android.opengl.GLSurfaceView;

import xu.ferris.launcher3deffect.cube.CubeRenderer;

/**
 * Created by Administrator on 2016/2/15.
 */
public class GLView extends GLSurfaceView {
    private GLRender glReader;

    public GLView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        glReader=new GLRender();
        setRenderer(glReader);
    }

}
