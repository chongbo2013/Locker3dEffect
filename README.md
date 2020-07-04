<!-- Copyright (c) 2020 ferrisxu -->

# 安卓3D场景 锁屏配置模版框架

## 采用技术
1、基于libgdx游戏引擎进行插件化开发模版  
2、采用xml进行解析配置模版，封装多种3D控件可自由组合  
ElvesView(spine2D骨骼动画)  
ImageView（2d图片）  
ModelView(3d模型控件，支持存放3d模型场景)  
ParticleView（粒子效果控件）  
View(3d view)  
ViewGroup(3d ViewGroup)  
3、Events事件消息系统，可以接受View传递到事件或者点击效果等。  
4、Animations动画扩展类CustomRotateAnimation 支持3D翻转动画、CustomAccelerometerAnimation支持重力感应动画  
5、支持Face蒙皮效果，分为GifFace（支持播放Gif），NormalFace（支持3D法线贴图），TextureFace（纹理贴图），VideoFace(支持将视频贴到模型或者View中)。其他Face,RedDotFace(绘制小红点)，TimeFace（绘制时间）

## 预览图
<p align="left">
  <img alt="android lock pre" width="270" height="480" src="https://github.com/chongbo2013/Launcher3dEffect/blob/master/WX20200704-202931%402x.png">
</p>


## 作者其他作品推荐
<p align="left">
  <img alt="android lock pre" width="270" height="480" src="https://www.coolapk.com/apk/com.phone.launcher.android_live_HD_wallpaper_efficiency_customize_apsu#ex-screenshot-carousel">
  
   <a href="https://www.coolapk.com/apk/com.phone.launcher.android_live_HD_wallpaper_efficiency_customize_apsu"><img alt="GO Play Store " 
</p>



## DIY主题模版样例

