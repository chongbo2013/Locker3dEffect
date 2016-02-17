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

    Bitmap bmp,sideBimap;

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

    float lightAmbient[] = new float[] { 0.5f, 0.5f, 0.6f, 1.0f };  //环境光
    float lightDiffuse[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };//漫反射光
    float[] lightPos = new float[] {0,0,3,1};  //光源位置
    /*
     * 因为进行光照处理，你必须告知系统你定义的模型各个面的方向，以便系统计算光影情况，方向的描述是通过向量点来描述的
     */
    float norms[] = new float[] { //法向量数组，用于描述个顶点的方向，以此说明各个面的方向
            // FRONT
            0f,  0f,  1f, //方向为(0,0,0)至(0,0,1)即Z轴正方向
            0f,  0f,  1f,
            0f,  0f,  1f,
            0f,  0f,  1f,
            // BACK
            0f,  0f,  -1f,
            0f,  0f,  -1f,
            0f,  0f,  -1f,
            0f,  0f,  -1f,
            // LEFT
            -1f,  0f,  0f,
            -1f,  0f,  0f,
            -1f,  0f,  0f,
            -1f,  0f,  0f,
            // RIGHT
            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,
            1f, 0f, 0f,
            // TOP
            0f,  1f, 0f,
            0f,  1f, 0f,
            0f,  1f, 0f,
            0f,  1f, 0f,
            // BOTTOM
            0f,  -1f, 0f,
            0f,  -1f, 0f,
            0f,  -1f, 0f,
            0f,  -1f, 0f
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
    FloatBuffer normBuff;
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
        normBuff = makeFloatBuffer(norms);
        texBuff = makeFloatBuffer(texCoords);
        bmp = BitmapFactory.decodeResource(c.getResources(), R.drawable.splash);
        sideBimap= BitmapFactory.decodeResource(c.getResources(), R.drawable.test_1);
    }

    private int[] textureids=null;
    protected void init(GL10 gl) {
        gl.glClearColor(0,0,0,0);//设置清屏时背景的颜色，R，G，B，A
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_LIGHTING); //启用光照
        gl.glEnable(GL10.GL_LIGHT0);  //开启光源0
        //设置光照参数，也可以使用默认的，不设置
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);

        gl.glNormalPointer(GL10.GL_FLOAT, 0, normBuff);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

        //使用纹理步骤：
        // 1.开启贴图
        gl.glEnable(GL10.GL_TEXTURE_2D);

        // 2.生成纹理ID
        textureids = new int[2];//尽管只有一个纹理，但使用一个元素的数组
        //glGenTextures(申请个数，存放数组，偏移值）
        gl.glGenTextures(2, textureids, 0); //向系统申请可用的，用于标示纹理的ID



        //3.绑定纹理，使得指定纹理处于活动状态。一次只能激活一个纹理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[0]);
        //4.绑定纹理数据，传入指定图片
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);


        gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        gl.glEnable(GL10.GL_DEPTH_TEST); //启用深度缓存
        gl.glEnable(GL10.GL_CULL_FACE);  //启用背面剪裁
        gl.glClearDepthf(1.0f);    // 设置深度缓存值
        gl.glDepthFunc(GL10.GL_LEQUAL);  // 设置深度缓存比较函数，GL_LEQUAL表示新的像素的深度缓存值小于等于当前像素的深度缓存值（通过gl.glClearDepthf(1.0f)设置）时通过深度测试
        gl.glShadeModel(GL10.GL_SMOOTH);// 设置阴影模式GL_SMOOTH


        //3.绑定纹理，使得指定纹理处于活动状态。一次只能激活一个纹理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[1]);
        //4.绑定纹理数据，传入指定图片
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, sideBimap, 0);







        //5.传递各个顶点对应的纹理坐标
//        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuff);
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); //开启纹理坐标数组

        //6.设置纹理参数 （可选）
        /*下面的两行参数告诉OpenGL在显示图像时，当它比放大得原始的纹理大
        *( GL_TEXTURE_MAG_FILTER )或缩小得比原始得纹理小( GL_TEXTURE_MIN_FILTER )
        *时OpenGL采用的滤波方式。通常这两种情况下我都采用 GL_LINEAR 。这使得纹理从很远处
        *到离屏幕很近时都平滑显示。使用 GL_LINEAR 需要CPU和显卡做更多的运算。如果您的机器很慢，
        *您也许应该采用 GL_NEAREST 。过滤的纹理在放大的时候，看起来马赛克的很。您也可以结合这
        *两种滤波方式。在近处时使用 GL_LINEAR ，远处时 GL_NEAREST 。
        **/
        gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        gl.glEnable(GL10.GL_DEPTH_TEST); //启用深度缓存
        gl.glEnable(GL10.GL_CULL_FACE);  //启用背面剪裁
        gl.glClearDepthf(1.0f);    // 设置深度缓存值
        gl.glDepthFunc(GL10.GL_LEQUAL);  // 设置深度缓存比较函数，GL_LEQUAL表示新的像素的深度缓存值小于等于当前像素的深度缓存值（通过gl.glClearDepthf(1.0f)设置）时通过深度测试
        gl.glShadeModel(GL10.GL_SMOOTH);// 设置阴影模式GL_SMOOTH
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

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuff);//设置顶点数据
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);// 使用纹理数组
       gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuff);

        gl.glRotatef(yrot, 0, 1, 0);



        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[0]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);  //绘制正方型FRONT面

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureids[1]);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

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