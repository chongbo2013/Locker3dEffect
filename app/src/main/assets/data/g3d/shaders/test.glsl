[VS]
#include "g_attributes.glsl:VS"
#include "u_uniforms.glsl"
#include "skinning.glsl"
#include "common.glsl:VS"
#include "common.glsl:tangentVectorsVS"
#include "lights.glsl:lights"


varying vec3 v_lightDir;
varying vec3 v_lightCol;
varying vec3 v_viewDir;
varying vec3 v_ambientLight;
#ifdef environmentCubemapFlag
varying vec3 v_reflect;
#endif

//独立添加over uv transform gdx真是累死人，为啥不把功能实现完整
#ifdef overTextureFlag
uniform vec4 u_overUVTransform;
varying vec2 v_overUV;
#endif
 
//独立添加video uv transform gdx 实现3D模型中跑视频
#ifdef videoTextureFlag
uniform vec4 u_videoUVTransform;
varying vec2 v_videoUV;
#endif

#ifdef diffuseTextureFlag
uniform vec4 u_diffuseUVTransform;
varying vec2 v_diffuseUV;
#endif

#ifdef specularTextureFlag
uniform vec4 u_specularUVTransform;
varying vec2 v_specularUV;
#endif

void main() {




//初始化
    #if defined(positionFlag)
    g_position = vec4(a_position, 1.0);
    #else
	g_position = vec4(0.0, 0.0, 0.0, 1.0);
    #endif


    #if defined(normalFlag)
	g_normal = a_normal;
    #else
	g_normal = vec3(0.0, 0.0, 1.0);
    #endif

    #if defined(binormalFlag)
    	 g_binormal = a_binormal;
    #else
    	 g_binormal = vec3(0.0, 1.0, 0.0);
    #endif // binormalFlag


#if defined(tangentFlag)
	 g_tangent = a_tangent;
#else
	 g_tangent = vec3(1.0, 0.0, 0.0);
#endif // tangentFlag

#if defined(texCoord0Flag)
	 g_texCoord0 = a_texCoord0;
#else
	 g_texCoord0 = vec2(0.0, 0.0);
#endif // texCoord0Flag


//扩展glsl修改OverTexture的纹理坐标实现反光板效果
#ifdef overTextureFlag
		v_overUV = u_overUVTransform.xy + g_texCoord0 * u_overUVTransform.zw;
#endif

//扩展glsl修改VideoTexture的纹理坐标实现视频适配屏幕
#ifdef videoTextureFlag
		v_videoUV = u_videoUVTransform.xy + g_texCoord0 * u_videoUVTransform.zw;
#endif

	#ifdef diffuseTextureFlag
		v_diffuseUV = u_diffuseUVTransform.xy + g_texCoord0 * u_diffuseUVTransform.zw;
	#endif //diffuseTextureFlag

	#ifdef specularTextureFlag
		v_specularUV = u_specularUVTransform.xy + g_texCoord0 * u_specularUVTransform.zw;
	#endif //specularTextureFlag

	calculateTangentVectors();


	g_position = applySkinning(g_position);
	g_normal = normalize(u_normalMatrix * applySkinning(g_normal));
	g_binormal = normalize(u_normalMatrix * applySkinning(g_binormal));
	g_tangent = normalize(u_normalMatrix * applySkinning(g_tangent));

	g_position = u_worldTrans * g_position;
	gl_Position = u_projViewTrans * g_position;

	mat3 worldToTangent;
	worldToTangent[0] = g_tangent;
	worldToTangent[1] = g_binormal;
	worldToTangent[2] = g_normal;

	v_ambientLight = getAmbient(g_normal);

	v_lightDir = normalize(-u_dirLights[0].direction) * worldToTangent;
	v_lightCol = u_dirLights[0].color;
	vec3 viewDir = normalize(u_cameraPosition.xyz - g_position.xyz);
	v_viewDir = viewDir * worldToTangent;
#ifdef environmentCubemapFlag
	v_reflect = reflect(-viewDir, g_normal);
#endif
//	pushColor();
//	pushTexCoord0();
    v_color=g_color;
    v_texCoord0=g_texCoord0;
}


[FS]
#ifdef GL_ES
#define LOWP lowp
#define MED mediump
#define HIGH highp
precision mediump float;
#else
#define MED
#define LOWP
#define HIGH
#endif

#include "g_attributes.glsl:FS"
#include "u_uniforms.glsl"
#include "common.glsl:FS"
#include "common.glsl:colorDiffuseFS"
#include "common.glsl:colorSpecularFS"
#include "common.glsl:colorOver"
#include "common.glsl:colorVideo"
varying vec3 v_lightDir;
varying vec3 v_lightCol;
varying vec3 v_viewDir;
varying vec3 v_ambientLight;
#ifdef environmentCubemapFlag
varying vec3 v_reflect;
#endif

