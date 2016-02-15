package xu.ferris.launcher3deffect;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/2/15.
 */
public class HelloOpenGLES10SurfaceView extends GLSurfaceView {

    public HelloOpenGLES10SurfaceView(Context context) {
        super(context);
        init();
    }

    public HelloOpenGLES10SurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setRenderer(new HelloOpenGLES10Renderer());
    }
}
