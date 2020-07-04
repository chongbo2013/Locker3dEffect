package com.locker.theme.scenes3d.newscreen.beans;

import android.text.TextUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.locker.theme.scenes3d.ScriptUtils;
import com.locker.theme.scenes3d.newscreen.actors.View;
import com.locker.theme.scenes3d.newscreen.tween.BaseParserTween;

import java.util.Calendar;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;

/**
 * <CustomAccelerometerAnimation
 * name="scene_bg_acceleromet_test"
 * aX="true"
 * aY="true"
 * startX="0"
 * endX="screenWidth"
 * startY="0"
 * endY="screenHeight"
 * xSpeex="2"
 * ySpeex="2"
 * rX="true"
 * rY="true"
 * rZ="true"
 * rStartx="0"
 * rEndx="360"
 * rStarty="0"
 * rEndy="360"
 * rXspeex="2"
 * rYspeex="2"
 * isValueAdd="true"
 * />
 * <p>
 * 自定义 重力感应控件
 * 支持 extends View 控件使用
 * Created by xff on 2017/12/23.
 */

public class CustomAccelerometerAnimation extends BaseParserTween {
    public String name;

    public boolean rX = false;
    public String rXSpeexscript;


    public String rYSpeexscript;


    public String aXSpeexscript;


    public String aYSpeexscript;


    public String rStartXscript, rEndXscript;


    public boolean rY = false;
    public String rStartYscript, rEndYscript;


    public boolean aX = false;
    public String aStartXscript, aEndXscript;


    public boolean aY = false;
    public String aStartYscript, aEndYscript;


    public boolean isValueAdd;


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
    float x = 0;
    float y = 0;

    public float getRotateX() {
        return rotateX;
    }

    public float getRotateY() {
        return rotateY;
    }

    public float getRotateZ() {
        return 0;
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return 0;
    }


    float lastAcX = 0, lastAcY = 0;

    public float getAccelerometerY() {
        float currentAcY = Gdx.input.getAccelerometerY();
        float delAcY = currentAcY - lastAcY;
        lastAcY = currentAcY;
        return delAcY;
    }

    public float getAccelerometerX() {
        float currentAcX = Gdx.input.getAccelerometerX();
        float delAcX = currentAcX - lastAcX;
        lastAcX = currentAcX;
        return delAcX;
    }

