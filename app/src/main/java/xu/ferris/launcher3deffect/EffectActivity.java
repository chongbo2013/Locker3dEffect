package xu.ferris.launcher3deffect;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EffectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        GLSurfaceView view=new HelloOpenGLES10SurfaceView(this);
        OpenGLView view=new OpenGLView(this);
        setContentView(view);
    }
}
