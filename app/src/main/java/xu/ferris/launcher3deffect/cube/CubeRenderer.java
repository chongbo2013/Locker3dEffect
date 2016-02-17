package xu.ferris.launcher3deffect.cube;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;

import xu.ferris.launcher3deffect.R;

public class CubeRenderer implements Renderer {

    Bitmap bmp,sideBimap,fontBitmap;

    float box[] = new float[] {
            // FRONT
            -1f, -1f,  0.10f,
            1f, -1f,  0.10f,
            -1f,  1f,  0.10f,
            1f,  1f, 0.10f,
            // BACK
            1f, -1f, -0.10f,
            -1f,  -1f, -0.10f,
            1f, 1f, -0.10f,
           -1f,  1f, -0.10f,
            // LEFT
            -1f, -1f,  -0.10f,
            -1f,  -1f, 0.10f,
            -1f, 1f, -0.10f,
            -1f,  1f, 0.10f,
            // RIGHT
            1f, -1f, 0.10f,
            1f,  -1f, -0.10f,
            1f, 1f,  0.10f,
            1f,  1f,  -0.10f,
            // TOP
            -1f,  1f,  0.10f,
            1f,  1f,  0.10f,
            -1f,  1f, -0.10f,
            1f,  1f, -0.10f,
            // BOTTOM
            -1f, -1f, 0.10f,
            1f, -1f, 0.10f,
            -1f, -1f,  -0.10f,
            1f, -1f, -0.10f,
    };





    float texCoords[] = new float[] { //纹理坐标对应数组
            // FRONT
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            // BACK
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            // LEFT
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            // RIGHT
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
            // TOP
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            // BOTTOM
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f
    };


    FloatBuffer cubeBuff;
    FloatBuffer texBuff;

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

    public CubeRenderer(Context c) {
        // TODO Auto-generated constructor stub
        cubeBuff = makeFloatBuffer(box);//转换float数组
        texBuff = makeFloatBuffer(texCoords);
        bmp = BitmapFactory.decodeResource(c.getResources(), R.drawable.theme_icons);//需要绘制的面
        sideBimap= BitmapFactory.decodeResource(c.getResources(), R.drawable.gl_crystal_side);//侧面等
        fontBitmap= BitmapFactory.decodeResource(c.getResources(), R.drawable.gl_crystal_font);//前面
    }

    private int[] textureids=null;
    protected void init(GL10 gl) {
        gl.glClearColor(0, 0,0,0);//设置清屏时背景的颜色，R，G，B，A
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        //使用纹理步骤：
        // 1.开启贴图
        gl.glEnable(GL10.GL_TEXTURE_2D);

        // 2.生成纹理ID
        textureids = new int[3];//尽管只有一个纹理，但使用一个元素的数组
        //glGenTextures(申请个数，存放数组，偏移值）
        gl.glGenTextures(3, textureids, 0); //向系统申请可用的，用于标示纹理的ID



        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[0]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);


        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[1]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, sideBimap, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);


        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[2]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, fontBitmap, 0);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // TODO Auto-generated method stub
        init(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int w, int h) {
        // TODO Auto-generated method stub
        gl.glViewport(0, 0, w, h); //设置视窗
        gl.glMatrixMode(GL10.GL_PROJECTION); // 设置投影矩阵
        gl.glLoadIdentity();  //设置矩阵为单位矩阵，相当于重置矩阵
        GLU.gluPerspective(gl, 90.0f, ((float) w) / h, 0.1f, 10f);//设置透视范围
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // TODO Auto-generated method stub
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);// 清除屏幕和深度缓存
        gl.glMatrixMode(GL10.GL_MODELVIEW);   //切换至模型观察矩阵
        gl.glLoadIdentity();// 重置当前的模型观察矩阵
        GLU.gluLookAt(gl, 0, 0, 3, 0, 0, 0, 0, -1, 0);//设置视点和模型中心位置


        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);// 使用纹理数组

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuff);//设置顶点数据
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuff);

        gl.glRotatef(yrot, 0, 1, 0);



        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[2]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);  //绘制正方型FRONT面

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[0]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);




        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[2]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[0]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);



        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[1]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[1]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[1]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[1]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);



        xrot += 0.5f;
        yrot += 0.5f;
    }

}