#ifdef environmentCubemapFlag
uniform samplerCube u_environmentCubemap;
#endif

#ifdef reflectionColorFlag
uniform vec4 u_reflectionColor;
#endif

#define saturate(x) clamp( x, 0.0, 1.0 )


#ifdef overTextureFlag
varying MED vec2 v_overUV;
#endif

#ifdef videoTextureFlag
varying MED vec2 v_videoUV;
#endif
#ifdef diffuseTextureFlag
varying MED vec2 v_diffuseUV;
#endif

#ifdef specularTextureFlag
varying MED vec2 v_specularUV;
#endif

void main() {
    g_color = v_color;
    g_texCoord0 = v_texCoord0;
     vec4 diffuse;
        //add video support by ferris.xu
     #if defined(videoTextureFlag)
         vec4 video=texture2D(u_videoTexture, v_videoUV);
         #if defined(diffuseTextureFlag)
         video.rgb*= (texture2D(u_diffuseTexture, v_diffuseUV)).rgb;
         #endif
          #if defined(diffuseColorFlag)
              diffuse= g_color * video * u_diffuseColor;
              #else
              diffuse=g_color*video;
          #endif
          #else
               #if defined(diffuseTextureFlag) || defined(diffuseColorFlag)
               vec4 fetchDiffuse;
               #if defined(diffuseTextureFlag) && defined(diffuseColorFlag)
               fetchDiffuse=(texture2D(u_diffuseTexture, v_diffuseUV) * u_diffuseColor);
               #elif defined(diffuseTextureFlag)
               fetchDiffuse= (texture2D(u_diffuseTexture, v_diffuseUV));
               #elif defined(diffuseColorFlag)
               fetchDiffuse=u_diffuseColor;
               #else
               fetchDiffuse= vec4(1.0);
               #endif
               diffuse= g_color * fetchDiffuse;
               #else
               diffuse=g_color;
               #endif
       #endif


    //fix vivo xplay6 bug ferris
    vec3 specular;
    #if defined(specularTextureFlag) && defined(specularColorFlag)
    specular= (texture2D(u_specularTexture, v_specularUV).rgb * u_specularColor.rgb);
    #elif defined(specularTextureFlag)
    specular=texture2D(u_specularTexture, v_specularUV).rgb;
    #elif defined(specularColorFlag)
    specular=u_specularColor.rgb;
    #else
    specular= vec3(0.0);
    #endif

#ifdef normalTextureFlag
	vec4 N = vec4(normalize(texture2D(u_normalTexture, g_texCoord0).xyz * 2.0 - 1.0), 1.0);
#ifdef environmentCubemapFlag
	vec3 reflectDir = normalize(v_reflect + (vec3(0.0, 0.0, 1.0) - N.xyz));
#endif
#else
	vec4 N = vec4(0.0, 0.0, 1.0, 1.0);
#ifdef environmentCubemapFlag
	vec3 reflectDir = normalize(v_reflect);
#endif
#endif

	vec3 L = normalize(v_lightDir);
	vec3 V = normalize(v_viewDir);
	vec3 H = normalize(L + V);
	float NL = dot(N.xyz, L);
	float NH = max(0.0, dot(N.xyz, H));
	
	float specOpacity = 1.0; //(1.0 - diffuse.w);
	float spec = min(1.0, pow(NH, 10.0) * specOpacity);
	float selfShadow = saturate(4.0 * NL);
#ifdef environmentCubemapFlag
	vec3 environment = textureCube(u_environmentCubemap, reflectDir).rgb;
	specular *= environment;
#ifdef reflectionColorFlag
	diffuse.rgb = saturate(vec3(1.0) - u_reflectionColor.rgb) * diffuse.rgb + environment * u_reflectionColor.rgb;
#endif
#endif


	gl_FragColor = vec4(saturate((v_lightCol * diffuse.rgb) * NL), diffuse.w);
	gl_FragColor.rgb += v_ambientLight * diffuse.rgb;



	 //覆盖在模型上的纹理图片 transform uv
     #if defined(overTextureFlag)
      vec4 over=texture2D(u_overTexture, v_overUV);
      #ifdef environmentCubemapFlag
      over.rgb *= environment;
      #endif
      gl_FragColor.rgb +=  over.rgb;
      #else
      gl_FragColor.rgb += (selfShadow * spec) * specular;
     #endif

//fix 透明度混合问题 by ferris.xu


    //add model blen by ferris.xu
        #ifdef blendedFlag
    		gl_FragColor.a = u_opacity;

    		#ifdef alphaTestFlag
    			if (gl_FragColor.a <= u_alphaTest)
    				discard;
    		#endif
    	#endif
        gl_FragColor.rgb*=gl_FragColor.a;
}
