#extension GL_OES_EGL_image_external : require
precision mediump float;
uniform samplerExternalOES videoTex;// 图片 采样器
varying vec2 textureCoordinate;

void main() {
        vec4 tc = texture2D(videoTex, textureCoordinate);
        float color = tc.r * 0.3 + tc.g * 0.59 + tc.b * 0.11; // 这里进行的颜色变换处理,传说中的黑白滤镜
        gl_FragColor = vec4(color,color,color,1.0);

//    float x = textureCoordinate.x;

//    if (x < 1.0/3.0) {
//        x+=1.0/3.0;
//    } else if (x > 2.0/3.0){
//        x-=1.0/3.0;
//    }
    //    if(x < 0.5) {
    //        x+=0.25;
    //    }else {
    //        x-=0.25;
    //    }

//    gl_FragColor = texture2D(videoTex, vec2(x, textureCoordinate.y));
}