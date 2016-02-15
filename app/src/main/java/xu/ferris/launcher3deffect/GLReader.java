package xu.ferris.launcher3deffect;


import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLReader implements Renderer{

    float box[] = new float[] {
            // FRONT
            -0.5f, -0.5f,  0.5f,
            0.5f, -0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            // BACK
            -0.5f, -0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            // LEFT
            -0.5f, -0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            // RIGHT
            0.5f, -0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            // TOP
            -0.5f,  0.5f,  0.5f,
            0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f,
            0.5f,  0.5f, -0.5f,
            // BOTTOM
            -0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f, -0.5f, -0.5f,
    };

    FloatBuffer cubeBuff;

    float xrot = 0.0f;
    float yrot = 0.0f;
    /**
     * 将float数组转换存储在字节缓冲数组
     * @param arr
     * @return
     */
    public FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);//分配缓冲空间，一个float占4个字节
        bb.order(ByteOrder.nativeOrder()); //设置字节顺序， 其中ByteOrder.nativeOrder()是获取本机字节顺序
        FloatBuffer fb = bb.asFloatBuffer(); //转换为float型
        fb.put(arr);        //添加数据
        fb.position(0);      //设置数组的起始位置
        return fb;
    }

    public GLReader(){
        cubeBuff = makeFloatBuffer(box);//转换float数组
    }

    //所有的绘图操作都在此方法中执行
    public void onDrawFrame(GL10 gl) {
        // TODO Auto-generated method stub

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);// 清除屏幕和深度缓存

        gl.glMatrixMode(GL10.GL_MODELVIEW);   //切换至模型观察矩阵
        gl.glLoadIdentity();// 重置当前的模型观察矩阵
        GLU.gluLookAt(gl, 0, 0, 3, 0, 0, 0, 0, 1, 0);//设置视点和模型中心位置

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuff);//设置顶点数据
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

//        gl.glRotatef(xrot, 1, 0, 0);  //绕着(0,0,0)与(1,0,0)即x轴旋转
        gl.glRotatef(yrot, 0, 1, 0);

        //六个面一个面绘制一种颜色
        gl.glColor4f(1.0f, 0, 0, 1.0f);   //设置颜色，红色
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);  //绘制正方型FRONT面
        gl.glColor4f(1.0f, 1.0f, 0, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);

        gl.glColor4f(0, 1.0f, 0, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
        gl.glColor4f(0, 1.0f, 1.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);

        gl.glColor4f(0, 0, 1.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);

//        xrot += 1.0f;
        yrot += 0.5f;
    }
    //当窗口大小改变时调用
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // TODO Auto-generated method stub

        //设置OpenGL的场景大小
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION); // 设置投影矩阵
        gl.glLoadIdentity();  //设置矩阵为单位矩阵，相当于重置矩阵
        GLU.gluPerspective(gl, 45.0f, ((float) width) / height, 0.1f, 10f);//设置透视范围
    }
    //当窗口被创建是调用
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // TODO Auto-generated method stub

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);//设置清屏时背景的颜色，R，G，B，A

        gl.glEnable(GL10.GL_DEPTH_TEST); //启用深度缓存
        gl.glEnable(GL10.GL_CULL_FACE);  //启用背面剪裁
        gl.glClearDepthf(1.0f);    // 设置深度缓存值
        gl.glDepthFunc(GL10.GL_LEQUAL);  // 设置深度缓存比较函数，GL_LEQUAL表示新的像素的深度缓存值小于等于当前像素的深度缓存值（通过gl.glClearDepthf(1.0f)设置）时通过深度测试
        gl.glShadeModel(GL10.GL_SMOOTH);// 设置阴影模式GL_SMOOTH
    }




}
