package com.locker.theme.scenes3d.newscreen;

import android.text.TextUtils;
import android.util.Xml;

import com.badlogic.gdx.graphics.Color;
import com.locker.theme.scenes3d.Scenes3DCore;
import com.locker.theme.scenes3d.ScriptUtils;
import com.locker.theme.scenes3d.newscreen.actions.AlphaUnLockAction;
import com.locker.theme.scenes3d.newscreen.actions.MoveUnLockAction;
import com.locker.theme.scenes3d.newscreen.actions.RotateUnLockAction;
import com.locker.theme.scenes3d.newscreen.actors.ElvesView;
import com.locker.theme.scenes3d.newscreen.actors.ImageView;
import com.locker.theme.scenes3d.newscreen.actors.ModelView;
import com.locker.theme.scenes3d.newscreen.actors.ParticleView;
import com.locker.theme.scenes3d.newscreen.actors.View;
import com.locker.theme.scenes3d.newscreen.actors.ViewGroup;
import com.locker.theme.scenes3d.newscreen.beans.ActorBean;
import com.locker.theme.scenes3d.newscreen.beans.CustomAccelerometerAnimation;
import com.locker.theme.scenes3d.newscreen.beans.CustomRotateAnimation;
import com.locker.theme.scenes3d.newscreen.beans.ElvesBean;
import com.locker.theme.scenes3d.newscreen.beans.ImageBean;
import com.locker.theme.scenes3d.newscreen.beans.ModelBean;
import com.locker.theme.scenes3d.newscreen.beans.ParticleBean;
import com.locker.theme.scenes3d.newscreen.beans.UnLockConfig;
import com.locker.theme.scenes3d.newscreen.clock.ClockParser;
import com.locker.theme.scenes3d.newscreen.clock.DateParam;
import com.locker.theme.scenes3d.newscreen.clock.Param;
import com.locker.theme.scenes3d.newscreen.clock.WeatherParam;
import com.locker.theme.scenes3d.newscreen.clock.WeekParam;
import com.locker.theme.scenes3d.newscreen.core.TweenAction;
import com.locker.theme.scenes3d.newscreen.event.AnimationEventbean;
import com.locker.theme.scenes3d.newscreen.event.EventBean;
import com.locker.theme.scenes3d.newscreen.event.MessageEventbean;
import com.locker.theme.scenes3d.newscreen.event.MessageInstance;
import com.locker.theme.scenes3d.newscreen.event.OpenEventbean;
import com.locker.theme.scenes3d.newscreen.event.ReceiveMessageBean;
import com.locker.theme.scenes3d.newscreen.event.SendMessageBean;
import com.locker.theme.scenes3d.newscreen.face.GifFace;
import com.locker.theme.scenes3d.newscreen.face.RedDotFace;
import com.locker.theme.scenes3d.newscreen.face.TextureFace;
import com.locker.theme.scenes3d.newscreen.face.TimeFace;
import com.locker.theme.scenes3d.newscreen.face.TimeNormalFace;
import com.locker.theme.scenes3d.newscreen.face.VideoFace;
import com.locker.theme.scenes3d.newscreen.tween.AlphaParserTween;
import com.locker.theme.scenes3d.newscreen.tween.BaseGroupParserTween;
import com.locker.theme.scenes3d.newscreen.tween.BaseParserTween;
import com.locker.theme.scenes3d.newscreen.tween.ColorParserTween;
import com.locker.theme.scenes3d.newscreen.tween.MoveXYZParserTween;
import com.locker.theme.scenes3d.newscreen.tween.ParallelParserTween;
import com.locker.theme.scenes3d.newscreen.tween.RotateXYZParserTween;
import com.locker.theme.scenes3d.newscreen.tween.ScaleXYZParserTween;
import com.locker.theme.scenes3d.newscreen.tween.SequenceParserTween;
import com.locker.theme.scenes3d.newscreen.widgets.MainCellLayout;
import com.locker.theme.scenes3d.newscreen.widgets.PswdCellLayout;
import com.locker.theme.scenes3d.newscreen.widgets.UnLockLayout;
import com.locker.theme.scenes3d.newscreen.widgets.WallpaperLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Circ;
import aurelienribon.tweenengine.equations.Cubic;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Expo;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Quart;
import aurelienribon.tweenengine.equations.Quint;
import aurelienribon.tweenengine.equations.Sine;

import static com.locker.theme.scenes3d.Scenes3DCore.scenes3DCore;
import static com.locker.theme.scenes3d.newscreen.core.AssetUtil.readBuffer;

/**
 * 解析主题
 * Created by xff on 2017/12/22.
 */

public class XmlParserUtils {

