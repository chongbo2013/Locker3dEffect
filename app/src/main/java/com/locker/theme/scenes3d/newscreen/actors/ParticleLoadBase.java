package com.locker.theme.scenes3d.newscreen.actors;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
import com.badlogic.gdx.graphics.g3d.particles.ParticleShader;
import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
import com.badlogic.gdx.graphics.g3d.particles.influencers.RegionInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ScaleInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.influencers.SpawnInfluencer;
import com.badlogic.gdx.graphics.g3d.particles.renderers.BillboardRenderer;
import com.badlogic.gdx.graphics.g3d.particles.values.PointSpawnShapeValue;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.locker.theme.scenes3d.Scenes3DCore;
import com.locker.theme.scenes3d.newscreen.beans.ParticleBean;

/**
 * Created by xff on 2018/9/7.
 */

public class ParticleLoadBase {
    //Simulation
    ParticleController emitters;

    //Rendering
//    Environment environment;
    BillboardParticleBatch billboardParticleBatch;


    public ModelBatch modelBatch;
    protected boolean loading = false;
    ParticleBean modelBean;
    String tex="";

    BlendingAttribute blendingAttribute = new BlendingAttribute(1, 771, 1.0F);
    protected final Matrix4 transform = new Matrix4();

    public ParticleLoadBase(ParticleBean modelBean) {
        this.modelBean = modelBean;
        modelBatch = new ModelBatch();
        tex = modelBean.resource;
        Scenes3DCore.scenes3DCore.assets.load(tex, Texture.class);
        emitters = new ParticleController();

//        environment = new Environment();
//        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0f, 0f, 0.1f, 1f));
//        environment.add(new DirectionalLight().set(1f, 1f, 1f, 0, -0.5f, -1));
        billboardParticleBatch = new BillboardParticleBatch(ParticleShader.AlignMode.Screen, false, 100, blendingAttribute, null);
        loading = true;
    }









    public void render(Batch batch, Camera camera, float x, float y, float z, float width, float height, float thickness, float scacleX, float scacleY, float scacleZ, float ratationX, float ratationY, float ratationZ, float originWidth, float originHeight, float originThickness, int scaleMode, float lerp, float parentAlpha) {

        if (loading && Scenes3DCore.scenes3DCore.assets.isLoaded(tex, Texture.class)) {
            loading = false;
            onLoaded();
        }
        if (loading)
            return;

        transform.idt();
        transform.mul(batch.getTransformMatrix());
        transform.translate(x,y,z);
        transform.translate(originWidth,originHeight,0);
        if(ratationX>0){
            transform.rotate(Vector3.X,ratationX);
            transform.translate(0,originThickness,0);
        }else if(ratationY>0){
            transform.rotate(Vector3.Y,ratationY);
            transform.translate(originThickness,0,0);
        }else if(ratationZ>0){
            transform.rotate(Vector3.Z,ratationZ);
            transform.translate(0,0,originThickness);
        }
        transform.scale(scacleX,scacleY,scacleZ);
        emitters.setTransform(transform);
        billboardParticleBatch.setCamera(camera);
        if (blendingAttribute != null) {
            blendingAttribute.opacity = parentAlpha*modelBean.color.a;
        }
        modelBatch.begin(camera);
        if (emitters != null) {
            //Update
            billboardParticleBatch.begin();
            emitters.update();
            emitters.draw();
            billboardParticleBatch.end();
            modelBatch.render(billboardParticleBatch/*, environment*/);
        }
        modelBatch.end();



    }

    private void onLoaded() {
        Texture particleTexture = Scenes3DCore.scenes3DCore.assets.get(tex);
        billboardParticleBatch.setTexture(particleTexture);
        emitters  = createBillboardController(new float[]{modelBean.color.r, modelBean.color.g, modelBean.color.b}, particleTexture);
        emitters.init();
        emitters.start();
    }


    private ParticleController createBillboardController(float[] colors, Texture particleTexture) {
        //Emission
        RegularEmitter emitter = new RegularEmitter();

        emitter.getDuration().setLow(50);//多久发射
        emitter.getEmission().setHigh(300);//一次发射多少粒子
        emitter.getLife().setHigh(200);//粒子活多久
        emitter.setMaxParticleCount(500);//总共多少粒子
        emitter.setContinuous(true);

        //Spawn
        PointSpawnShapeValue pointSpawnShapeValue = new PointSpawnShapeValue();
        pointSpawnShapeValue.xOffsetValue.setLow(0, 1f);
        pointSpawnShapeValue.xOffsetValue.setActive(false);
        pointSpawnShapeValue.yOffsetValue.setLow(0, 1f);
        pointSpawnShapeValue.yOffsetValue.setActive(false);
        pointSpawnShapeValue.zOffsetValue.setLow(0, 1f);
        pointSpawnShapeValue.zOffsetValue.setActive(false);
        SpawnInfluencer spawnSource = new SpawnInfluencer(pointSpawnShapeValue);

        //Scale
        ScaleInfluencer scaleInfluencer = new ScaleInfluencer();
        scaleInfluencer.value.setTimeline(new float[]{0, 1});
        scaleInfluencer.value.setScaling(new float[]{1, 0.5f});
        scaleInfluencer.value.setLow(0.5f);
        scaleInfluencer.value.setHigh(1);

        //Color
        ColorInfluencer.Single colorInfluencer = new ColorInfluencer.Single();
        colorInfluencer.colorValue.setColors(new float[]{colors[0], colors[1], colors[2], 0, 0, 0});
        colorInfluencer.colorValue.setTimeline(new float[]{0, 1});
        colorInfluencer.alphaValue.setHigh(1);
        colorInfluencer.alphaValue.setTimeline(new float[]{0, 0.5f,  1});
        colorInfluencer.alphaValue.setScaling(new float[]{0, 1f,  0});

        //Dynamics
        DynamicsInfluencer dynamicsInfluencer = new DynamicsInfluencer();
        DynamicsModifier.BrownianAcceleration modifier = new DynamicsModifier.BrownianAcceleration();
        modifier.strengthValue.setTimeline(new float[]{0, 1});
        modifier.strengthValue.setScaling(new float[]{0, 1});
        modifier.strengthValue.setHigh(80);
        modifier.strengthValue.setLow(1, 5);
        dynamicsInfluencer.velocities.add(modifier);

        return new ParticleController("Billboard Controller", emitter, new BillboardRenderer(billboardParticleBatch),
                new RegionInfluencer.Single(particleTexture),
                spawnSource,
                scaleInfluencer,
                colorInfluencer,
                dynamicsInfluencer
        );
    }

    public void dispose() {
        modelBatch.dispose();
//        environment.clear();

        emitters.dispose();


    }
}