    //执行动画
    @Override
    public void draw(View view, float time) {
        if (view == null)
            return;
        totalTime += time;

        float rotateX = 0;
        float rotateY = 0;

        float x = 0;
        float y = 0;

        if (rX) {
            float rStartX = 0;
            float rEndX = 0;
            float rXSpeex = 0;


            String rXSpeexscriptTemp = rXSpeexscript;
            rXSpeexscriptTemp = updateParameter(rXSpeexscriptTemp);//更新参数
            try {
                Object ret = ScriptUtils.se().eval(rXSpeexscriptTemp);
                if (ret != null) {
                    if (ret instanceof Double) {
                        Double value = (Double) ret;
                        rXSpeex += value;
                    } else if (ret instanceof Integer) {
                        int value = (Integer) ret;
                        rXSpeex += value;
                    } else if (ret instanceof Float) {
                        float value = (Float) ret;
                        rXSpeex += value;
                    }
                }
            } catch (ScriptException e) {
                e.printStackTrace();
            }

            String rStartXscriptTemp = rStartXscript;
            rStartXscriptTemp = updateParameter(rStartXscriptTemp);//更新参数
            try {
                Object ret = ScriptUtils.se().eval(rStartXscriptTemp);
                if (ret != null) {
                    if (ret instanceof Double) {
                        Double value = (Double) ret;
                        rStartX += value;
                    } else if (ret instanceof Integer) {
                        int value = (Integer) ret;
                        rStartX += value;
                    } else if (ret instanceof Float) {
                        float value = (Float) ret;
                        rStartX += value;
                    }
                }
            } catch (ScriptException e) {
                e.printStackTrace();
            }

            String rEndXscriptTemp = rEndXscript;
            rEndXscriptTemp = updateParameter(rEndXscriptTemp);//更新参数
            try {
                Object ret = ScriptUtils.se().eval(rEndXscriptTemp);
                if (ret != null) {
                    if (ret instanceof Double) {
                        Double value = (Double) ret;
                        rEndX += value;
                    } else if (ret instanceof Integer) {
                        int value = (Integer) ret;
                        rEndX += value;
                    } else if (ret instanceof Float) {
                        float value = (Float) ret;
                        rEndX += value;
                    }
                }
            } catch (ScriptException e) {
                e.printStackTrace();
            }


            //Gdx.input.getAccelerometerX() 左右  10 到-10
            // Gdx.input.getAccelerometerY() 上下 -10  到10
            rotateX = getAccelerometerY() * rXSpeex;
            if (isValueAdd) {
                this.rotateX += rotateX;
            } else {
                this.rotateX = rotateX;
            }
//            this.rotateX=this.rotateX%360;
//            this.rotateX= MathUtils.clamp(this.rotateX,rStartX,rEndX);
        }

        if (rY) {

            float rStartY = 0;
            float rEndY = 0;
            float rYSpeex = 0;

            String rYSpeexscriptString = rYSpeexscript;
            rYSpeexscriptString = updateParameter(rYSpeexscriptString);//更新参数
            try {
                Object ret = ScriptUtils.se().eval(rYSpeexscriptString);
                if (ret != null) {
                    if (ret instanceof Double) {
                        Double value = (Double) ret;
                        rYSpeex += value;
                    } else if (ret instanceof Integer) {
                        int value = (Integer) ret;
                        rYSpeex += value;
                    } else if (ret instanceof Float) {
                        float value = (Float) ret;
                        rYSpeex += value;
                    }
                }
            } catch (ScriptException e) {
                e.printStackTrace();
            }


            String rStartYscriptTemp = rStartYscript;
            rStartYscriptTemp = updateParameter(rStartYscriptTemp);//更新参数
            try {
                Object ret = ScriptUtils.se().eval(rStartYscriptTemp);
                if (ret != null) {
                    if (ret instanceof Double) {
                        Double value = (Double) ret;
                        rStartY += value;
                    } else if (ret instanceof Integer) {
                        int value = (Integer) ret;
                        rStartY += value;
                    } else if (ret instanceof Float) {
                        float value = (Float) ret;
                        rStartY += value;
                    }
                }
            } catch (ScriptException e) {
                e.printStackTrace();
            }


                String rEndYscriptString=rEndYscript;
            rEndYscriptString=updateParameter(rEndYscriptString);//更新参数
                try {
                    Object ret = ScriptUtils.se().eval(rEndYscriptString);
                    if (ret != null) {
                        if (ret instanceof Double) {
                            Double value = (Double) ret;
                            rEndY += value;
                        } else if (ret instanceof Integer) {
                            int value = (Integer) ret;
                            rEndY += value;
                        } else if (ret instanceof Float) {
                            float value = (Float) ret;
                            rEndY += value;
                        }
                    }
                } catch (ScriptException e) {
                    e.printStackTrace();
                }

            rotateY = getAccelerometerX() * rYSpeex;


            if (isValueAdd) {
                this.rotateY += rotateY;
            } else {
                this.rotateY = rotateY;
            }
            //暂时不做限制
//            this.rotateY=this.rotateY%360;
//            this.rotateY= MathUtils.clamp(this.rotateY,rStartY,rEndY);
        }


        ///stat

        if (aX) {
            float aStartX = 0;
            float aEndX = 0;
            float aXSpeex = 0;

                String aXSpeexscriptTemp=aXSpeexscript;
               aXSpeexscriptTemp=updateParameter(aXSpeexscriptTemp);//更新参数
                try {
                    Object ret = ScriptUtils.se().eval(aXSpeexscriptTemp);
                    if (ret != null) {
                        if (ret instanceof Double) {
                            Double value = (Double) ret;
                            aXSpeex += value;
                        } else if (ret instanceof Integer) {
                            int value = (Integer) ret;
                            aXSpeex += value;
                        } else if (ret instanceof Float) {
                            float value = (Float) ret;
                            aXSpeex += value;
                        }
                    }
                } catch (ScriptException e) {
                    e.printStackTrace();
                }


                String   aStartXscriptTemp=aStartXscript;

            aStartXscriptTemp=updateParameter(aStartXscriptTemp);//更新参数
                try {
                    Object ret = ScriptUtils.se().eval(aStartXscriptTemp);
                    if (ret != null) {
                        if (ret instanceof Double) {
                            Double value = (Double) ret;
                            aStartX += value;
                        } else if (ret instanceof Integer) {
                            int value = (Integer) ret;
                            aStartX += value;
                        } else if (ret instanceof Float) {
                            float value = (Float) ret;
                            aStartX += value;
                        }
                    }
                } catch (ScriptException e) {
                    e.printStackTrace();
                }



            String aEndXscriptString=aEndXscript;
            aEndXscriptString=updateParameter(aEndXscriptString);//更新参数
                try {
                    Object ret = ScriptUtils.se().eval(aEndXscriptString);
                    if (ret != null) {
                        if (ret instanceof Double) {
                            Double value = (Double) ret;
                            aEndX += value;
                        } else if (ret instanceof Integer) {
                            int value = (Integer) ret;
                            aEndX += value;
                        } else if (ret instanceof Float) {
                            float value = (Float) ret;
                            aEndX += value;
                        }
                    }
                } catch (ScriptException e) {
                    e.printStackTrace();
                }


            x = getAccelerometerX() * aXSpeex;
            if (isValueAdd) {
                this.x += x;
            } else {
                this.x = x;
            }
//            this.x= MathUtils.clamp(this.x,aStartX,aEndX);
        }

        if (aY) {
            float aStartY = 0;
            float aEndY = 0;
            float aYSpeex = 0;

            String aYSpeexscriptTemp=  aYSpeexscript;
            aYSpeexscriptTemp= updateParameter(aYSpeexscriptTemp);//更新参数
                try {
                    Object ret = ScriptUtils.se().eval(aYSpeexscriptTemp);
                    if (ret != null) {
                        if (ret instanceof Double) {
                            Double value = (Double) ret;
                            aYSpeex += value;
                        } else if (ret instanceof Integer) {
                            int value = (Integer) ret;
                            aYSpeex += value;
                        } else if (ret instanceof Float) {
                            float value = (Float) ret;
                            aYSpeex += value;
                        }
                    }
                } catch (ScriptException e) {
                    e.printStackTrace();
                }



                String aStartYscriptTemp=aStartYscript;
            aStartYscriptTemp=updateParameter(aStartYscriptTemp);//更新参数
                try {
                    Object ret = ScriptUtils.se().eval(aStartYscriptTemp);
                    if (ret != null) {
                        if (ret instanceof Double) {
                            Double value = (Double) ret;
                            aStartY += value;
                        } else if (ret instanceof Integer) {
                            int value = (Integer) ret;
                            aStartY += value;
                        } else if (ret instanceof Float) {
                            float value = (Float) ret;
                            aStartY += value;
                        }
                    }
                } catch (ScriptException e) {
                    e.printStackTrace();
                }


            String aEndYscriptTemp=aEndYscript;
            aEndYscriptTemp=updateParameter(aEndYscriptTemp);//更新参数
                try {
                    Object ret = ScriptUtils.se().eval(aEndYscriptTemp);
                    if (ret != null) {
                        if (ret instanceof Double) {
                            Double value = (Double) ret;
                            aEndY += value;
                        } else if (ret instanceof Integer) {
                            int value = (Integer) ret;
                            aEndY += value;
                        } else if (ret instanceof Float) {
                            float value = (Float) ret;
                            aEndY += value;
                        }
                    }
                } catch (ScriptException e) {
                    e.printStackTrace();
                }

            y = getAccelerometerY() * aYSpeex;
            if (isValueAdd) {
                this.y += y;
            } else {
                this.y = y;
            }
//            this.y= MathUtils.clamp(this.y,aStartY,aEndY);
        }


//        if(rX)
//        view.setRotationX(this.rotateX);
//
//        if(rY)
//        view.setRotationY(this.rotateY);
//
//        if(aX)
//            view.setX(x);
//
//        if(aY)
//            view.setY(y);
    }

    //限制位移
    @Override
    public void checkBound(View view) {

    }

    @Override
    public void dispose() {
        super.dispose();


    }
}
