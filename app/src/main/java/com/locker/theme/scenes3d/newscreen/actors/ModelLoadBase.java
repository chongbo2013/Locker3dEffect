package com.locker.theme.scenes3d.newscreen.actors;

import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.GdxSurfaceTexture;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OESTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.CubemapAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FacedCubemapData;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.locker.theme.scenes3d.Scenes3DCore;
import com.locker.theme.scenes3d.newscreen.beans.ModelBean;
import com.locker.theme.scenes3d.newscreen.core.MultiPassShader;
import com.locker.theme.scenes3d.newscreen.core.ShaderLoader;
import com.locker.theme.scenes3d.newscreen.face.Face;
import com.locker.theme.scenes3d.newscreen.face.VideoFace;

/**
 * 3D模型加载器
 * 暂时不支持多个环境CUBEMAP，节省内存，加快速度
 * 2017-12-25 14:22:17 ferris.xu
 * https://github.com/libgdx/libgdx/wiki/Material-and-environment#blendingattribute
 */
public class ModelLoadBase {
    //模型共用资源
    public static DirectionalLight dirLight;
    public static FileHandle shaderRoot;


    public ModelBatch shaderBatch;
    public TestShaderProvider shaderProvider;
    public Cubemap cubemap;
    public Environment environment;
    //独立资源
    protected boolean loading = false;
    protected final Matrix4 transform = new Matrix4();
    protected String currentModel = null;
    protected ObjectMap<ModelInstance, AnimationController> animationControllers = new ObjectMap<ModelInstance, AnimationController>();
    protected Array<ModelInstance> instances = new Array<ModelInstance>();
    protected final BoundingBox bounds = new BoundingBox();


    ModelBean modelBean;


    public FileHandle getFileHanlePath(String path) {
        return Scenes3DCore.scenes3DCore.iHost == null ? Gdx.files.internal(path) : Gdx.files.absolute(Scenes3DCore.scenes3DCore.fixPath + path);
    }

    public ModelLoadBase(ModelBean modelBean) {
        this.modelBean = modelBean;

        if (shaderRoot == null)
            shaderRoot = Scenes3DCore.scenes3DCore.iHost == null ? Gdx.files.internal("data/g3d/shaders") : Gdx.files.absolute(Scenes3DCore.scenes3DCore.fixPath + "data/g3d/shaders");

        if (!modelBean.isObj) {
            blendingAttribute = new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, 1.0f);
        } else {
            blendingAttribute = new BlendingAttribute(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA, 1.0f);
        }
         depthTestAttribute = new DepthTestAttribute(GL20.GL_LEQUAL, modelBean.enableDepthTest);
         alphaAttribute = FloatAttribute.createAlphaTest(modelBean.alphaTest);
         cullFaceAttribute = IntAttribute.createCullFace(modelBean.isCullBackFace?GL20.GL_BACK:GL20.GL_FRONT);
        if (!modelBean.isObj)
            shaderProvider = new TestShaderProvider();


