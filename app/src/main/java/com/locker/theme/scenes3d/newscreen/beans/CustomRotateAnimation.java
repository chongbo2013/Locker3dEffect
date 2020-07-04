package com.locker.theme.scenes3d.newscreen.beans;

import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;
import com.locker.theme.scenes3d.ScriptUtils;
import com.locker.theme.scenes3d.newscreen.actors.ModelView;
import com.locker.theme.scenes3d.newscreen.actors.View;
import com.locker.theme.scenes3d.newscreen.tween.BaseParserTween;

import java.util.Calendar;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;

/**
 * 自定义xyz旋转动画
 * 支持 extends View 控件使用
 * Created by xff on 2017/12/23.
 */

public class CustomRotateAnimation extends BaseParserTween {
    public String name;

    public String rotateXScript, rotateYScript, rotateZScript;
    public String subObjectName;
    public boolean isValueAdd, isSubValueAdd = false;
    public Node node;




    Vector3 acceleromoterTempvecor = new Vector3();
    Vector3 acceleromoterXYvecor = new Vector3();

    public float alpha = 0.05f;


    protected void reset() {
        totalTime = 0.0f;
    }

    //初始化 运算符等
    @Override
    public void load() {



    }

    float totalDeltaX = 0f;
    float totalDeltaY = 0f;
    int touchXDir, touchYDir;
    public static Calendar c = Calendar.getInstance();

    public String updateParameter(String str) {
        if (Gdx.input.isTouched()) {
            totalDeltaX += Gdx.input.getDeltaX();
            totalDeltaY += Gdx.input.getDeltaY();
        }

        if (str.contains("screenWidth")) {
            str = str.replaceAll("screenWidth", Gdx.graphics.getWidth() + "");
        }
        if (str.contains("screenHeight")) {
            str = str.replaceAll("screenHeight", Gdx.graphics.getHeight() + "");
        }
        // 1 0
        if (str.contains("isTouched")) {
            boolean isTouch = Gdx.input.isTouched();
            str = str.replaceAll("isTouched", (isTouch ? 1 : 0) + "");
        }

        if (str.contains("touchDeltaX")) {

            str = str.replaceAll("touchDeltaX", Gdx.input.getDeltaX() + "");
        }

        if (str.contains("touchDeltaY")) {
            str = str.replaceAll("touchDeltaY", Gdx.input.getDeltaY() + "");
        }

        if (str.contains("touchXDir")) {
            if (totalDeltaX > 0) {
                touchXDir = 1;
            } else if (totalDeltaX < 0) {
                touchXDir = -1;
            }
            str = str.replaceAll("touchXDir", touchXDir + "");
        }

        if (str.contains("touchYDir")) {
            if (totalDeltaY > 0) {
                touchXDir = 1;
            } else if (totalDeltaY < 0) {
                touchYDir = -1;
            }
            str = str.replaceAll("touchYDir", touchYDir + "");
        }


        if (str.contains("totalDeltaX")) {
            str = str.replaceAll("totalDeltaX", totalDeltaX + "");
        }

        if (str.contains("totalDeltaY")) {
            str = str.replaceAll("totalDeltaY", totalDeltaY + "");
        }

        if (str.contains("deltaTime")) {
            str = str.replaceAll("deltaTime", Gdx.graphics.getDeltaTime() + "");
        }

        if (str.contains("time")) {
            str = str.replaceAll("time", totalTime + "");
        }

        //时
        if (str.contains("hour12")) {
            int hour12 = c.get(Calendar.HOUR);
            hour12 = hour12 == 0 ? 12 : hour12;
            str = str.replaceAll("hour12", hour12 + "");
        }

        //分
        if (str.contains("minute")) {
            int minute = c.get(Calendar.MINUTE);
            str = str.replaceAll("minute", minute + "");
        }
        //秒
        if (str.contains("currentTimeMillis")) {
            str = str.replaceAll("currentTimeMillis", System.currentTimeMillis() + "");
        }
        //手指抬起
        if (!Gdx.input.isTouched()) {
            totalDeltaX = 0;
            totalDeltaY = 0;
        }

        return str;
    }

    float totalTime = 0.0f;
    float rotateX = 0;
    float rotateY = 0;
    float rotateZ = 0;

    public float getRotateX() {
        return rotateX;
    }

    public float getRotateY() {
        return rotateY;
    }

    public float getRotateZ() {
        return rotateZ;
    }

