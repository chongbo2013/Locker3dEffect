package xu.ferris.launcher3deffect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import xu.ferris.launcher3deffect.cube.CubeGLView;
import xu.ferris.launcher3deffect.cube2.GLImage;
import xu.ferris.launcher3deffect.cube2.GLView;

public class EffectActivity extends AppCompatActivity {

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