        if (shaderProvider != null) {
            shaderBatch = new ModelBatch(shaderProvider);
        } else {
            shaderBatch = new ModelBatch(getFileHanlePath("data/g3d/shaders/default.vertex.glsl"), getFileHanlePath("data/g3d/shaders/default.fragment.glsl"));
        }
        //设置着色器
        setShader("test");
        //环境贴图
        setEnvironment(modelBean.environmentName);
        //加载模型
        loadModel(modelBean.resource);
    }

    //反光板效果和视频模型的效果，需要在g3dj里面手动配置，才能生效

    Texture textureObj;
    TextureAttribute overTextureAttribute;
    BlendingAttribute blendingAttribute;
    DepthTestAttribute depthTestAttribute;
    FloatAttribute alphaAttribute;
    IntAttribute cullFaceAttribute;

    //https://github.com/libgdx/libgdx/wiki/Material-and-environment#blendingattribute
    protected void onLoaded() {
        instances.clear();
        animationControllers.clear();

        final ModelInstance instance = new ModelInstance(Scenes3DCore.scenes3DCore.assets.get(currentModel, Model.class), transform);
        instances.add(instance);
        if (instance.animations.size > 0)
            animationControllers.put(instance, new AnimationController(instance));
        instance.calculateBoundingBox(bounds);
        //获取模型的包围盒
        bounds.getDimensions(dimensions);


        for (Material m : instance.materials) {
            m.set(blendingAttribute);
            m.set(depthTestAttribute);
            m.set(alphaAttribute);
            m.set(cullFaceAttribute);
            //如果加载的是obj的模型
            if (modelBean.isObj && modelBean.objTexture != null) {
                FileHandle texHandle = Scenes3DCore.scenes3DCore.iHost == null ? Gdx.files.internal(modelBean.objTexture) : Gdx.files.absolute(Scenes3DCore.scenes3DCore.fixPath + modelBean.objTexture);
                textureObj = new Texture(texHandle);
                m.set(TextureAttribute.createDiffuse(textureObj));
            }

            if (m.has(TextureAttribute.Over)) {
                Attribute overAttribute = m.get(TextureAttribute.Over);
                if (overAttribute != null) {
                    overTextureAttribute = (TextureAttribute) overAttribute;
                }
            }
            // find 漫反射 face
            if (modelBean.face != null) {
                for (Face face : modelBean.face) {
                    if (m.id.equals(face.objectName)) {
                        face.textureFaceAttribute = ((TextureAttribute) m.get(TextureAttribute.Diffuse));

                    }
                }
            }
            //find 凹凸纹理 face
            if (modelBean.normalFace != null) {
                for (Face face : modelBean.normalFace) {
                    if (m.id.equals(face.objectName)) {
                        face.textureFaceAttribute = ((TextureAttribute) m.get(TextureAttribute.Normal));

                    }
                }


            }


            //find videoface
            if (modelBean.videoface != null) {
                for (Face face : modelBean.videoface) {
                    if (m.id.equals(face.objectName)) {
                        face.textureFaceAttribute = ((TextureAttribute) m.get(TextureAttribute.Video));
                        face.textureFaceAttribute.textureDescription.texture=((VideoFace)face).gdxVideoPlayer.videoTexture.texture;
                    }
                }
            }


        }
    }

    protected void loadModel(final String name) {
        if (name == null) return;
        currentModel = name;
        Scenes3DCore.scenes3DCore.assets.load(currentModel, Model.class);
        loading = true;
    }


    //设置shaer
    public void setShader(String name) {
        if (shaderProvider == null)
            return;
        shaderProvider.error = false;
        if (name.equals("<default>")) {
            shaderProvider.config.vertexShader = null;
            shaderProvider.config.fragmentShader = null;
            shaderProvider.name = "default";
        } else {
            ShaderLoader loader = new ShaderLoader(shaderRoot);
            shaderProvider.config.vertexShader = loader.load(name + ".glsl:VS");
            shaderProvider.config.fragmentShader = loader.load(name + ".glsl:FS");
            shaderProvider.name = name;
        }
        shaderProvider.clear();
    }


    public void setEnvironment(String name) {
        if (TextUtils.isEmpty(name) || shaderProvider == null) return;


        if (environment == null) {
            environment = new Environment();
            environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
            environment.add(dirLight = new DirectionalLight().set(1f, 1f, 1f, -0.5f, -1.0f, -0.8f));
        }
        if (cubemap != null) {
            cubemap.dispose();
            cubemap = null;
        }
        if (!modelBean.enablecubemap||name.equals("<none>")) {
            if (environment.has(CubemapAttribute.EnvironmentMap)) {
                environment.remove(CubemapAttribute.EnvironmentMap);
                shaderProvider.clear();
            }
        } else {
            FileHandle root = Scenes3DCore.scenes3DCore.iHost == null ? Gdx.files.internal("data/g3d/environment") : Gdx.files.absolute(Scenes3DCore.scenes3DCore.fixPath + "data/g3d/environment");
            FacedCubemapData faces = new FacedCubemapData(root.child(name + "_PX.png"), root.child(name + "_NX.png"),
                    root.child(name + "_PY.png"), root.child(name + "_NY.png"), root.child(name + "_PZ.png"),
                    root.child(name + "_NZ.png"), false); // FIXME mipmapping on desktop
            cubemap = new Cubemap(faces);
            faces.load(Cubemap.CubemapSide.NegativeX, root.child(name + "_NX.png"));
            cubemap.load(faces);
            //fix cubemap 没有添加到管理，导致onpause后纹理无法重新加载问题
            Cubemap.addManagedCubemap(Gdx.app, cubemap);
            if (!environment.has(CubemapAttribute.EnvironmentMap)) shaderProvider.clear();
            environment.set(new CubemapAttribute(CubemapAttribute.EnvironmentMap, cubemap));
        }
    }


    Vector3 dimensions = new Vector3();

    /**
     * 模型是否加载完成
     *
     * @return
     */
    public boolean isLoaded() {
        return !loading;
    }

    Vector3 rotatexyzTemp = new Vector3();
    Vector3 rotatexyz = new Vector3();
    //重力感应相关
    float tsfromuvTime = 0f;
    Vector3 acceleromoterXvecor = null;
    Vector3 acceleromoterTempvecor = null;

    public void render(Actor actor, Batch batch, Camera camera, float x, float y, float z, float width, float height, float thickness, float scacleX, float scacleY, float scacleZ, float ratationX, float ratationY, float ratationZ, float originWidth, float originHeight, float originThickness, int scaleMode, float lerp, float parentAlpha) {
        //透明度
        if (loading && Scenes3DCore.scenes3DCore.assets.isLoaded(modelBean.resource, Model.class)) {
            loading = false;
            onLoaded();
        }

        if (loading)
            return;
        //更改漫反射纹理
        if (modelBean.face != null) {
            for (Face face : modelBean.face) {
                face.update(actor);
                if (face.textureFaceAttribute != null && face.getFace() != null) {
                    face.textureFaceAttribute.set(face.getFace());
                }
            }
        }
        //更改 凹凸纹理
        if (modelBean.normalFace != null) {
            for (Face face : modelBean.normalFace) {
                face.update(actor);
                if (face.textureFaceAttribute != null && face.getFace() != null) {
                    face.textureFaceAttribute.set(face.getFace());
                }
            }
        }

        //更新视频 纹理
        if (modelBean.videoface != null) {
            for (Face face : modelBean.videoface) {
                face.update(actor);
            }
        }

        //overTextureAttribute 反光板根据重力平移光柱
        if (overTextureAttribute != null && overTextureAttribute.textureDescription != null) {
            if (acceleromoterXvecor == null)
                acceleromoterXvecor = new Vector3();
            if (acceleromoterTempvecor == null)
                acceleromoterTempvecor = new Vector3();
            //重力控制左右 +-
            float accelerometerX = Gdx.input.getAccelerometerX();
            float accelerometerY = Gdx.input.getAccelerometerY();
            float accelerometerZ = Gdx.input.getAccelerometerZ();
            //动画插值
            acceleromoterTempvecor.set(accelerometerX, accelerometerY, accelerometerZ);
            acceleromoterXvecor.lerp(acceleromoterTempvecor, 0.1f);
            //最终的值
            float step = 0.5f;
            float center = (1.f - step) / .2f;
            tsfromuvTime = center - MathUtils.clamp(center / 10f * acceleromoterXvecor.x, -center, center);
            float u1 = tsfromuvTime;
            float v1 = 0f;
            float u2 = tsfromuvTime + step;
            float v2 = 1f;
            overTextureAttribute.offsetU = u1;
            overTextureAttribute.offsetV = v1;
            overTextureAttribute.scaleU = u2 - u1;
            overTextureAttribute.scaleV = v2 - v1;
        }

        transform.idt();
        transform.mul(batch.getTransformMatrix());
        transform.translate(x, y, z);

        rotatexyzTemp.set(ratationX, ratationY, ratationZ);
        rotatexyz.lerp(rotatexyzTemp, lerp);
        transform.rotate(Vector3.X, rotatexyz.x);
        transform.rotate(Vector3.Y, rotatexyz.y);
        transform.rotate(Vector3.Z, rotatexyz.z);

        //fix 宽高获取不到的时候导致模型消失问题
        if (dimensions.x <= 0 || dimensions.y <= 0 || dimensions.z <= 0) {
            float scaclew = width / originWidth;
            float scacleh = height / originHeight;
            float scaclez = thickness / originThickness;


            if (scaleMode == 0)
                transform.scale(scaclew * scacleX, scacleh * scacleY, scaclez * scacleZ);
            else if (scaleMode == 1)
                transform.scale(scaclew * scacleX, scaclew * scacleY, scaclew * scacleZ);
            else
                transform.scale(scacleh * scacleX, scacleh * scacleY, scacleh * scacleZ);

        } else {
            float scaclew = width / dimensions.x;
            float scacleh = height / dimensions.y;
            float scaclez = thickness / dimensions.z;
            if (scaleMode == 0)
                transform.scale(scaclew * scacleX, scacleh * scacleY, scaclez * scacleZ);
            else if (scaleMode == 1)
                transform.scale(scaclew * scacleX, scaclew * scacleY, scaclew * scacleZ);
            else
                transform.scale(scacleh * scacleX, scacleh * scacleY, scacleh * scacleZ);
        }

        if (blendingAttribute != null) {
            blendingAttribute.opacity = parentAlpha;
        }


        shaderBatch.begin(camera);
        //解决带透明度
        if (environment != null)
            shaderBatch.render(instances, environment);
        else
            shaderBatch.render(instances);
        shaderBatch.end();

    }


    public void modelDispose() {
        if (shaderBatch != null)
            shaderBatch.dispose();
        shaderBatch = null;
        if (shaderProvider != null)
            shaderProvider.dispose();
        shaderProvider = null;

        if (cubemap != null)
            cubemap.dispose();
        cubemap = null;

        if (environment != null)
            environment.clear();
        environment = null;
        dirLight = null;
        shaderRoot = null;


    }

    public void dispose() {
        if (modelBean.face != null) {
            for (Face face : modelBean.face) {
                face.dispose();
            }
            modelBean.face.clear();
            modelBean.face = null;
        }

        if (modelBean.normalFace != null) {
            for (Face face : modelBean.normalFace) {
                face.dispose();
            }
            modelBean.normalFace.clear();
            modelBean.normalFace = null;
        }


        if (modelBean.videoface != null) {
            for (Face face : modelBean.videoface) {
                face.dispose();
            }
            modelBean.videoface.clear();
            modelBean.videoface = null;
        }
        modelBean = null;
        if (textureObj != null) {
            textureObj.dispose();
        }
        textureObj = null;
        modelDispose();
    }

    /**
     * 获取子模型
     *
     * @param subObjectName
     * @return
     */
    public Node getNode(String subObjectName) {
        for (ModelInstance instance : instances) {
            if (instance.nodes != null && instance.nodes.size > 0) {
                for (int i = 0; i < instance.nodes.size; i++) {
                    Node t = instance.nodes.get(i);
                    if (t.id.equals(subObjectName)) {
                        return t;
                    }
                }
            }
        }
        return null;
    }


    public static class TestShaderProvider extends DefaultShaderProvider {
        public boolean error = false;
        public String name = "default";

        public void clear() {
            for (final Shader shader : shaders)
                shader.dispose();
            shaders.clear();
        }

        public boolean revert() {
            if (config.vertexShader == null || config.fragmentShader == null) return false;
            config.vertexShader = null;
            config.fragmentShader = null;
            clear();
            return true;
        }

        @Override
        public Shader getShader(Renderable renderable) {
            try {
                return super.getShader(renderable);
            } catch (Throwable e) {
                if (!revert()) {
                    throw new GdxRuntimeException("Error creating shader, cannot revert to default shader", e);
                }
                error = true;
                return super.getShader(renderable);
            }
        }

        @Override
        protected Shader createShader(Renderable renderable) {
            BaseShader result = new MultiPassShader(renderable, config);
            return result;
        }
    }
}
