package xu.ferris.launcher3deffect;

import android.app.Activity;
import android.os.Bundle;

import xu.ferris.launcher3deffect.cube.CubeGLView;

public class EffectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        GLSurfaceView view=new HelloOpenGLES10SurfaceView(this);
//        OpenGLView view=new OpenGLView(this);
//        GLImage.load(this.getResources());
//        GLView view=new GLView(this);
//        view.setRadio(50);
        CubeGLView view=new CubeGLView(this);
        setContentView(view);
    }
}
