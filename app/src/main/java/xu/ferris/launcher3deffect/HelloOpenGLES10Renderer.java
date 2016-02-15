package xu.ferris.launcher3deffect;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ferris on 2016/2/15.
 */
public class HelloOpenGLES10Renderer implements GLSurfaceView.Renderer {

    float  rotateQuad;


    // 正方形的四个顶点
    private FloatBuffer quateBuffer;

    /**
     * 初始化正方形
     *  将float数组转换存储在字节缓冲数组
     */
    private void initQuate() {

        float[] four = { -1f, -1f, 1f,
                          1f, -1f, 1f,
                         -1f, 1f, 1f,
                          1f, 1f, 1f, };

        ByteBuffer vbb = ByteBuffer.allocateDirect(
                //分配缓冲空间，一个float占4个字节
                four.length * 4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序， 其中ByteOrder.nativeOrder()是获取本机字节顺序
        quateBuffer = vbb.asFloatBuffer();//转换为float型
        quateBuffer.put(four);  //添加数据
        quateBuffer.position(0);  //设置数组的起始位置

    }



    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 设置背景色
        gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        initQuate();
        // 指定需要启用定点数组
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

    }

    //所有的绘图操作都在此方法中执行
    public void onDrawFrame(GL10 gl) {

        //清楚屏幕和深度缓存
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        //切换至模型观察矩阵
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        //重置当前的模型观察矩阵
        gl.glLoadIdentity();
        //确定视图点

        //相当于我们的脑袋位置在(0, 0, -5)处，眼睛望向(0f, 0f, 0f),即原点。
        // 后面的三个参数(0.0,1.0,0.0),y轴为1，其余为0，表示脑袋朝上，就是正常的情况。看到的情况如下图：
        GLU.gluLookAt(gl, 0f, 0f, 5f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // 创建一个旋转的正方形     沿X轴旋转
        gl.glRotatef(rotateQuad, 0.0f, 1f, 0f);
        //平移到中心
        gl.glTranslatef(0, 0,  -1f);
        // 画一个正方形
        gl.glColor4f(0.63671875f, 0.76953125f, 0.22265625f, 0.0f);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, quateBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        // 改变旋转角度
        rotateQuad += 0.5f;
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {

        //设置OpenGL的场景大小
        gl.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION); //设置投影矩阵
        gl.glLoadIdentity(); //设置矩阵为单位矩阵，相当于重置矩阵
        gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7); // 应用投影矩阵

    }

}
