package xu.ferris.launcher3deffect;

import android.content.Context;
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
    }
}