**xml解析配置**
```
<?xml version="1.0" encoding="utf-8"?>
<DIYThemes>
    <!--延迟多久，真正移除锁屏，这个时间可以做解锁动画-->
    <Config unLockDelay="500" />
    <Screen
        name="main"
        height="screenHeight"
        originX="screenWidth*0.5"
        originY="screenHeight*0.5"
        width="screenWidth"
        x="0.0"
        y="0.0"
        z="0.0">
        <!--天空-->
        <ModelView
            name="scene_bg2"
            color="1.0,1.0,1.0,1.0"
            lerp="0.05"
            originHeight="369"
            originWidth="369"
            originThickness="467"
            originX="screenWidth*0.5"
            originY="screenHeight*0.5"
            originZ="0"
            resource="data/g3d/lay2.g3dj"
            scaleMode="height"
            scaleX="2.0"
            scaleY="2.0"
            scaleZ="2.0"
            width="screenWidth"
            height="screenHeight"
            thickness="screenWidth"
            x="0"
            y="0"
            z="0"
            >
            <Animations>
                <CustomRotateAnimation
                    name="scene_bg_rotate_y2"
                    isValueAdd="true"
                    rotateYScript="-(touchDeltaX*0.5)"
                    />
                <CustomRotateAnimation
                    name="scene_bg_rotate_z_2"
                    rotateYScript="0.15"
                    isValueAdd="true"
                     />
            </Animations>
            <Events>
                <Continue
                    name="scene_bg_ctn_rotate_y_2"
                    item="scene_bg_rotate_y2"
                    type="Animation" />
                <Continue
                    name="scene_bg_ctn_rotate_z_2"
                    item="scene_bg_rotate_z_2"
                    type="Animation" />
            </Events>
        </ModelView>

        <!--屋子1-->
        <ModelView
            name="scene_bg3"
            color="1.0,1.0,1.0,1.0"
            lerp="0.05"
            originHeight="369"
            originWidth="369"
            originThickness="467"
            originX="screenWidth*0.5"
            originY="screenHeight*0.2*0.5"
            originZ="0"
            resource="data/g3d/lay3.g3dj"
            scaleMode="height"
            scaleX="1.0"
            scaleY="1.0"
            scaleZ="1.0"
            width="screenWidth"
            height="screenHeight*0.4"
            thickness="screenWidth"
            x="0"
            y="screenHeight*0.35"
            z="0"
        >
            <Animations>
                <CustomRotateAnimation
                    name="scene_bg_rotate_y3"
                    acceleromoterX="true"
                    isValueAdd="true"
                    rotateYScript="-(touchDeltaX*0.5)"
                />
                <CustomRotateAnimation
                    name="scene_bg_rotate_z_3"
                    rotateYScript="0.15"
                    isValueAdd="true"
                />
            </Animations>

            <Events>
                <Continue
                    name="scene_bg_ctn_rotate_y_1_3"
                    item="scene_bg_rotate_y3"
                    type="Animation" />

                <Continue
                    name="scene_bg_ctn_rotate_z_3"
                    item="scene_bg_rotate_z_3"
                    type="Animation" />
            </Events>
        </ModelView>


        <ModelView
            name="scene_bg4"
            color="1.0,1.0,1.0,1.0"
            lerp="0.05"
            originHeight="369"
            originWidth="369"
            originThickness="467"
            originX="screenWidth*0.5"
            originY="screenHeight*0.2*0.5"
            originZ="0"
            resource="data/g3d/lay4.g3dj"
            scaleMode="height"
            scaleX="2.0"
            scaleY="2.0"
            scaleZ="2.0"
            width="screenWidth"
            height="screenHeight*0.4"
            thickness="screenWidth"
            x="0"
            y="screenHeight*0.3"
            z="0"
        >
            <Animations>
                <CustomRotateAnimation
                    name="scene_bg_rotate_y4"
                    acceleromoterX="true"
                    isValueAdd="true"
                    rotateYScript="-(touchDeltaX*0.5)"
                />

                <!--支持控件根据重力感应DIY效果-->
                <CustomAccelerometerAnimation
                    name="scene_bg_acceleromet_test4"
                    rY="true"
                    rStartY="-360"
                    rEndY="360"
                    rYspeex="if(isTouched==1){0}else{10}"
                    isValueAdd="true"
                />
                <CustomRotateAnimation
                    name="scene_bg_rotate_z_4"
                    rotateYScript="0.15"
                    isValueAdd="true"
                />
            </Animations>

            <Events>
                <Continue
                    name="scene_bg_ctn_rotate_y_1_4"
                    item="scene_bg_rotate_y4"
                    type="Animation" />
                <Continue
                    name="scene_bg_ctn_rotate_z_acceleromet_test4"
                    item="scene_bg_acceleromet_test4"
                    type="Animation" />
                <Continue
                    name="scene_bg_ctn_rotate_z_4"
                    item="scene_bg_rotate_z_4"
                    type="Animation" />
            </Events>
        </ModelView>


        <ModelView
            name="scene_bg1"
            color="1.0,1.0,1.0,1.0"
            lerp="0.05"
            originHeight="369"
            originWidth="369"
            originThickness="467"
            originX="screenWidth*0.5"
            originY="screenHeight*0.2*0.5"
            originZ="0"
            resource="data/g3d/lay1.g3dj"
            scaleMode="height"
            scaleX="1.0"
            scaleY="1.0"
            scaleZ="1.0"
            width="screenWidth"
            height="screenHeight*0.2"
            thickness="screenWidth"
            x="0"
            y="screenHeight*0.35"
            z="0"
        >
            <Animations>
                <CustomRotateAnimation
                    name="scene_bg_rotate_y1"
                    acceleromoterX="true"
                    isValueAdd="true"
                    rotateYScript="-(touchDeltaX*0.5)"
                />

                <!--支持控件根据重力感应DIY效果-->
                <CustomAccelerometerAnimation
                    name="scene_bg_acceleromet_test1"
                    rY="true"
                    rStartY="-360"
                    rEndY="360"
                    rYspeex="if(isTouched==1){0}else{10}"
                    isValueAdd="true"
                />
                <CustomRotateAnimation
                    name="scene_bg_rotate_z_1"
                    rotateYScript="0.15"
                    isValueAdd="true"
                />

            </Animations>

            <Events>
                <Continue
                    name="scene_bg_ctn_rotate_y_1_1"
                    item="scene_bg_rotate_y1"
                    type="Animation" />
                <Continue
                    name="scene_bg_ctn_rotate_z_acceleromet_test1"
                    item="scene_bg_acceleromet_test1"
                    type="Animation" />
                <Continue
                    name="scene_bg_ctn_rotate_z_11"
                    item="scene_bg_rotate_z_1"
                    type="Animation" />
            </Events>
        </ModelView>


        <!--<ImageView-->
            <!--name="scene_clock"-->
            <!--color="1.0,1.0,1.0,1.0"-->
            <!--originX="screenWidth*0.75*0.5"-->
            <!--originY="267/602*screenWidth*0.2*0.5"-->
            <!--width="screenWidth*0.75"-->
            <!--height="267/602*screenWidth*0.75"-->
            <!--x="screenWidth*0.25*0.5"-->
            <!--y="screenHeight*0.68"-->
        <!--&gt;-->

            <!--<TimeFace-->
                <!--name="scene_clock_diffuse_face"-->
                <!--bg="atlas_time_128px_d_,png,0,0,602,267,flipv=false,fliph=false,clock/"-->
                <!--height="267"-->
                <!--width="602"-->
                <!--time1="atlas_time_128px_d_,png,170,88,77,88,flipv=false,fliph=false,clock/"-->
                <!--time2="atlas_time_128px_d_,png,220,88,77,88,flipv=false,fliph=false,clock/"-->
                <!--dot="atlas_time_128px_d_,png,260,88,77,88,flipv=false,fliph=false,clock/"-->
                <!--time3="atlas_time_128px_d_,png,290,88,77,88,flipv=false,fliph=false,clock/"-->
                <!--time4="atlas_time_128px_d_,png,344,88,77,88,flipv=false,fliph=false,clock/"-->
                <!--date="MM/dd|MM/dd|#fdf2e2|Puff.otf|dontStroke|0,left,262,174,6,35,flipv=false,fliph=false,clock/"-->
                <!--weather="temperature|#fdf2e2|Puff.otf|dontStroke|0,left,320,174,6,35,flipv=false,fliph=false,clock/"-->
            <!--/>-->
            <!--&lt;!&ndash;week="#ff404040|Puff.otf|dontStroke|0,left,300,110,112,25,flipv=true,fliph=false,clock/"&ndash;&gt;-->
            <!--<Animations>-->

            <!--</Animations>-->
            <!--<Events>-->
            <!--</Events>-->
        <!--</ImageView>-->

        <!--<ModelView-->
            <!--name="scene_light_clock"-->
            <!--color="1.0,1.0,1.0,1.0"-->
            <!--lerp="0.05"-->
            <!--originHeight="27.6"-->
            <!--originWidth="92.8"-->
            <!--originThickness="3"-->
            <!--originX="screenWidth*0.78*0.5"-->
            <!--originY="27.6/92.8*screenWidth*0.78*0.5"-->
            <!--originZ="0"-->
            <!--resource="data/g3d/clock.g3dj"-->
            <!--scaleMode="width"-->
            <!--scaleX="1.0"-->
            <!--scaleY="1.0"-->
            <!--environment="environment_03"-->
            <!--scaleZ="1.0"-->
            <!--width="screenWidth*0.78"-->
            <!--height="27.6/92.8*screenWidth*0.78"-->
            <!--thickness="3"-->
            <!--x="screenWidth*0.11"-->
            <!--y="screenHeight*0.68"-->
            <!--z="0"-->
        <!--&gt;-->

            <!--<TimeFace-->
                <!--name="scene_clock_diffuse_face"-->
                <!--bg="clock_d_,png,0,0,512,168,flipv=false,fliph=false,clock/"-->
                <!--dot="atlas_time_128px_dw_08_,png,211,39,73,64,flipv=false,fliph=false,clock/"-->
                <!--height="168"-->
                <!--objectName="03 - Default"-->
                <!--time1="atlas_time_128px_dw_08_,png,51,40,73,64,flipv=false,fliph=false,clock/"-->
                <!--time2="atlas_time_128px_dw_08_,png,131,40,73,64,flipv=false,fliph=false,clock/"-->
                <!--time3="atlas_time_128px_dw_08_,png,292,39,73,64,flipv=false,fliph=false,clock/"-->
                <!--time4="atlas_time_128px_dw_08_,png,372,40,73,64,flipv=false,fliph=false,clock/"-->
                <!--date="MM月dd日|MM/dd|#ffffff,left,100,110,112,25,flipv=false,fliph=false,clock/"-->
                <!--week="#ffffff,left,300,110,112,25,flipv=false,fliph=false,clock/"-->
                <!--width="512" />-->
            <!--&lt;!&ndash;有了NormalFace就有凹凸的感觉，和TimeFace 是配对的，当然，你可以没有TimeNormalFace&ndash;&gt;-->
            <!--<TimeNormalFace-->
                <!--name="scene_clock_normal_face"-->
                <!--bg="clock_n_,png,0,0,512,168,flipv=false,fliph=false,clock/"-->
                <!--dot="numble_alpha_nomal_,png,211,39,73,64,flipv=false,fliph=false,clock/"-->
                <!--height="168"-->
                <!--objectName="03 - Default"-->
                <!--time1="numble_alpha_nomal_,png,51,40,73,64,flipv=false,fliph=false,clock/"-->
                <!--time2="numble_alpha_nomal_,png,131,40,73,64,flipv=false,fliph=false,clock/"-->
                <!--time3="numble_alpha_nomal_,png,292,39,73,64,flipv=false,fliph=false,clock/"-->
                <!--time4="numble_alpha_nomal_,png,372,40,73,64,flipv=false,fliph=false,clock/"-->
                <!--width="512" />-->

            <!--<Animations>-->
                <!--<CustomRotateAnimation-->
                    <!--name="scene_clock_3d_animation_1"-->
                    <!--rotateXScript="Math.sin(time)*5"-->
                    <!--rotateYScript="Math.cos(time)*5" />-->

                <!--<RotateXYZAction-->
                    <!--name="scene_clock_animation_2"-->
                    <!--duration="0.2"-->
                    <!--rotateX="135.0"-->
                    <!--rotateY="135.0"-->
                    <!--rotateZ="0.0" />-->
                <!--<RotateXYZAction-->
                    <!--name="scene_clock_animation_3"-->
                    <!--duration="0.2"-->
                    <!--rotateX="0.0"-->
                    <!--rotateY="0.0"-->
                    <!--rotateZ="0.0" />-->
                <!--<Sequence-->
                    <!--name="scene_clock_animation_4"-->
                    <!--item="scene_clock_animation_2,scene_clock_animation_3" />-->
            <!--</Animations>-->

            <!--<Events>-->
                <!--<Continue-->
                    <!--name="scene_clock_3d_evs_ctn_rte_xy"-->
                    <!--item="scene_clock_3d_animation_1"-->
                    <!--type="Animation" />-->
                <!--<Click-->
                    <!--name="scene_clock_evs_clk_rte_xy"-->
                    <!--item="scene_clock_animation_4"-->
                    <!--type="Animation" />-->
            <!--</Events>-->
        <!--</ModelView>-->


        <ModelView
            name="scene_light_clock2"
            color="1.0,1.0,1.0,1.0"
            lerp="0.05"
            originHeight="197.57"
            originWidth="197.57"
            originThickness="1"
            originX="screenWidth*0.78*0.5"
            originY="27.6/92.8*screenWidth*0.78*0.5"
            originZ="0"
            resource="data/g3d/screen.g3dj"

            scaleX="1.0"
            scaleY="1.0"
            environment="environment_03"
            scaleZ="1.0"
            width="screenWidth*0.78"
            height="screenWidth*0.78"
            thickness="1"
            x="screenWidth*0.11"
            y="screenHeight*0.68"
            z="0"
        >

            <VideoFace
            name="testVideo"
            objectName="Material #25"
            resource="video.mp4" />
        </ModelView>

        <ModelView
            name="scene_light_clock2"
            color="1.0,1.0,1.0,1.0"
            lerp="0.05"
            originHeight="27.6"
            originWidth="92.8"
            originThickness="3"
            originX="screenWidth*0.78*0.5"
            originY="27.6/92.8*screenWidth*0.78*0.5"
            originZ="0"
            resource="data/g3d/clock.g3dj"
            scaleMode="height"
            scaleX="1.0"
            scaleY="1.0"
            environment="environment_03"
            scaleZ="1.0"
            width="screenWidth*0.78"
            height="27.6/92.8*screenWidth*0.78"
            thickness="3"
            x="screenWidth*0.11"
            y="screenHeight*0.48"
            z="0"
        >
            <Animations>
                <CustomRotateAnimation
                    name="scene_clock_3d_animation_1"
                    rotateXScript="Math.sin(time)*5"
                    rotateYScript="Math.cos(time)*5" />

            </Animations>

            <Events>
                <Continue
                    name="scene_clock_3d_evs_ctn_rte_xy"
                    item="scene_clock_3d_animation_1"
                    type="Animation" />
            </Events>
        </ModelView>
        <!--短信控件-->
        <ImageView
            name="iv_msm"
            color="1.0,1.0,1.0,1.0"
            width="screenWidth*0.2"
            height="screenWidth*0.2"
            originX="screenWidth*0.3*0.2"
            originY="screenWidth*0.3*0.8"
            x="-screenWidth*0.25"
            y="screenHeight*0.6">
            <TextureFace
                name="tex_face_msm"
                resource="data/g3d/msm.png" />

            <!--小红点的宽高-->
            <RedDotFace
                name="tex_face_msm_redpoint"
                width="screenWidth*0.2*0.2"
                height="screenWidth*0.2*0.2"
                x="screenWidth*0.2*0.6"
                y="screenWidth*0.2*0.6"
                />


            <!--1、收到新消息后，显示
               从左边出来
            -->
            <Animations>

                <MoveXYZAction
                    name="action_msg_move_gone"
                    duration="0.3"
                    x="-screenWidth*0.25"
                    y="screenHeight*0.6"
                    z="0" />
                <MoveXYZAction
                    name="action_msg_move_show"
                    duration="0.3"
                    x="screenWidth*0.05"
                    y="screenHeight*0.6"
                    z="0.0" />


                <RotateXYZAction
                    name="action_continue_rotate_1"
                    duration="0.5"
                    rotateX="0.0"
                    rotateY="0.0"
                    rotateZ="0"
                    interpolator="linearTween"/>
                <RotateXYZAction
                    name="action_continue_rotate_2"
                    duration="0.5"
                    rotateX="0.0"
                    rotateY="0.0"
                    rotateZ="20"
                    interpolator="linearTween"/>
                <Sequence
                    name="action_continue_se_rotate_1"
                    item="action_continue_rotate_1,action_continue_rotate_2"
                    repeatYoyo="-1,0"

                />



            </Animations>

            <Events>
                <Continue
                    name="event_continue_animation"
                    type="Animation"
                    item="action_continue_se_rotate_1"
                    once="true"
                />
                <!--点击按钮，打开消息界面-->
                <Click
                    name="event_click_open_msgView"
                    item="open_msg"
                    type="Open" />

                <!--来新消息显示短信图标-->
                <NewMsg
                    name="event_new_msg"
                    type="Animation"
                    item="action_msg_move_show"
                />
                <!--没有消息，隐藏图标-->
                <NoMsg
                    name="event_no_msg"
                    type="Animation"
                    item="action_msg_move_gone"
                />
            </Events>

        </ImageView>


        <!--解锁控件-->
        <ImageView
            name="iv_unlock"
            color="1.0,1.0,1.0,1.0"
            originX="screenWidth*0.45*0.5"
            originY="93/338*screenWidth*0.45*0.5"
            x="screenWidth*0.275"
            y="screenHeight*0.15"
            width="screenWidth*0.45"
            height="93/338*screenWidth*0.45"
            effect="Gradient"
        >
            <TextureFace
                name="tex_face_unlock"
                resource="data/g3d/l_widget_unlocker.png" />

            <Animations>
                <MoveXYZAction
                    name="screen_mv_lock"
                    duration="0.2"
                    x="screenWidth*0.275"
                    y="screenHeight*0.15"
                    z="0" />
                <MoveXYZAction
                    name="screen_mv_unlock"
                    duration="0.2"
                    x="screenWidth"
                    y="screenHeight*0.15"
                    z="0" />
            </Animations>
            <Events>
                <Lock
                    name="event03"
                    item="screen_mv_lock"
                    type="Animation" />
                <Unlock
                    name="event07"
                    item="screen_mv_unlock"
                    type="Animation" />
            </Events>

            <!--上滑解锁示例-->
            <!--alphaActions关联其他控件 透明度-->
            <!--距离和速度是配合使用distance & speed-->
            <UnLockConfig
                distance="screenWidth*0.05"
                speed="3000"
                fbo="false"
                fromX="screenWidth*0.275"
                fromY="screenHeight*0.15"
                interpolator="linearTween"
                threshold="screenWidth*0.48"
                time="0.2"
                toX="screenWidth*0.7"
                toY="screenHeight*0.15"
                fromAlpha="1"
                toAlpha="0"
                alphaSpeex="1.0"
                alphaActions="main|1|0"
            />

        </ImageView>

        <Animations>
            <AlphaAction
                name="screen_mv_unlock"
                duration="0.2"
                opacity="0"
                />
            <AlphaAction
                name="screen_mv_lock"
                duration="0.2"
                opacity="1"
            />
        </Animations>
        <Events>
             <Lock
                name="event_screen_alpha_lock2"
                item="screen_mv_lock"
                type="Animation" />
            <Unlock
                name="event_screen_alpha_lock3"
                item="screen_mv_unlock"
                type="Animation" />
        </Events>
    </Screen>

</DIYThemes>
```