    //执行动画
    @Override
    public void draw(View view, float time) {
        if (view == null)
            return;
        totalTime += time;

        if (subObjectName != null) {

            if (node == null) {
                if (view instanceof ModelView) {
                    ModelView modelView = ((ModelView) view);
                    if (modelView.modelLoadBase == null || !modelView.modelLoadBase.isLoaded())
                        return;
                    node = modelView.modelLoadBase.getNode(subObjectName);

                }
            }

            //对子模型进行动画
            rotationNode(node);
            return;
        }

        float rotateX = 0;
        float rotateY = 0;
        float rotateZ = 0;

        if (!TextUtils.isEmpty(rotateXScript)) {
            String rotateXScriptTemp = rotateXScript;
            rotateXScriptTemp = updateParameter(rotateXScriptTemp);//更新参数
            try {
                Object ret = ScriptUtils.se().eval(rotateXScriptTemp);
                if (ret != null) {
                    if (ret instanceof Double) {
                        Double value = (Double) ret;
                        rotateX += value;
                    } else if (ret instanceof Integer) {
                        int value = (Integer) ret;
                        rotateX += value;
                    } else if (ret instanceof Float) {
                        float value = (Float) ret;
                        rotateX += value;
                    }
                }
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }


        if (!TextUtils.isEmpty(rotateYScript) ) {
            String rotateYScriptTemp=rotateYScript;
            rotateYScriptTemp=updateParameter(rotateYScriptTemp);//更新参数
            try {
                Object ret = ScriptUtils.se().eval(rotateYScriptTemp);
                if (ret != null) {
                    if (ret instanceof Double) {
                        Double value = (Double) ret;
                        rotateY += value;
                    } else if (ret instanceof Integer) {
                        int value = (Integer) ret;
                        rotateY += value;
                    } else if (ret instanceof Float) {
                        float value = (Float) ret;
                        rotateY += value;
                    }
                }
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(rotateZScript)) {
           String rotateZScriptTemp=rotateZScript;
            rotateZScriptTemp=updateParameter(rotateZScriptTemp);//更新参数
            try {
                Object ret = ScriptUtils.se().eval(rotateZScriptTemp);
                if (ret != null) {
                    if (ret instanceof Double) {
                        Double value = (Double) ret;
                        rotateZ += value;
                    } else if (ret instanceof Integer) {
                        int value = (Integer) ret;
                        rotateZ += value;
                    } else if (ret instanceof Float) {
                        float value = (Float) ret;
                        rotateZ += value;
                    }
                }
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        if (isValueAdd) {
            this.rotateX += rotateX;
            this.rotateY += rotateY;
            this.rotateZ += rotateZ;
        } else {
            this.rotateX = rotateX;
            this.rotateY = rotateY;
            this.rotateZ = rotateZ;
        }
//        view.setRotationX(rotateX);
//        view.setRotationY(rotateY);
//        view.setRotation(rotateZ);
    }

    private void rotationNode(Node node) {
        if (node == null)
            return;


        float rotateX = 0;
        float rotateY = 0;
        float rotateZ = 0;

        if (!TextUtils.isEmpty(rotateXScript)) {
            String rotateXScriptTemp=rotateXScript;
            rotateXScriptTemp=updateParameter(rotateXScriptTemp);//更新参数
            try {
                Object ret = ScriptUtils.se().eval(rotateXScriptTemp);
                if (ret != null) {
                    if (ret instanceof Double) {
                        Double value = (Double) ret;
                        rotateX += value;
                    } else if (ret instanceof Integer) {
                        int value = (Integer) ret;
                        rotateX += value;
                    } else if (ret instanceof Float) {
                        float value = (Float) ret;
                        rotateX += value;
                    }

                }
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }


        if (!TextUtils.isEmpty(rotateYScript) ) {
            String rotateYScriptTemp=rotateYScript;
            rotateYScriptTemp=updateParameter(rotateYScriptTemp);//更新参数
            try {
                Object ret = ScriptUtils.se().eval(rotateYScriptTemp);
                if (ret != null) {
                    if (ret instanceof Double) {
                        Double value = (Double) ret;
                        rotateY += value;
                    } else if (ret instanceof Integer) {
                        int value = (Integer) ret;
                        rotateY += value;
                    } else if (ret instanceof Float) {
                        float value = (Float) ret;
                        rotateY += value;
                    }

                }
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(rotateZScript) ) {
            String rotateZScriptTemp=rotateZScript;
            rotateZScriptTemp=updateParameter(rotateZScriptTemp);//更新参数
            try {
                Object ret = ScriptUtils.se().eval(rotateZScriptTemp);
                if (ret != null) {
                    if (ret instanceof Double) {
                        Double value = (Double) ret;
                        rotateZ += value;
                    } else if (ret instanceof Integer) {
                        int value = (Integer) ret;
                        rotateZ += value;
                    } else if (ret instanceof Float) {
                        float value = (Float) ret;
                        rotateZ += value;
                    }

                }
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }


        if (isSubValueAdd) {
            acceleromoterTempvecor.x += rotateX;
            acceleromoterTempvecor.y += rotateY;
            acceleromoterTempvecor.z += rotateZ;
        } else {
            acceleromoterTempvecor.x = rotateX;
            acceleromoterTempvecor.y = rotateY;
            acceleromoterTempvecor.z = rotateZ;
        }


        acceleromoterXYvecor.lerp(acceleromoterTempvecor, alpha);
        //这里直接用global了，具体请参考node源码，暂时直接用了，以后如果有冲突再处理
        node.globalTransform.rotate(Vector3.X, acceleromoterXYvecor.x);
        node.globalTransform.rotate(Vector3.Y, acceleromoterXYvecor.y);
        node.globalTransform.rotate(Vector3.Z, acceleromoterXYvecor.z);
    }


    @Override
    public void dispose() {
        super.dispose();
    }
}