    /**
     * 从xml获取view
     * @param file_path
     * @return
     */
    public static View getDiyThemeViewFromXml(String  file_path) {
        View view = null;
        InputStream inputStream=null;
        try {

            if(scenes3DCore.iHost==null){
                inputStream=readBuffer(scenes3DCore.hostContext,file_path);
            }else{
                inputStream = new FileInputStream(file_path);
            }
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            beginDocument(parser, "DIYThemes");
            int eventType = parser.getEventType();
            String name;


            while (eventType != XmlPullParser.END_DOCUMENT) {
                name = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if(name.equals("UnLockConfig")){//解锁配置
                            UnLockConfig unLockConfig=new UnLockConfig();
                            if(view!=null){
                                view.actonBean.unLockConfig=unLockConfig;
                            }
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if(key.equals("type")) {
                                    unLockConfig.type=value;
                                }else if(key.equals("alphaActions")){
                                    AlphaUnLockAction alphaUnLockAction=new AlphaUnLockAction();
                                    String[] values=value.split("\\|");
                                    alphaUnLockAction.target=values[0];
                                    alphaUnLockAction.param1=values[1];
                                    alphaUnLockAction.param2=values[2];
                                    unLockConfig.unLockActions.add(alphaUnLockAction);
                                }else if(key.equals("moveActions")){
                                    MoveUnLockAction moveUnLockAction=new MoveUnLockAction();
                                    String[] values=value.split("\\|");
                                    moveUnLockAction.target=values[0];
                                    moveUnLockAction.param1=values[1];
                                    moveUnLockAction.param2=values[2];
                                    unLockConfig.unLockActions.add(moveUnLockAction);
                                }else if(key.equals("rotateActions")){
                                    RotateUnLockAction rotateUnLockAction=new RotateUnLockAction();
                                    String[] values=value.split("\\|");
                                    rotateUnLockAction.target=values[0];
                                    rotateUnLockAction.param1=values[1];
                                    rotateUnLockAction.param2=values[2];
                                    unLockConfig.unLockActions.add(rotateUnLockAction);
                                }else if(key.equals("fromX")){
                                    unLockConfig.fromX=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("fromY")){
                                    unLockConfig.fromY=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("toX")){
                                    unLockConfig.toX=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("toY")){
                                    unLockConfig.toY=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("speed")){
                                    unLockConfig.speed=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("threshold")){
                                    unLockConfig.threshold=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("distance")){
                                    unLockConfig.distance=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("time")){
                                    unLockConfig.time=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("fbo")){
                                    unLockConfig.fbo=value.contains("true")?true:false;
                                }else if(key.equals("fromAlpha")){
                                    unLockConfig.fromAlpha=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("toAlpha")){
                                    unLockConfig.toAlpha=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("alphaSpeex")){
                                    unLockConfig.alphaSpeex=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("interpolator")){
                                    TweenEquation tweenEquation = Linear.INOUT;
                                    switch (value) {
                                        case "linearTween":
                                            tweenEquation = Linear.INOUT;
                                            break;
                                        case "easeInQuad":
                                            tweenEquation = Quad.IN;
                                            break;
                                        case "easeOutQuad":
                                            tweenEquation = Quad.OUT;
                                            break;
                                        case "easeInOutQuad":
                                            tweenEquation = Quad.INOUT;
                                            break;
                                        case "easeInCubic":
                                            tweenEquation = Cubic.IN;
                                            break;
                                        case "easeOutCubic":
                                            tweenEquation = Cubic.OUT;
                                            break;
                                        case "easeInOutCubic":
                                            tweenEquation = Cubic.INOUT;
                                            break;
                                        case "easeInQuart":
                                            tweenEquation = Quart.IN;
                                            break;
                                        case "easeOutQuart":
                                            tweenEquation = Quart.OUT;
                                            break;
                                        case "easeInOutQuart":
                                            tweenEquation = Quart.INOUT;
                                            break;
                                        case "easeInQuint":
                                            tweenEquation = Quint.IN;
                                            break;
                                        case "easeOutQuint":
                                            tweenEquation = Quint.OUT;
                                            break;
                                        case "easeInOutQuint":
                                            tweenEquation = Quint.INOUT;
                                            break;
                                        case "easeInExpo":
                                            tweenEquation = Expo.IN;
                                            break;
                                        case "easeOutExpo":
                                            tweenEquation = Expo.OUT;
                                            break;
                                        case "easeInOutExpo":
                                            tweenEquation = Expo.INOUT;
                                            break;
                                        case "easeInCirc":
                                            tweenEquation = Circ.IN;
                                            break;
                                        case "easeOutCirc":
                                            tweenEquation = Circ.OUT;
                                            break;
                                        case "easeInOutCirc":
                                            tweenEquation = Circ.INOUT;
                                            break;
                                        case "easeInSine":
                                            tweenEquation = Sine.IN;
                                            break;
                                        case "easeOutSine":
                                            tweenEquation = Sine.OUT;
                                            break;
                                        case "easeInOutSine":
                                            tweenEquation = Sine.INOUT;
                                            break;
                                        case "easeInBack":
                                            tweenEquation = Back.IN;
                                            break;
                                        case "easeOutBack":
                                            tweenEquation = Back.OUT;
                                            break;
                                        case "easeInOutBack":
                                            tweenEquation = Back.INOUT;
                                            break;
                                        case "easeInElastic":
                                            tweenEquation = Elastic.IN;
                                            break;
                                        case "easeOutElastic":
                                            tweenEquation = Elastic.OUT;
                                            break;
                                        case "easeInOutElastic":
                                            tweenEquation = Elastic.INOUT;
                                            break;
                                        case "easeInBounce":
                                            tweenEquation = Bounce.IN;
                                            break;
                                        case "easeOutBounce":
                                            tweenEquation = Bounce.OUT;
                                            break;
                                        case "easeInOutBounce":
                                            tweenEquation = Bounce.INOUT;
                                            break;
                                    }
                                    unLockConfig.tweenEquation=tweenEquation;
                                }
                            }
                        }else if(name.equals("Config")){//解锁配置
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if(key.equals("unLockDelay")) {
                                    Scenes3DCore.unLockDelay=Long.parseLong(value);
                                }
                            }
                        }else if (name.equals("ModelView")) {
                            //解析属性，创建对象
                            view = new ModelView();

                            ModelBean bean = new ModelBean();
                            view.actonBean = bean;
                            //初始化基础属性
                            parserBaseAttributes(view, parser);
                            System.out.print("dd");
                        } else if (name.equals("ParticleView")) {
                            //解析属性，创建对象
                            view = new ParticleView();

                            ParticleBean bean = new ParticleBean();
                            view.actonBean = bean;
                            //初始化基础属性
                            parserBaseAttributes(view, parser);
                            System.out.print("dd");
                        } else if (name.equals("ImageView")) {
                            //解析属性，创建对象
                            view = new ImageView();

                            ImageBean bean = new ImageBean();
                            view.actonBean = bean;
                            //初始化基础属性
                            parserBaseAttributes(view, parser);
                            System.out.print("dd");
                        } else if (name.equals("ElvesView")) {
                            //解析属性，创建对象
                            view = new ElvesView();

                            ElvesBean bean = new ElvesBean();
                            view.actonBean = bean;
                            //初始化基础属性
                            parserBaseAttributes(view, parser);
                            System.out.print("dd");
                        } else if (name.equals("Animations")) {
                            if (view != null) {
                                view.actonBean.animations = new HashMap<>();
                            }
                        } else if (name.equals("CustomRotateAnimation")) {
                            if (view != null) {
                                CustomRotateAnimation customRotateAnimation = new CustomRotateAnimation();
                                parserCustomRotateAnimationAttributes(view.actonBean.animations, customRotateAnimation, parser);
                            }
                        }else if (name.equals("CustomAccelerometerAnimation")) {
                            if (view != null) {
                                CustomAccelerometerAnimation customRotateAnimation = new CustomAccelerometerAnimation();
                                parserCustomAccelerometerAnimationAttributes(view.actonBean.animations, customRotateAnimation, parser);
                            }
                        } else if (isAnimation(name)) {

                            if (view != null) {
                                if (isGroup(name)) {
                                    parserAnimationGroupAttributes(view, name, parser);
                                } else {
                                    parserAnimationAttributes(view, name, parser);
                                }
                            }
                        } else if (name.equals("Events")) {
                            if (view != null) {
                                view.actonBean.eventAnimation = new ArrayList<>();
                            }
                        } else if (name.equals("Continue")) {//持续性事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            //是否持续绘制
                            String continue_once = "";
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }else if(key.equals("once")){
                                    continue_once=value;
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_CONTINUE;
                            eventBean.delay=continue_delay;
                            eventBean.isOnce="true".equals(continue_once);
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            }
                        } else if (name.equals("Click")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_CLICK;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            }
                        } else if (name.equals("Lock")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_LOCK;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            }
                        } else if (name.equals("Unlock")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_UNLOCK;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            }
                        }else if (name.equals("PswdShow")) {//密码页面显示
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_PSWD_SHOW;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            }
                        } else if (name.equals("PswdHide")) {//密码页面显示
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_PSWD_HIDE;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            }
                        }else if (name.equals("NewMsg")) {//来新消息了
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_NEW_MSG;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            }
                        }else if (name.equals("NoMsg")) {//来新消息了
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_NO_MSG;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            }
                        }  else if (name.equals("Common")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_COMMON;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            }
                        } else if (name.equals("UnLockTouchDown")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_UNLOCK_TOUCH_DOWN;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            }
                        } else if (name.equals("UnLockDragEnter")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_UNLOCK_DRAG_ENTER;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            }
                        }else if (name.equals("UnLockDragExit")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_UNLOCK_DRAG_EXIT;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            }
                        }else if (name.equals("UnLockTouchUp")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_UNLOCK_TOUCH_UP;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            }
                        }else if (name.equals("TextureFace")) {//普通纹理表皮
                            TextureFace textureFace = new TextureFace();
                            ActorBean modelBean = view.actonBean;
                            if (modelBean.face == null)
                                modelBean.face = new ArrayList<>();
                            modelBean.face.add(textureFace);
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    textureFace.name = value;
                                } else if (key.equals("objectName")) {
                                    textureFace.objectName = value;
                                } else if (key.equals("resource")) {
                                    textureFace.resource = Scenes3DCore.getFileString(value);
                                }
                            }
                        }else if (name.equals("RedDotFace")) {//普通纹理表皮
                            RedDotFace textureFace = new RedDotFace();
                            ActorBean modelBean = view.actonBean;
                            if (modelBean.face == null)
                                modelBean.face = new ArrayList<>();
                            modelBean.face.add(textureFace);
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    textureFace.name = value;
                                } else if (key.equals("objectName")) {
                                    textureFace.objectName = value;
                                } else if (key.equals("resource")) {
                                    textureFace.resource = Scenes3DCore.getFileString(value);
                                }else if (key.equals("width")) {
                                    textureFace.width =ScriptUtils.getValue(value, Float.class);
                                }else if (key.equals("height")) {
                                    textureFace.height =ScriptUtils.getValue(value, Float.class);
                                }else if (key.equals("x")) {
                                    textureFace.x =ScriptUtils.getValue(value, Float.class);
                                }else if (key.equals("y")) {
                                    textureFace.y =ScriptUtils.getValue(value, Float.class);
                                }
                            }
                        } else if (name.equals("TimeFace")) {//普通纹理表皮
                            TimeFace textureFace = new TimeFace();
                            ActorBean modelBean = view.actonBean;
                            if (modelBean.face == null)
                                modelBean.face = new ArrayList<>();
                            modelBean.face.add(textureFace);
                            ClockParser clockParser = new ClockParser();
                            textureFace.drawParser = clockParser;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    textureFace.name = value;
                                } else if (key.equals("objectName")) {
                                    textureFace.objectName = value;
                                } else if (key.equals("resource")) {
                                    textureFace.resource = Scenes3DCore.getFileString(value);
                                } else if (key.equals("width")) {
                                    clockParser.width = Integer.parseInt(value);
                                } else if (key.equals("height")) {
                                    clockParser.height = Integer.parseInt(value);
                                } else if (key.equals("weather") ||key.equals("week") ||key.equals("date") ||key.equals("bg") || key.equals("time1") || key.equals("time2") || key.equals("dot") || key.equals("time3") || key.equals("time4")) {
                                    Param param = null;
                                    if(key.equals("date")){
                                        param=new DateParam();
                                    }else if(key.equals("week")){
                                        param=new WeekParam();
                                    }else if(key.equals("weather")){
                                        param=new WeatherParam();
                                    }else{
                                        param=new Param();
                                    }
                                    String[] strs = value.split(",");
                                    switch (key){
                                        case "bg":
                                            param.type = 0;
                                            break;
                                        case "time1":
                                            param.type = 1;
                                            break;
                                        case "time2":
                                            param.type = 2;
                                            break;
                                        case "dot":
                                            param.type = 3;
                                            break;
                                        case "time3":
                                            param.type = 4;
                                            break;
                                        case "time4":
                                            param.type = 5;
                                            break;
                                        case "date":
                                            param.type = 6;
                                            break;
                                        case "week":
                                            param.type = 7;
                                            break;
                                        case "weather":
                                            param.type = 8;
                                            break;
                                    }
                                    param.alias = strs[0];
                                    param.suffix = strs[1];
                                    param.x = Integer.parseInt(strs[2]);
                                    param.y = Integer.parseInt(strs[3]);
                                    param.width = Integer.parseInt(strs[4]);
                                    param.height = Integer.parseInt(strs[5]);
                                    param.isFlipV = strs[6].contains("true");
                                    param.isFlipH = strs[7].contains("true");
                                    param.resource = strs[8];
                                    clockParser.addParam(param);
                                }
                            }
                        } else if (name.equals("TimeNormalFace")) {//普通纹理表皮
                            TimeNormalFace textureFace = new TimeNormalFace();
                            ActorBean modelBean = view.actonBean;
                            if (modelBean.normalFace == null)
                                modelBean.normalFace = new ArrayList<>();
                            modelBean.normalFace.add(textureFace);
                            ClockParser clockParser = new ClockParser();
                            textureFace.drawParser = clockParser;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    textureFace.name = value;
                                } else if (key.equals("objectName")) {
                                    textureFace.objectName = value;
                                } else if (key.equals("resource")) {
                                    textureFace.resource = Scenes3DCore.getFileString(value);
                                } else if (key.equals("width")) {
                                    clockParser.width = Integer.parseInt(value);
                                } else if (key.equals("height")) {
                                    clockParser.height = Integer.parseInt(value);
                                } else if (key.equals("weather") ||key.equals("week") ||key.equals("date") ||key.equals("bg") || key.equals("time1") || key.equals("time2") || key.equals("dot") || key.equals("time3") || key.equals("time4")) {
                                    Param param = null;
                                    if(key.equals("date")){
                                        param=new DateParam();
                                    }else if(key.equals("week")){
                                        param=new WeekParam();
                                    }else if(key.equals("weather")){
                                        param=new WeatherParam();
                                    } else{
                                        param=new Param();
                                    }
                                    String[] strs = value.split(",");
                                    switch (key){
                                        case "bg":
                                            param.type = 0;
                                            break;
                                        case "time1":
                                            param.type = 1;
                                            break;
                                        case "time2":
                                            param.type = 2;
                                            break;
                                        case "dot":
                                            param.type = 3;
                                            break;
                                        case "time3":
                                            param.type = 4;
                                            break;
                                        case "time4":
                                            param.type = 5;
                                            break;
                                    }
                                    param.alias = strs[0];
                                    param.suffix = strs[1];
                                    param.x = Integer.parseInt(strs[2]);
                                    param.y = Integer.parseInt(strs[3]);
                                    param.width = Integer.parseInt(strs[4]);
                                    param.height = Integer.parseInt(strs[5]);
                                    param.isFlipV = strs[6].contains("true");
                                    param.isFlipH = strs[7].contains("true");
                                    param.resource = strs[8];
                                    clockParser.addParam(param);
                                }
                            }
                        } else if (name.equals("VideoFace")) {//视频表皮
                            if (view.actonBean instanceof ModelBean) {
                                ModelBean modelBean = (ModelBean) view.actonBean;
                                VideoFace videoFace = new VideoFace();
                                if (modelBean.videoface == null)
                                    modelBean.videoface = new ArrayList<>();
                                modelBean.videoface.add(videoFace);
                                int screenAttributeCount = parser.getAttributeCount();
                                for (int i = 0; i < screenAttributeCount; i++) {
                                    String key = parser.getAttributeName(i);
                                    String value = parser.getAttributeValue(i);
                                    if (key.equals("name")) {
                                        videoFace.name = value;
                                    } else if (key.equals("objectName")) {
                                        videoFace.objectName = value;
                                    } else if (key.equals("resource")) {
                                        videoFace.resource = Scenes3DCore.getFileString(value);
                                    } else if (key.equals("mute")) {
                                        videoFace.mute = value.equals("ture") ? true : false;
                                    } else if (key.equals("loop")) {
                                        videoFace.mute = value.equals("ture") ? true : false;
                                    }
                                }
                            }
                        } else if (name.equals("GifFace")) {//动图
                            GifFace textureFace = new GifFace();
                            ActorBean modelBean = view.actonBean;
                            if (modelBean.face == null)
                                modelBean.face = new ArrayList<>();
                            modelBean.face.add(textureFace);
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    textureFace.name = value;
                                } else if (key.equals("objectName")) {
                                    textureFace.objectName = value;
                                } else if (key.equals("resource")) {
                                    textureFace.resource = Scenes3DCore.getFileString(value);
                                } else if (key.equals("split")) {
                                    textureFace.split = value;
                                } else if (key.equals("animations")) {
                                    textureFace.animations = value;
                                }
                            }
                        }else if (name.equals("Receive")) {//动图
                            ReceiveMessageBean receiveMessageBean = new ReceiveMessageBean();
                            receiveMessageBean.view=view;
                            MessageInstance.get().putMessage(receiveMessageBean);
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    receiveMessageBean.eventId = value;
                                } else if (key.equals("dothings")) {
                                    receiveMessageBean.things = value;
                                } else if (key.equals("when")) {
                                    receiveMessageBean.whenFunction = Scenes3DCore.getFileString(value);
                                }
                            }
                        }else if (name.equals("Send")) {//动图
                            SendMessageBean sendMessageBean = new SendMessageBean();
                            sendMessageBean.view=view;
                            MessageInstance.get().putMessage(sendMessageBean);
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    sendMessageBean.eventId = value;
                                } else if (key.equals("dothings")) {
                                    sendMessageBean.things = value;
                                } else if (key.equals("when")) {
                                    sendMessageBean.whenFunction = Scenes3DCore.getFileString(value);
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
//                         if (name.equals("ModelView")) {
//                            view = null;
//                        } else if (name.equals("ParticleView")) {
//                            view = null;
//                        } else if (name.equals("ImageView")) {
//                            view = null;
//                        } else if (name.equals("ElvesView")) {
//                            view = null;
//                        }
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return view;


    }
    /**
     * 解析DIY主题
     *
     * @param file_path
     * @return
     */
    public static ArrayList<ViewGroup> getDiyThemesFromXml(String  file_path) {
        ArrayList<ViewGroup> screens = new ArrayList<>();
        InputStream inputStream=null;
        try {

            if(scenes3DCore.iHost==null){
                inputStream=readBuffer(scenes3DCore.hostContext,file_path);
            }else{
                inputStream = new FileInputStream(file_path);
            }
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(inputStream, "UTF-8");
            beginDocument(parser, "DIYThemes");
            int eventType = parser.getEventType();
            String name;

            View view = null;
            ViewGroup viewGroup = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                name = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if(name.equals("UnLockConfig")){//解锁配置
                            UnLockConfig unLockConfig=new UnLockConfig();
                            if(view!=null){
                                view.actonBean.unLockConfig=unLockConfig;
                            }else{
                                viewGroup.actonBean.unLockConfig=unLockConfig;
                            }
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if(key.equals("type")) {
                                    unLockConfig.type=value;
                                }else if(key.equals("alphaActions")){
                                    AlphaUnLockAction alphaUnLockAction=new AlphaUnLockAction();
                                    String[] values=value.split("\\|");
                                    alphaUnLockAction.target=values[0];
                                    alphaUnLockAction.param1=values[1];
                                    alphaUnLockAction.param2=values[2];
                                    unLockConfig.unLockActions.add(alphaUnLockAction);
                                }else if(key.equals("moveActions")){
                                    MoveUnLockAction moveUnLockAction=new MoveUnLockAction();
                                    String[] values=value.split("\\|");
                                    moveUnLockAction.target=values[0];
                                    moveUnLockAction.param1=values[1];
                                    moveUnLockAction.param2=values[2];
                                    unLockConfig.unLockActions.add(moveUnLockAction);
                                }else if(key.equals("rotateActions")){
                                    RotateUnLockAction rotateUnLockAction=new RotateUnLockAction();
                                    String[] values=value.split("\\|");
                                    rotateUnLockAction.target=values[0];
                                    rotateUnLockAction.param1=values[1];
                                    rotateUnLockAction.param2=values[2];
                                    unLockConfig.unLockActions.add(rotateUnLockAction);
                                }else if(key.equals("fromX")){
                                    unLockConfig.fromX=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("fromY")){
                                    unLockConfig.fromY=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("toX")){
                                    unLockConfig.toX=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("toY")){
                                    unLockConfig.toY=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("speed")){
                                    unLockConfig.speed=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("threshold")){
                                    unLockConfig.threshold=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("distance")){
                                    unLockConfig.distance=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("time")){
                                    unLockConfig.time=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("fbo")){
                                    unLockConfig.fbo=value.contains("true")?true:false;
                                }else if(key.equals("fromAlpha")){
                                    unLockConfig.fromAlpha=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("toAlpha")){
                                    unLockConfig.toAlpha=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("alphaSpeex")){
                                    unLockConfig.alphaSpeex=ScriptUtils.getValue(value,Float.class);
                                }else if(key.equals("interpolator")){
                                    TweenEquation tweenEquation = Linear.INOUT;
                                    switch (value) {
                                        case "linearTween":
                                            tweenEquation = Linear.INOUT;
                                            break;
                                        case "easeInQuad":
                                            tweenEquation = Quad.IN;
                                            break;
                                        case "easeOutQuad":
                                            tweenEquation = Quad.OUT;
                                            break;
                                        case "easeInOutQuad":
                                            tweenEquation = Quad.INOUT;
                                            break;
                                        case "easeInCubic":
                                            tweenEquation = Cubic.IN;
                                            break;
                                        case "easeOutCubic":
                                            tweenEquation = Cubic.OUT;
                                            break;
                                        case "easeInOutCubic":
                                            tweenEquation = Cubic.INOUT;
                                            break;
                                        case "easeInQuart":
                                            tweenEquation = Quart.IN;
                                            break;
                                        case "easeOutQuart":
                                            tweenEquation = Quart.OUT;
                                            break;
                                        case "easeInOutQuart":
                                            tweenEquation = Quart.INOUT;
                                            break;
                                        case "easeInQuint":
                                            tweenEquation = Quint.IN;
                                            break;
                                        case "easeOutQuint":
                                            tweenEquation = Quint.OUT;
                                            break;
                                        case "easeInOutQuint":
                                            tweenEquation = Quint.INOUT;
                                            break;
                                        case "easeInExpo":
                                            tweenEquation = Expo.IN;
                                            break;
                                        case "easeOutExpo":
                                            tweenEquation = Expo.OUT;
                                            break;
                                        case "easeInOutExpo":
                                            tweenEquation = Expo.INOUT;
                                            break;
                                        case "easeInCirc":
                                            tweenEquation = Circ.IN;
                                            break;
                                        case "easeOutCirc":
                                            tweenEquation = Circ.OUT;
                                            break;
                                        case "easeInOutCirc":
                                            tweenEquation = Circ.INOUT;
                                            break;
                                        case "easeInSine":
                                            tweenEquation = Sine.IN;
                                            break;
                                        case "easeOutSine":
                                            tweenEquation = Sine.OUT;
                                            break;
                                        case "easeInOutSine":
                                            tweenEquation = Sine.INOUT;
                                            break;
                                        case "easeInBack":
                                            tweenEquation = Back.IN;
                                            break;
                                        case "easeOutBack":
                                            tweenEquation = Back.OUT;
                                            break;
                                        case "easeInOutBack":
                                            tweenEquation = Back.INOUT;
                                            break;
                                        case "easeInElastic":
                                            tweenEquation = Elastic.IN;
                                            break;
                                        case "easeOutElastic":
                                            tweenEquation = Elastic.OUT;
                                            break;
                                        case "easeInOutElastic":
                                            tweenEquation = Elastic.INOUT;
                                            break;
                                        case "easeInBounce":
                                            tweenEquation = Bounce.IN;
                                            break;
                                        case "easeOutBounce":
                                            tweenEquation = Bounce.OUT;
                                            break;
                                        case "easeInOutBounce":
                                            tweenEquation = Bounce.INOUT;
                                            break;
                                    }
                                    unLockConfig.tweenEquation=tweenEquation;
                                }
                            }
                        }else if(name.equals("Config")){//解锁配置
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if(key.equals("unLockDelay")) {
                                   Scenes3DCore.unLockDelay=Long.parseLong(value);
                                }
                            }
                        }else if (name.equals("Screen")) {
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if(key.equals("name")) {
                                    if (value.equals("wallpaper")) {
                                        viewGroup = new WallpaperLayout();
                                    } else if (value.equals("pswd")) {
                                        viewGroup = new PswdCellLayout();
                                    } else if (value.equals("main")) {
                                        viewGroup = new MainCellLayout();
                                    } else if (value.equals("unlock")) {
                                        viewGroup = new UnLockLayout();
                                    }
                                    break;
                                }
                            }

                            viewGroup.actonBean = new ActorBean();
                            //解析容器
                            parserViewGroupBaseAttributes(viewGroup, parser);
                            screens.add(viewGroup);
                        } else if (name.equals("ModelView")) {
                            //解析属性，创建对象
                            view = new ModelView();
                            viewGroup.addActor(view);
                            ModelBean bean = new ModelBean();
                            view.actonBean = bean;
                            //初始化基础属性
                            parserBaseAttributes(view, parser);
                            System.out.print("dd");
                        } else if (name.equals("ParticleView")) {
                            //解析属性，创建对象
                            view = new ParticleView();
                            viewGroup.addActor(view);
                            ParticleBean bean = new ParticleBean();
                            view.actonBean = bean;
                            //初始化基础属性
                            parserBaseAttributes(view, parser);
                            System.out.print("dd");
                        } else if (name.equals("ImageView")) {
                            //解析属性，创建对象
                            view = new ImageView();
                            viewGroup.addActor(view);
                            ImageBean bean = new ImageBean();
                            view.actonBean = bean;
                            //初始化基础属性
                            parserBaseAttributes(view, parser);
                            System.out.print("dd");
                        } else if (name.equals("ElvesView")) {
                            //解析属性，创建对象
                            view = new ElvesView();
                            viewGroup.addActor(view);
                            ElvesBean bean = new ElvesBean();
                            view.actonBean = bean;
                            //初始化基础属性
                            parserBaseAttributes(view, parser);
                            System.out.print("dd");
                        } else if (name.equals("Animations")) {
                            if (view != null) {
                                view.actonBean.animations = new HashMap<>();
                            } else {
                                viewGroup.actonBean.animations = new HashMap<>();
                            }
                        } else if (name.equals("CustomRotateAnimation")) {
                            if (view != null) {
                                CustomRotateAnimation customRotateAnimation = new CustomRotateAnimation();
                                parserCustomRotateAnimationAttributes(view.actonBean.animations, customRotateAnimation, parser);
                            } else {
                                CustomRotateAnimation customRotateAnimation = new CustomRotateAnimation();
                                parserCustomRotateAnimationAttributes(viewGroup.actonBean.animations, customRotateAnimation, parser);
                            }
                        }else if (name.equals("CustomAccelerometerAnimation")) {
                            if (view != null) {
                                CustomAccelerometerAnimation customRotateAnimation = new CustomAccelerometerAnimation();
                                parserCustomAccelerometerAnimationAttributes(view.actonBean.animations, customRotateAnimation, parser);
                            } else {
                                CustomAccelerometerAnimation customRotateAnimation = new CustomAccelerometerAnimation();
                                parserCustomAccelerometerAnimationAttributes(viewGroup.actonBean.animations, customRotateAnimation, parser);
                            }
                        } else if (isAnimation(name)) {

                            if (view != null) {
                                if (isGroup(name)) {
                                    parserAnimationGroupAttributes(view, name, parser);
                                } else {
                                    parserAnimationAttributes(view, name, parser);
                                }
                            } else {
                                if (isGroup(name)) {
                                    parserAnimationGroupAttributes(viewGroup, name, parser);
                                } else {
                                    parserAnimationAttributes(viewGroup, name, parser);
                                }

                            }
                        } else if (name.equals("Events")) {
                            if (view != null) {
                                view.actonBean.eventAnimation = new ArrayList<>();
                            } else {
                                viewGroup.actonBean.eventAnimation = new ArrayList<>();
                            }
                        } else if (name.equals("Continue")) {//持续性事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            //是否持续绘制
                            String continue_once = "";
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }else if(key.equals("once")){
                                    continue_once=value;
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        } else {
                                            eventBean.trigger = viewGroup.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_CONTINUE;
                            eventBean.delay=continue_delay;
                            eventBean.isOnce="true".equals(continue_once);
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            } else {
                                viewGroup.actonBean.eventAnimation.add(eventBean);
                            }
                        } else if (name.equals("Click")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        } else {
                                            eventBean.trigger = viewGroup.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_CLICK;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            } else {
                                viewGroup.actonBean.eventAnimation.add(eventBean);
                            }
                        } else if (name.equals("Lock")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        } else {
                                            eventBean.trigger = viewGroup.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_LOCK;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            } else {
                                viewGroup.actonBean.eventAnimation.add(eventBean);
                            }
                        } else if (name.equals("Unlock")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        } else {
                                            eventBean.trigger = viewGroup.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_UNLOCK;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            } else {
                                viewGroup.actonBean.eventAnimation.add(eventBean);
                            }
                        }else if (name.equals("PswdShow")) {//密码页面显示
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        } else {
                                            eventBean.trigger = viewGroup.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_PSWD_SHOW;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            } else {
                                viewGroup.actonBean.eventAnimation.add(eventBean);
                            }
                        } else if (name.equals("PswdHide")) {//密码页面显示
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        } else {
                                            eventBean.trigger = viewGroup.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_PSWD_HIDE;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            } else {
                                viewGroup.actonBean.eventAnimation.add(eventBean);
                            }
                        }else if (name.equals("NewMsg")) {//来新消息了
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        } else {
                                            eventBean.trigger = viewGroup.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_NEW_MSG;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            } else {
                                viewGroup.actonBean.eventAnimation.add(eventBean);
                            }
                        }else if (name.equals("NoMsg")) {//来新消息了
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        } else {
                                            eventBean.trigger = viewGroup.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_NO_MSG;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            } else {
                                viewGroup.actonBean.eventAnimation.add(eventBean);
                            }
                        }  else if (name.equals("Common")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        } else {
                                            eventBean.trigger = viewGroup.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_COMMON;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            } else {
                                viewGroup.actonBean.eventAnimation.add(eventBean);
                            }
                        } else if (name.equals("UnLockTouchDown")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        } else {
                                            eventBean.trigger = viewGroup.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_UNLOCK_TOUCH_DOWN;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            } else {
                                viewGroup.actonBean.eventAnimation.add(eventBean);
                            }
                        } else if (name.equals("UnLockDragEnter")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        } else {
                                            eventBean.trigger = viewGroup.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_UNLOCK_DRAG_ENTER;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            } else {
                                viewGroup.actonBean.eventAnimation.add(eventBean);
                            }
                        }else if (name.equals("UnLockDragExit")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        } else {
                                            eventBean.trigger = viewGroup.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_UNLOCK_DRAG_EXIT;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            } else {
                                viewGroup.actonBean.eventAnimation.add(eventBean);
                            }
                        }else if (name.equals("UnLockTouchUp")) {//点击事件
                            String continue_name = "";
                            String continue_type = "";
                            String continue_item = "";
                            String continue_extra = "";
                            float  continue_delay = -1f;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    continue_name = value;
                                } else if (key.equals("type")) {
                                    continue_type = value;
                                } else if (key.equals("extra")) {
                                    continue_extra = value;
                                } else if (key.equals("item")) {
                                    continue_item = value;
                                }else if(key.equals("delay")){
                                    continue_delay=Float.parseFloat(value);
                                }
                            }
                            EventBean eventBean = null;
                            if (!TextUtils.isEmpty(continue_type)) {
                                switch (continue_type) {
                                    case "Animation"://类型为动画
                                        eventBean = new AnimationEventbean();
                                        if (view != null) {
                                            eventBean.trigger = view.actonBean.animations.get(continue_item);
                                        } else {
                                            eventBean.trigger = viewGroup.actonBean.animations.get(continue_item);
                                        }
                                        ((AnimationEventbean)eventBean).extra=continue_extra;
                                        break;
                                    case "Message"://类型为消息
                                        eventBean=new MessageEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                    case "Open"://类型为消息
                                        eventBean=new OpenEventbean();
                                        eventBean.trigger =continue_item;
                                        break;
                                }

                            }
                            eventBean.name = continue_name;
                            eventBean.type = EventBean.EVENT_UNLOCK_TOUCH_UP;
                            eventBean.delay=continue_delay;
                            if (view != null) {
                                view.actonBean.eventAnimation.add(eventBean);
                            } else {
                                viewGroup.actonBean.eventAnimation.add(eventBean);
                            }
                        }else if (name.equals("TextureFace")) {//普通纹理表皮
                            TextureFace textureFace = new TextureFace();
                            ActorBean modelBean = view.actonBean;
                            if (modelBean.face == null)
                                modelBean.face = new ArrayList<>();
                            modelBean.face.add(textureFace);
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    textureFace.name = value;
                                } else if (key.equals("objectName")) {
                                    textureFace.objectName = value;
                                } else if (key.equals("resource")) {
                                    textureFace.resource = Scenes3DCore.getFileString(value);
                                }
                            }
                        }else if (name.equals("RedDotFace")) {//普通纹理表皮
                            RedDotFace textureFace = new RedDotFace();
                            ActorBean modelBean = view.actonBean;
                            if (modelBean.face == null)
                                modelBean.face = new ArrayList<>();
                            modelBean.face.add(textureFace);
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    textureFace.name = value;
                                } else if (key.equals("objectName")) {
                                    textureFace.objectName = value;
                                } else if (key.equals("resource")) {
                                    textureFace.resource = Scenes3DCore.getFileString(value);
                                }else if (key.equals("width")) {
                                    textureFace.width =ScriptUtils.getValue(value, Float.class);
                                }else if (key.equals("height")) {
                                    textureFace.height =ScriptUtils.getValue(value, Float.class);
                                }else if (key.equals("x")) {
                                    textureFace.x =ScriptUtils.getValue(value, Float.class);
                                }else if (key.equals("y")) {
                                    textureFace.y =ScriptUtils.getValue(value, Float.class);
                                }
                            }
                        } else if (name.equals("TimeFace")) {//普通纹理表皮
                            TimeFace textureFace = new TimeFace();
                            ActorBean modelBean = view.actonBean;
                            if (modelBean.face == null)
                                modelBean.face = new ArrayList<>();
                            modelBean.face.add(textureFace);
                            ClockParser clockParser = new ClockParser();
                            textureFace.drawParser = clockParser;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    textureFace.name = value;
                                } else if (key.equals("objectName")) {
                                    textureFace.objectName = value;
                                } else if (key.equals("resource")) {
                                    textureFace.resource = Scenes3DCore.getFileString(value);
                                } else if (key.equals("width")) {
                                    clockParser.width = Integer.parseInt(value);
                                } else if (key.equals("height")) {
                                    clockParser.height = Integer.parseInt(value);
                                } else if (key.equals("weather") ||key.equals("week") ||key.equals("date") ||key.equals("bg") || key.equals("time1") || key.equals("time2") || key.equals("dot") || key.equals("time3") || key.equals("time4")) {
                                    Param param = null;
                                    if(key.equals("date")){
                                        param=new DateParam();
                                    }else if(key.equals("week")){
                                        param=new WeekParam();
                                    }else if(key.equals("weather")){
                                        param=new WeatherParam();
                                    }else{
                                        param=new Param();
                                    }
                                    String[] strs = value.split(",");
                                    switch (key){
                                        case "bg":
                                            param.type = 0;
                                            break;
                                        case "time1":
                                            param.type = 1;
                                            break;
                                        case "time2":
                                            param.type = 2;
                                            break;
                                        case "dot":
                                            param.type = 3;
                                            break;
                                        case "time3":
                                            param.type = 4;
                                            break;
                                        case "time4":
                                            param.type = 5;
                                            break;
                                        case "date":
                                            param.type = 6;
                                            break;
                                        case "week":
                                            param.type = 7;
                                            break;
                                        case "weather":
                                            param.type = 8;
                                            break;
                                    }
                                    param.alias = strs[0];
                                    param.suffix = strs[1];
                                    param.x = Integer.parseInt(strs[2]);
                                    param.y = Integer.parseInt(strs[3]);
                                    param.width = Integer.parseInt(strs[4]);
                                    param.height = Integer.parseInt(strs[5]);
                                    param.isFlipV = strs[6].contains("true");
                                    param.isFlipH = strs[7].contains("true");
                                    param.resource = strs[8];
                                    clockParser.addParam(param);
                                }
                            }
                        } else if (name.equals("TimeNormalFace")) {//普通纹理表皮
                            TimeNormalFace textureFace = new TimeNormalFace();
                            ActorBean modelBean = view.actonBean;
                            if (modelBean.normalFace == null)
                                modelBean.normalFace = new ArrayList<>();
                            modelBean.normalFace.add(textureFace);
                            ClockParser clockParser = new ClockParser();
                            textureFace.drawParser = clockParser;
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    textureFace.name = value;
                                } else if (key.equals("objectName")) {
                                    textureFace.objectName = value;
                                } else if (key.equals("resource")) {
                                    textureFace.resource = Scenes3DCore.getFileString(value);
                                } else if (key.equals("width")) {
                                    clockParser.width = Integer.parseInt(value);
                                } else if (key.equals("height")) {
                                    clockParser.height = Integer.parseInt(value);
                                } else if (key.equals("weather") ||key.equals("week") ||key.equals("date") ||key.equals("bg") || key.equals("time1") || key.equals("time2") || key.equals("dot") || key.equals("time3") || key.equals("time4")) {
                                    Param param = null;
                                    if(key.equals("date")){
                                        param=new DateParam();
                                    }else if(key.equals("week")){
                                        param=new WeekParam();
                                    }else if(key.equals("weather")){
                                        param=new WeatherParam();
                                    } else{
                                        param=new Param();
                                    }
                                    String[] strs = value.split(",");
                                    switch (key){
                                        case "bg":
                                            param.type = 0;
                                            break;
                                        case "time1":
                                            param.type = 1;
                                            break;
                                        case "time2":
                                            param.type = 2;
                                            break;
                                        case "dot":
                                            param.type = 3;
                                            break;
                                        case "time3":
                                            param.type = 4;
                                            break;
                                        case "time4":
                                            param.type = 5;
                                            break;
                                    }
                                    param.alias = strs[0];
                                    param.suffix = strs[1];
                                    param.x = Integer.parseInt(strs[2]);
                                    param.y = Integer.parseInt(strs[3]);
                                    param.width = Integer.parseInt(strs[4]);
                                    param.height = Integer.parseInt(strs[5]);
                                    param.isFlipV = strs[6].contains("true");
                                    param.isFlipH = strs[7].contains("true");
                                    param.resource = strs[8];
                                    clockParser.addParam(param);
                                }
                            }
                        } else if (name.equals("VideoFace")) {//视频表皮
                            if (view.actonBean instanceof ModelBean) {
                                ModelBean modelBean = (ModelBean) view.actonBean;
                                VideoFace videoFace = new VideoFace();
                                if (modelBean.videoface == null)
                                    modelBean.videoface = new ArrayList<>();
                                modelBean.videoface.add(videoFace);
                                int screenAttributeCount = parser.getAttributeCount();
                                for (int i = 0; i < screenAttributeCount; i++) {
                                    String key = parser.getAttributeName(i);
                                    String value = parser.getAttributeValue(i);
                                    if (key.equals("name")) {
                                        videoFace.name = value;
                                    } else if (key.equals("objectName")) {
                                        videoFace.objectName = value;
                                    } else if (key.equals("resource")) {
                                        videoFace.resource = Scenes3DCore.getFileString(value);
                                    } else if (key.equals("mute")) {
                                        videoFace.mute = value.equals("ture") ? true : false;
                                    } else if (key.equals("loop")) {
                                        videoFace.mute = value.equals("ture") ? true : false;
                                    }
                                }
                            }
                        } else if (name.equals("GifFace")) {//动图
                            GifFace textureFace = new GifFace();
                            ActorBean modelBean = view.actonBean;
                            if (modelBean.face == null)
                                modelBean.face = new ArrayList<>();
                            modelBean.face.add(textureFace);
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    textureFace.name = value;
                                } else if (key.equals("objectName")) {
                                    textureFace.objectName = value;
                                } else if (key.equals("resource")) {
                                    textureFace.resource = Scenes3DCore.getFileString(value);
                                } else if (key.equals("split")) {
                                    textureFace.split = value;
                                } else if (key.equals("animations")) {
                                    textureFace.animations = value;
                                }
                            }
                        }else if (name.equals("Receive")) {//动图
                            ReceiveMessageBean receiveMessageBean = new ReceiveMessageBean();
                            receiveMessageBean.view=view;
                            MessageInstance.get().putMessage(receiveMessageBean);
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    receiveMessageBean.eventId = value;
                                } else if (key.equals("dothings")) {
                                    receiveMessageBean.things = value;
                                } else if (key.equals("when")) {
                                    receiveMessageBean.whenFunction = Scenes3DCore.getFileString(value);
                                }
                            }
                        }else if (name.equals("Send")) {//动图
                            SendMessageBean sendMessageBean = new SendMessageBean();
                            sendMessageBean.view=view;
                            MessageInstance.get().putMessage(sendMessageBean);
                            int screenAttributeCount = parser.getAttributeCount();
                            for (int i = 0; i < screenAttributeCount; i++) {
                                String key = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (key.equals("name")) {
                                    sendMessageBean.eventId = value;
                                } else if (key.equals("dothings")) {
                                    sendMessageBean.things = value;
                                } else if (key.equals("when")) {
                                    sendMessageBean.whenFunction = Scenes3DCore.getFileString(value);
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("wallpaper")) {
                            viewGroup = null;
                        } else if (name.equals("pswd")) {
                            viewGroup = null;
                        } else if (name.equals("main")) {
                            viewGroup = null;
                        } else if (name.equals("unlock")) {
                            viewGroup = null;
                        } else if (name.equals("ModelView")) {
                            view = null;
                        } else if (name.equals("ParticleView")) {
                            view = null;
                        } else if (name.equals("ImageView")) {
                            view = null;
                        } else if (name.equals("ElvesView")) {
                            view = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return screens;


    }

    private static void parserAnimationAttributes(View view, String name, XmlPullParser parser) {
        BaseParserTween baseTween = null;
        String animationName = "";
        String x = "", y = "", z = "";
        String rotateX = "", rotateY = "", rotateZ = "";
        String scaleX = "", scaleY = "", scaleZ = "";
        String[] tint = null;
        // repeatYoyo
        String reverse = "";
        String reverseValue = "";
        TweenEquation tweenEquation = Linear.INOUT;
        float duration = 0.0f;
        String opacity = "";
        String fromTo = "to";
        float delay = 0.0f;
        int screenAttributeCount = parser.getAttributeCount();
        if (screenAttributeCount > 0) {
            for (int i = 0; i < screenAttributeCount; i++) {
                String key = parser.getAttributeName(i);
                String value = parser.getAttributeValue(i);
                if (key.equals("name")) {
                    animationName = value;
                } else if (key.equals("x")) {
                    x = value;
                } else if (key.equals("y")) {
                    y = value;
                } else if (key.equals("z")) {
                    z = value;
                } else if (key.equals("rotateX")) {
                    rotateX = value;
                } else if (key.equals("rotateY")) {
                    rotateY = value;
                } else if (key.equals("rotateZ")) {
                    rotateZ = value;
                } else if (key.equals("scaleX")) {
                    scaleX = value;
                } else if (key.equals("scaleY")) {
                    scaleY = value;
                } else if (key.equals("scaleZ")) {
                    scaleZ = value;
                } else if (key.equals("opacity")) {
                    opacity = value;
                } else if (key.equals("fromTo")) {
                    fromTo = value;
                } else if (key.equals("delay")) {
                    delay = Float.parseFloat(value);
                } else if (key.equals("tint")) {
                    tint = value.split(",");
                } else if (key.equals("interpolator")) {
                    switch (value) {
                        case "linearTween":
                            tweenEquation = Linear.INOUT;
                            break;
                        case "easeInQuad":
                            tweenEquation = Quad.IN;
                            break;
                        case "easeOutQuad":
                            tweenEquation = Quad.OUT;
                            break;
                        case "easeInOutQuad":
                            tweenEquation = Quad.INOUT;
                            break;
                        case "easeInCubic":
                            tweenEquation = Cubic.IN;
                            break;
                        case "easeOutCubic":
                            tweenEquation = Cubic.OUT;
                            break;
                        case "easeInOutCubic":
                            tweenEquation = Cubic.INOUT;
                            break;
                        case "easeInQuart":
                            tweenEquation = Quart.IN;
                            break;
                        case "easeOutQuart":
                            tweenEquation = Quart.OUT;
                            break;
                        case "easeInOutQuart":
                            tweenEquation = Quart.INOUT;
                            break;
                        case "easeInQuint":
                            tweenEquation = Quint.IN;
                            break;
                        case "easeOutQuint":
                            tweenEquation = Quint.OUT;
                            break;
                        case "easeInOutQuint":
                            tweenEquation = Quint.INOUT;
                            break;
                        case "easeInExpo":
                            tweenEquation = Expo.IN;
                            break;
                        case "easeOutExpo":
                            tweenEquation = Expo.OUT;
                            break;
                        case "easeInOutExpo":
                            tweenEquation = Expo.INOUT;
                            break;
                        case "easeInCirc":
                            tweenEquation = Circ.IN;
                            break;
                        case "easeOutCirc":
                            tweenEquation = Circ.OUT;
                            break;
                        case "easeInOutCirc":
                            tweenEquation = Circ.INOUT;
                            break;
                        case "easeInSine":
                            tweenEquation = Sine.IN;
                            break;
                        case "easeOutSine":
                            tweenEquation = Sine.OUT;
                            break;
                        case "easeInOutSine":
                            tweenEquation = Sine.INOUT;
                            break;
                        case "easeInBack":
                            tweenEquation = Back.IN;
                            break;
                        case "easeOutBack":
                            tweenEquation = Back.OUT;
                            break;
                        case "easeInOutBack":
                            tweenEquation = Back.INOUT;
                            break;
                        case "easeInElastic":
                            tweenEquation = Elastic.IN;
                            break;
                        case "easeOutElastic":
                            tweenEquation = Elastic.OUT;
                            break;
                        case "easeInOutElastic":
                            tweenEquation = Elastic.INOUT;
                            break;
                        case "easeInBounce":
                            tweenEquation = Bounce.IN;
                            break;
                        case "easeOutBounce":
                            tweenEquation = Bounce.OUT;
                            break;
                        case "easeInOutBounce":
                            tweenEquation = Bounce.INOUT;
                            break;
                    }
                } else if (key.equals("repeat") || key.equals("repeatYoyo")) {
                    reverse = key;
                    reverseValue = value;
                } else if (key.equals("duration")) {
                    duration = Float.parseFloat(value);
                }

            }
        }
        switch (name) {
            case "AlphaAction":
                baseTween = new AlphaParserTween();
                if (fromTo.equals("to"))
                    baseTween.to(view, TweenAction.OPACITY, duration).target(opacity).delay(delay).ease(tweenEquation);
                else
                    baseTween.from(view, TweenAction.OPACITY, duration).target(opacity).delay(delay).ease(tweenEquation);
                break;
            case "ScaleXYZAction":
                baseTween = new ScaleXYZParserTween();
                if (fromTo.equals("to"))
                    baseTween.to(view, TweenAction.SCALE_XYZ, duration).target(scaleX, scaleY, scaleZ).delay(delay).ease(tweenEquation);
                else
                    baseTween.from(view, TweenAction.SCALE_XYZ, duration).target(scaleX, scaleY, scaleZ).delay(delay).ease(tweenEquation);
                break;
            case "RotateXYZAction":
                baseTween = new RotateXYZParserTween();
                if (fromTo.equals("to"))
                    baseTween.to(view, TweenAction.ROTATION_XYZ, duration).target(rotateX, rotateY, rotateZ).delay(delay).ease(tweenEquation);
                else
                    baseTween.from(view, TweenAction.ROTATION_XYZ, duration).target(rotateX, rotateY, rotateZ).delay(delay).ease(tweenEquation);
                break;
            case "MoveXYZAction":
                baseTween = new MoveXYZParserTween();
                if (fromTo.equals("to"))
                    baseTween.to(view, TweenAction.POS_XYZ, duration).target(x, y, z).delay(delay).ease(tweenEquation);
                else
                    baseTween.from(view, TweenAction.POS_XYZ, duration).target(x, y, z).delay(delay).ease(tweenEquation);
                break;
            case "ColorAction":
                baseTween = new ColorParserTween();
                if (fromTo.equals("to"))
                    baseTween.to(view, TweenAction.TINT, duration).target(tint[0], tint[1], tint[2]).delay(delay).ease(tweenEquation);
                else
                    baseTween.from(view, TweenAction.TINT, duration).target(tint[0], tint[1], tint[2]).delay(delay).ease(tweenEquation);
                break;
        }

        baseTween.fromTo = fromTo;
        baseTween.reverse = reverse;

        if (reverse.equals("repeat")) {
            String[] repeatValue = reverseValue.split(",");
            baseTween.repeat(Integer.parseInt(repeatValue[0]), Float.parseFloat(repeatValue[1]));
        } else if (reverse.equals("repeatYoyo")) {
            String[] repeatValue = reverseValue.split(",");
            baseTween.repeatYoyo(Integer.parseInt(repeatValue[0]), Float.parseFloat(repeatValue[1]));
        }

        view.actonBean.animations.put(animationName, baseTween);
    }

    private static void parserAnimationAttributes(ViewGroup view, String name, XmlPullParser parser) {
        BaseParserTween baseTween = null;
        String animationName = "";
        String x = "", y = "", z = "";
        String rotateX = "", rotateY = "", rotateZ = "";
        String scaleX = "", scaleY = "", scaleZ = "";
        String[] tint = null;
        // repeatYoyo
        String reverse = "";
        String reverseValue = "";
        TweenEquation tweenEquation = Linear.INOUT;
        float duration = 0.0f;
        String opacity = "";
        String fromTo = "to";
        float delay = 0.0f;
        int screenAttributeCount = parser.getAttributeCount();
        if (screenAttributeCount > 0) {
            for (int i = 0; i < screenAttributeCount; i++) {
                String key = parser.getAttributeName(i);
                String value = parser.getAttributeValue(i);
                if (key.equals("name")) {
                    animationName = value;
                } else if (key.equals("x")) {
                    x = value;
                } else if (key.equals("y")) {
                    y = value;
                } else if (key.equals("z")) {
                    z = value;
                } else if (key.equals("rotateX")) {
                    rotateX = value;
                } else if (key.equals("rotateY")) {
                    rotateY = value;
                } else if (key.equals("rotateZ")) {
                    rotateZ = value;
                } else if (key.equals("scaleX")) {
                    scaleX = value;
                } else if (key.equals("scaleY")) {
                    scaleY = value;
                } else if (key.equals("scaleZ")) {
                    scaleZ = value;
                } else if (key.equals("opacity")) {
                    opacity = value;
                } else if (key.equals("fromTo")) {
                    fromTo = value;
                } else if (key.equals("delay")) {
                    delay = Float.parseFloat(value);
                } else if (key.equals("tint")) {
                    tint = value.split(",");
                } else if (key.equals("interpolator")) {
                    switch (value) {
                        case "linearTween":
                            tweenEquation = Linear.INOUT;
                            break;
                        case "easeInQuad":
                            tweenEquation = Quad.IN;
                            break;
                        case "easeOutQuad":
                            tweenEquation = Quad.OUT;
                            break;
                        case "easeInOutQuad":
                            tweenEquation = Quad.INOUT;
                            break;
                        case "easeInCubic":
                            tweenEquation = Cubic.IN;
                            break;
                        case "easeOutCubic":
                            tweenEquation = Cubic.OUT;
                            break;
                        case "easeInOutCubic":
                            tweenEquation = Cubic.INOUT;
                            break;
                        case "easeInQuart":
                            tweenEquation = Quart.IN;
                            break;
                        case "easeOutQuart":
                            tweenEquation = Quart.OUT;
                            break;
                        case "easeInOutQuart":
                            tweenEquation = Quart.INOUT;
                            break;
                        case "easeInQuint":
                            tweenEquation = Quint.IN;
                            break;
                        case "easeOutQuint":
                            tweenEquation = Quint.OUT;
                            break;
                        case "easeInOutQuint":
                            tweenEquation = Quint.INOUT;
                            break;
                        case "easeInExpo":
                            tweenEquation = Expo.IN;
                            break;
                        case "easeOutExpo":
                            tweenEquation = Expo.OUT;
                            break;
                        case "easeInOutExpo":
                            tweenEquation = Expo.INOUT;
                            break;
                        case "easeInCirc":
                            tweenEquation = Circ.IN;
                            break;
                        case "easeOutCirc":
                            tweenEquation = Circ.OUT;
                            break;
                        case "easeInOutCirc":
                            tweenEquation = Circ.INOUT;
                            break;
                        case "easeInSine":
                            tweenEquation = Sine.IN;
                            break;
                        case "easeOutSine":
                            tweenEquation = Sine.OUT;
                            break;
                        case "easeInOutSine":
                            tweenEquation = Sine.INOUT;
                            break;
                        case "easeInBack":
                            tweenEquation = Back.IN;
                            break;
                        case "easeOutBack":
                            tweenEquation = Back.OUT;
                            break;
                        case "easeInOutBack":
                            tweenEquation = Back.INOUT;
                            break;
                        case "easeInElastic":
                            tweenEquation = Elastic.IN;
                            break;
                        case "easeOutElastic":
                            tweenEquation = Elastic.OUT;
                            break;
                        case "easeInOutElastic":
                            tweenEquation = Elastic.INOUT;
                            break;
                        case "easeInBounce":
                            tweenEquation = Bounce.IN;
                            break;
                        case "easeOutBounce":
                            tweenEquation = Bounce.OUT;
                            break;
                        case "easeInOutBounce":
                            tweenEquation = Bounce.INOUT;
                            break;
                    }
                } else if (key.equals("repeat") || key.equals("repeatYoyo")) {
                    reverse = key;
                    reverseValue = value;
                } else if (key.equals("duration")) {
                    duration = Float.parseFloat(value);
                }

            }
        }
        switch (name) {
            case "AlphaAction":
                baseTween = new AlphaParserTween();
                if (fromTo.equals("to"))
                    baseTween.to(view, TweenAction.OPACITY, duration).target(opacity).delay(delay).ease(tweenEquation);
                else
                    baseTween.from(view, TweenAction.OPACITY, duration).target(opacity).delay(delay).ease(tweenEquation);
                break;
            case "ScaleXYZAction":
                baseTween = new ScaleXYZParserTween();
                if (fromTo.equals("to"))
                    baseTween.to(view, TweenAction.SCALE_XYZ, duration).target(scaleX, scaleY, scaleZ).delay(delay).ease(tweenEquation);
                else
                    baseTween.from(view, TweenAction.SCALE_XYZ, duration).target(scaleX, scaleY, scaleZ).delay(delay).ease(tweenEquation);
                break;
            case "RotateXYZAction":
                baseTween = new RotateXYZParserTween();
                if (fromTo.equals("to"))
                    baseTween.to(view, TweenAction.ROTATION_XYZ, duration).target(rotateX, rotateY, rotateZ).delay(delay).ease(tweenEquation);
                else
                    baseTween.from(view, TweenAction.ROTATION_XYZ, duration).target(rotateX, rotateY, rotateZ).delay(delay).ease(tweenEquation);
                break;
            case "MoveXYZAction":
                baseTween = new MoveXYZParserTween();
                if (fromTo.equals("to"))
                    baseTween.to(view, TweenAction.POS_XYZ, duration).target(x, y, z).delay(delay).ease(tweenEquation);
                else
                    baseTween.from(view, TweenAction.POS_XYZ, duration).target(x, y, z).delay(delay).ease(tweenEquation);
                break;
            case "ColorAction":
                baseTween = new ColorParserTween();
                if (fromTo.equals("to"))
                    baseTween.to(view, TweenAction.TINT, duration).target(tint[0], tint[1], tint[2]).delay(delay).ease(tweenEquation);
                else
                    baseTween.from(view, TweenAction.TINT, duration).target(tint[0], tint[1], tint[2]).delay(delay).ease(tweenEquation);
                break;
        }

        baseTween.fromTo = fromTo;
        baseTween.reverse = reverse;

        if (reverse.equals("repeat")) {
            String[] repeatValue = reverseValue.split(",");
            baseTween.repeat(Integer.parseInt(repeatValue[0]), Float.parseFloat(repeatValue[1]));
        } else if (reverse.equals("repeatYoyo")) {
            String[] repeatValue = reverseValue.split(",");
            baseTween.repeatYoyo(Integer.parseInt(repeatValue[0]), Float.parseFloat(repeatValue[1]));
        }

        view.actonBean.animations.put(animationName, baseTween);
    }


    private static void parserAnimationGroupAttributes(View view, String name, XmlPullParser parser) {
        BaseGroupParserTween timeline = null;
        if (name.equals("Sequence")) {
            timeline = new SequenceParserTween();
        } else {
            timeline = new ParallelParserTween();
        }

        // repeatYoyo
        String reverse = "";
        String reverseValue = "";
        String fromTo = "to";

        int screenAttributeCount = parser.getAttributeCount();
        if (screenAttributeCount > 0) {
            for (int i = 0; i < screenAttributeCount; i++) {
                String key = parser.getAttributeName(i);
                String value = parser.getAttributeValue(i);
                if (key.equals("name")) {
                    view.actonBean.animations.put(value, timeline);
                } else if (key.equals("item")) {
                    String[] items = value.split(",");
                    for (int z = 0; z < items.length; z++) {
                        BaseParserTween item = view.actonBean.animations.get(items[z]);
                        timeline.push(item);
                    }
                } else if (key.equals("repeat") || key.equals("repeatYoyo")) {
                    reverse = key;
                    reverseValue = value;
                }else if (key.equals("fromTo")) {
                    fromTo = value;
                }
            }
        }


        timeline.fromTo = fromTo;
        timeline.reverse = reverse;

        if (reverse.equals("repeat")) {
            String[] repeatValue = reverseValue.split(",");
            timeline.repeat(Integer.parseInt(repeatValue[0]), Float.parseFloat(repeatValue[1]));
        } else if (reverse.equals("repeatYoyo")) {
            String[] repeatValue = reverseValue.split(",");
            timeline.repeatYoyo(Integer.parseInt(repeatValue[0]), Float.parseFloat(repeatValue[1]));
        }

    }

    private static void parserAnimationGroupAttributes(ViewGroup view, String name, XmlPullParser parser) {
        BaseGroupParserTween timeline = null;
        if (name.equals("Sequence")) {
            timeline = new SequenceParserTween();
        } else {
            timeline = new ParallelParserTween();
        }

        // repeatYoyo
        String reverse = "";
        String reverseValue = "";
        String fromTo = "to";

        int screenAttributeCount = parser.getAttributeCount();
        if (screenAttributeCount > 0) {
            for (int i = 0; i < screenAttributeCount; i++) {
                String key = parser.getAttributeName(i);
                String value = parser.getAttributeValue(i);
                if (key.equals("name")) {
                    view.actonBean.animations.put(value, timeline);
                } else if (key.equals("item")) {
                    String[] items = value.split(",");
                    for (int z = 0; z < items.length; z++) {
                        BaseParserTween item = view.actonBean.animations.get(items[z]);
                        timeline.push(item);
                    }
                } else if (key.equals("repeat") || key.equals("repeatYoyo")) {
                    reverse = key;
                    reverseValue = value;
                }else if (key.equals("fromTo")) {
                    fromTo = value;
                }
            }
        }

        timeline.fromTo = fromTo;
        timeline.reverse = reverse;

        if (reverse.equals("repeat")) {
            String[] repeatValue = reverseValue.split(",");
            timeline.repeat(Integer.parseInt(repeatValue[0]), Float.parseFloat(repeatValue[1]));
        } else if (reverse.equals("repeatYoyo")) {
            String[] repeatValue = reverseValue.split(",");
            timeline.repeatYoyo(Integer.parseInt(repeatValue[0]), Float.parseFloat(repeatValue[1]));
        }

    }

    //    //AlphaAction ScaleXYZAction RotateXYZAction MoveXYZAction ColorAction
    public static boolean isAnimation(String parser) {
        return parser.equals("Parallel") || parser.equals("Sequence") || parser.equals("AlphaAction") || parser.equals("ScaleXYZAction") || parser.equals("MoveXYZAction") || parser.equals("RotateXYZAction") || parser.equals("ColorAction");
    }

    public static boolean isGroup(String parser) {
        return parser.equals("Parallel") || parser.equals("Sequence");
    }


    private static void parserCustomRotateAnimationAttributes(Map<String, BaseParserTween> animations, CustomRotateAnimation customRotateAnimation, XmlPullParser parser) {
        int screenAttributeCount = parser.getAttributeCount();
        for (int i = 0; i < screenAttributeCount; i++) {
            String name = parser.getAttributeName(i);
            String value = parser.getAttributeValue(i);
            switch (name) {
                case "name":
                    customRotateAnimation.name = value;
                    animations.put(value, customRotateAnimation);
                    break;
                case "rotateXScript":
                    customRotateAnimation.rotateXScript = value;
                    break;
                case "isValueAdd":
                    customRotateAnimation.isValueAdd = value.equals("true")?true:false;
                    break;
                case "isSubValueAdd":
                    customRotateAnimation.isSubValueAdd = value.equals("true")?true:false;
                    break;
                case "rotateYScript":
                    customRotateAnimation.rotateYScript = value;
                    break;
                case "rotateZScript":
                    customRotateAnimation.rotateZScript = value;
                    break;
                case "subObjectName":
                    customRotateAnimation.subObjectName = value;
                    break;
                case "alpha":
                    customRotateAnimation.alpha = Float.parseFloat(value);
                    break;
            }
        }

    }

    private static void parserCustomAccelerometerAnimationAttributes(Map<String, BaseParserTween> animations, CustomAccelerometerAnimation customRotateAnimation, XmlPullParser parser) {
        int screenAttributeCount = parser.getAttributeCount();
        for (int i = 0; i < screenAttributeCount; i++) {
            String name = parser.getAttributeName(i);
            String value = parser.getAttributeValue(i);
            switch (name) {
                case "name":
                    customRotateAnimation.name = value;
                    animations.put(value, customRotateAnimation);
                    break;
                case "aX":
                    customRotateAnimation.aX = value.equals("true")?true:false;
                    break;
                case "aY":
                    customRotateAnimation.aY = value.equals("true")?true:false;
                    break;
                case "rX":
                    customRotateAnimation.rX = value.equals("true")?true:false;
                    break;
                case "rY":
                    customRotateAnimation.rY = value.equals("true")?true:false;
                    break;
                case "isValueAdd":
                    customRotateAnimation.isValueAdd = value.equals("true")?true:false;
                    break;

                case "aXspeex":
                    customRotateAnimation.aXSpeexscript = value;
                    break;
                case "aYspeex":
                    customRotateAnimation.aYSpeexscript = value;
                    break;
                case "rXspeex":
                    customRotateAnimation.rXSpeexscript = value;
                    break;
                case "rYspeex":
                    customRotateAnimation.rYSpeexscript = value;
                    break;

                case "aStartX":
                    customRotateAnimation.aStartXscript = value;
                    break;
                case "aEndX":
                    customRotateAnimation.aEndXscript = value;
                    break;
                case "aStartY":
                    customRotateAnimation.aStartYscript = value;
                    break;
                case "aEndY":
                    customRotateAnimation.aEndYscript = value;
                    break;

                case "rStartX":
                    customRotateAnimation.rStartXscript = value;
                    break;
                case "rEndX":
                    customRotateAnimation.rEndXscript = value;
                    break;
                case "rStartY":
                    customRotateAnimation.rStartYscript = value;
                    break;
                case "rEndY":
                    customRotateAnimation.rEndYscript = value;
                    break;
            }
        }

    }


    /**
     * 初始化view的基础属性
     *
     * @param view
     * @param parser
     */
    private static void parserBaseAttributes(View view, XmlPullParser parser) {
        ActorBean actorBean = view.actonBean;
        int screenAttributeCount = parser.getAttributeCount();
        for (int i = 0; i < screenAttributeCount; i++) {
            String name = parser.getAttributeName(i);
            String value = parser.getAttributeValue(i);
            switch (name) {
                case "name":
                    actorBean.name = value;
                    break;
                case "x":
                    actorBean.x = ScriptUtils.getValue(value, Float.class);
                    break;
                case "y":
                    actorBean.y = ScriptUtils.getValue(value, Float.class);
                    break;
                case "z":
                    actorBean.z = ScriptUtils.getValue(value, Float.class);
                    break;
                case "effect":
                    actorBean.effect = value;
                    break;
                case "width":
                    actorBean.width = ScriptUtils.getValue(value, Float.class);
                    break;
                case "height":
                    actorBean.height = ScriptUtils.getValue(value, Float.class);
                    break;
                case "scaleX":
                    actorBean.scaleX = ScriptUtils.getValue(value, Float.class);
                    break;
                case "scaleY":
                    actorBean.scaleY = ScriptUtils.getValue(value, Float.class);
                    break;
                case "scaleZ":
                    actorBean.scaleZ = ScriptUtils.getValue(value, Float.class);
                    break;
                case "rotation":
                    actorBean.rotation = ScriptUtils.getValue(value, Float.class);
                    break;
                case "rotationX":
                    actorBean.rotationX = ScriptUtils.getValue(value, Float.class);
                    break;
                case "rotationY":
                    actorBean.rotationY = ScriptUtils.getValue(value, Float.class);
                    break;
                case "originX":
                    actorBean.originX = ScriptUtils.getValue(value, Float.class);
                    break;
                case "originY":
                    actorBean.originY = ScriptUtils.getValue(value, Float.class);
                    break;
                case "originZ":
                    actorBean.originZ = ScriptUtils.getValue(value, Float.class);
                    break;
                case "color":
                    actorBean.color = ScriptUtils.getValue(value, Color.class);
                    break;
                case "resource":
                    actorBean.resource = Scenes3DCore.getFileString(value);
                    break;
                case "thickness":
                    actorBean.thickness = ScriptUtils.getValue(value, Float.class);
                    break;
                case "originWidth":
                    actorBean.originWidth = ScriptUtils.getValue(value, Float.class);
                    break;
                case "originHeight":
                    actorBean.originHeight = ScriptUtils.getValue(value, Float.class);
                    break;
                case "originThickness":
                    actorBean.originThickness = ScriptUtils.getValue(value, Float.class);
                    break;
                case "scaleMode":
                    if (value.equals("width")) {
                        actorBean.scaleMode = 1;
                    } else if (value.equals("height")) {
                        actorBean.scaleMode = 2;
                    } else {
                        actorBean.scaleMode = 0;
                    }
                    break;
                case "lerp":
                    actorBean.lerp = Float.parseFloat(value);
                    break;
                case "isObjType"://是否obj模型
                    if(actorBean instanceof ModelBean){
                        ((ModelBean)actorBean).isObj = value.equals("true")?true:false;
                    }
                    break;
                case "environment"://是否obj模型
                    if(actorBean instanceof ModelBean){
                        ((ModelBean)actorBean).environmentName = value;
                    }
                    break;
                case "enablecubemap"://是否obj模型
                    if(actorBean instanceof ModelBean){
                        ((ModelBean)actorBean).enablecubemap = value.equals("true")?true:false;
                    }
                    break;
                case "enableDepthTest"://是否obj模型
                    if(actorBean instanceof ModelBean){
                        ((ModelBean)actorBean).enableDepthTest = value.equals("true")?true:false;
                    }
                    break;
                case "alphaTest"://是否obj模型
                    if(actorBean instanceof ModelBean){
                        ((ModelBean)actorBean).alphaTest = ScriptUtils.getValue(value, Float.class);
                    }
                    break;
                case "isCullBackFace"://是否obj模型
                    if(actorBean instanceof ModelBean){
                        ((ModelBean)actorBean).isCullBackFace = value.equals("true")?true:false;
                    }
                    break;
                case "objTexture"://是否obj模型纹理
                    if(actorBean instanceof ModelBean){
                        ((ModelBean)actorBean).objTexture = value;
                    }
                    break;

            }
        }


    }


    private static void parserViewGroupBaseAttributes(ViewGroup view, XmlPullParser parser) {
        ActorBean actorBean = view.actonBean;
        int screenAttributeCount = parser.getAttributeCount();
        for (int i = 0; i < screenAttributeCount; i++) {
            String name = parser.getAttributeName(i);
            String value = parser.getAttributeValue(i);
            switch (name) {
                case "name":
                    actorBean.name = value;
                    break;
                case "x":
                    actorBean.x = ScriptUtils.getValue(value, Float.class);
                    break;
                case "y":
                    actorBean.y = ScriptUtils.getValue(value, Float.class);
                    break;
                case "z":
                    actorBean.z = ScriptUtils.getValue(value, Float.class);
                    break;
                case "effect":
                    actorBean.effect = value;
                    break;
                case "width":
                    actorBean.width = ScriptUtils.getValue(value, Float.class);
                    break;
                case "height":
                    actorBean.height = ScriptUtils.getValue(value, Float.class);
                    break;
                case "scaleX":
                    actorBean.scaleX = ScriptUtils.getValue(value, Float.class);
                    break;
                case "scaleY":
                    actorBean.scaleY = ScriptUtils.getValue(value, Float.class);
                    break;
                case "scaleZ":
                    actorBean.scaleZ = ScriptUtils.getValue(value, Float.class);
                    break;
                case "rotation":
                    actorBean.rotation = ScriptUtils.getValue(value, Float.class);
                    break;
                case "rotationX":
                    actorBean.rotationX = ScriptUtils.getValue(value, Float.class);
                    break;
                case "rotationY":
                    actorBean.rotationY = ScriptUtils.getValue(value, Float.class);
                    break;
                case "originX":
                    actorBean.originX = ScriptUtils.getValue(value, Float.class);
                    break;
                case "originY":
                    actorBean.originY = ScriptUtils.getValue(value, Float.class);
                    break;
                case "originZ":
                    actorBean.originZ = ScriptUtils.getValue(value, Float.class);
                    break;
                case "color":
                    actorBean.color = ScriptUtils.getValue(value, Color.class);
                    break;
                case "resource":
                    actorBean.resource = Scenes3DCore.getFileString(value);
                    break;
                case "thickness":
                    actorBean.thickness = ScriptUtils.getValue(value, Float.class);
                    break;
                case "originWidth":
                    actorBean.originWidth = ScriptUtils.getValue(value, Float.class);
                    break;
                case "originHeight":
                    actorBean.originHeight = ScriptUtils.getValue(value, Float.class);
                    break;
                case "originThickness":
                    actorBean.originThickness = ScriptUtils.getValue(value, Float.class);
                    break;
                case "scaleMode":
                    if (value.equals("width")) {
                        actorBean.scaleMode = 1;
                    } else if (value.equals("height")) {
                        actorBean.scaleMode = 2;
                    } else {
                        actorBean.scaleMode = 0;
                    }
                    break;
                case "lerp":
                    actorBean.lerp = Float.parseFloat(value);
                    break;
            }
        }


    }

    public static boolean isView(String tag) {
        return tag.equals("ParticleView") ||tag.equals("ModelView") || tag.equals("ElvesView") || tag.equals("ImageView");
    }

    //容器
    public static boolean isViewGroup(String tag) {
        return tag.equals("ViewGroup");
    }

    static final void beginDocument(XmlPullParser parser, String firstElementName)
            throws XmlPullParserException, IOException {
        int type;
        while ((type = parser.next()) != XmlPullParser.START_TAG
                && type != XmlPullParser.END_DOCUMENT) {
        }
        if (type != XmlPullParser.START_TAG) {
            throw new XmlPullParserException("No start tag found");
        }
        if (!parser.getName().equals(firstElementName)) {
            throw new XmlPullParserException("Unexpected start tag: found " + parser.getName() +
                    ", expected " + firstElementName);
        }


    }
}
