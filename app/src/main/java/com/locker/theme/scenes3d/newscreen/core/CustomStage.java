package com.locker.theme.scenes3d.newscreen.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * 由于GLSurfaceView 设置透明后，产生的一些混合模式出现BUG，所以采取
 * 自定义shader，解决透明度混合问题。
 * 后面如果需要用到混合模式，需要另外做处理
 * Created by xff on 2017/4/26.
 */

public class CustomStage extends Stage {
    private PerspectiveCamera camera;
    CustomPolygonSpriteBatch batch;
    public boolean defaultShader=true;
    public CustomStage(Viewport viewport, Batch batch) {
        super(viewport,batch);
        this.batch= (CustomPolygonSpriteBatch) batch;

        float width= Gdx.graphics.getWidth();
        float height=Gdx.graphics.getHeight();
        float fieldOfView = 35f;
        float cameraZ = (float)( height / 2 / Math.tan( fieldOfView / 2 * MathUtils.degreesToRadians ) );
        float near = 100f;
        float far = cameraZ+height;
        camera = new PerspectiveCamera( fieldOfView , width , height );
        camera.near = near;
        camera.far = far;
        camera.position.set( width / 2 , height / 2 , cameraZ );
        camera.direction.set( 0 , 0 , -1 );
        viewport.setCamera(camera);
        camera.update();
    }

    @Override
    public void draw() {
        camera.update();
        if(batch!=null&&getRoot().isVisible()) {
            batch.setCustomBlend(CustomPolygonSpriteBatch.STAGE_BLEAN);
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            //选中用哪个shader
            batch.getShader().setUniformf("normalstutas",defaultShader?1.0f:0.0f);
            getRoot().draw(batch, 1.0f);
            batch.end();
        }
    }
    static Vector3 intersection = new Vector3(0, 0, 1);
    //解决触摸问题
    @Override
    public Vector2 screenToStageCoordinates (Vector2 screenCoords) {
        Ray pickRay = getViewport().getPickRay(screenCoords.x, screenCoords.y);

        if (Intersector.intersectRayPlane(pickRay, new Plane(Vector3.Z, Vector3.Zero), intersection)) {
            screenCoords.x = intersection.x;
            screenCoords.y = intersection.y;
        } else {
            screenCoords.x = Float.MAX_VALUE;
            screenCoords.y = Float.MAX_VALUE;
        }
        return screenCoords;
    }
}
