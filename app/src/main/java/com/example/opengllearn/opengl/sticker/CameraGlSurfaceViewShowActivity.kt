package com.example.opengllearn.opengl.sticker

import android.graphics.PixelFormat
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.opengllearn.R
import com.example.opengllearn.opengl.utils.AssetsUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CameraGlSurfaceViewShowActivity : AppCompatActivity(),
    SurfaceTexture.OnFrameAvailableListener {
    val instance by lazy { this }
    private lateinit var mSurfaceTexture: SurfaceTexture
    private lateinit var camera: Camera
    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var myRender: MyRender


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_gl_surface_view_show)
        glSurfaceView = findViewById(R.id.camera_glsurface_view)
        glSurfaceView.setEGLContextClientVersion(2)
        myRender = MyRender()
        glSurfaceView.setRenderer(myRender)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

    }


    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        glSurfaceView.requestRender()

    }


    inner class MyRender : GLSurfaceView.Renderer {

        private lateinit var mPosBuffer: FloatBuffer
        private lateinit var mTexBuffer: FloatBuffer
        private var mProgram = -1
        private val mPosCoordinate = floatArrayOf(
            -1f, -1f,
            -1f, 1f,
            1f, -1f,
            1f, 1f)

        private val mPosCoordinate1 = floatArrayOf(
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f)

        // 顺时针转90并沿Y轴翻转  后摄像头正确，前摄像头上下颠倒
        private val mTexCoordinateBackRight = floatArrayOf(
            1f, 1f,
            0f, 1f,
            1f, 0f,
            0f, 0f);

        private val mTexCoordinateBackRight1 = floatArrayOf(
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f
    // 旋转90度
//            1f,0f,
//            1f,1f,
//            0f,0f,
//            0f,1f
//        // 镜像反转
//             1f,1f,
//             1f,0f,
//             0f,1f,
//             0f,0f



        );

        // 顺时针旋转90  后摄像头上下颠倒，前摄像头正确
        private val mTexCoordinateForntRight = floatArrayOf(
            0f, 1f,
            1f, 1f,
            0f, 0f,
            1f, 0f);

        private val mProjectMatrix = FloatArray(16)
        private val mCameraMatrix = FloatArray(16)
        private val mMVPMatrix = FloatArray(16)
        private val mTempMatrix = FloatArray(16)
        private var uPosHandle = 0
        private var aTexHandle = 0
        private var mMVPMatrixHandle = 0

        var mBoolean = false

        init {
            Matrix.setIdentityM(mProjectMatrix, 0);
            Matrix.setIdentityM(mCameraMatrix, 0);
            Matrix.setIdentityM(mMVPMatrix, 0);
            Matrix.setIdentityM(mTempMatrix, 0);

        }

        private fun loadShader(type: Int, shaderCode: String): Int {
            val shader = GLES20.glCreateShader(type)
            // 添加上面编写的着色器代码并编译它
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
            return shader
        }


        private fun createProgram() {
            val vertexSource = AssetsUtils.read(instance, "camera_vertexShader.glsl")
            val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource!!)
            val fragmentSource = AssetsUtils.read(instance, "camera_fragmentShader.glsl");
            val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource!!)

            // 创建空的OpenGL ES程序
            mProgram = GLES20.glCreateProgram()
            // 添加顶点着色器到程序中
            GLES20.glAttachShader(mProgram, vertexShader)
            // 添加片段着色器到程序中
            GLES20.glAttachShader(mProgram, fragmentShader)
            // 创建OpenGL ES程序可执行文件
            GLES20.glLinkProgram(mProgram)

            // 释放shader资源
            GLES20.glDeleteShader(vertexShader)
            GLES20.glDeleteShader(fragmentShader)

        }

        // 添加程序到ES环境中
        private fun activeProgram() {
            // 将程序添加到OpenGL ES环境
            GLES20.glUseProgram(mProgram);
            mSurfaceTexture.setOnFrameAvailableListener(this@CameraGlSurfaceViewShowActivity)
            // 获取顶点着色器的位置的句柄
            uPosHandle = GLES20.glGetAttribLocation(mProgram, "position");
            aTexHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "textureTransform");

            mPosBuffer = convertToFloatBuffer(mPosCoordinate1)
            mTexBuffer = convertToFloatBuffer(mTexCoordinateBackRight1);

            GLES20.glVertexAttribPointer(uPosHandle, 2, GLES20.GL_FLOAT, false, 0, mPosBuffer)
            GLES20.glVertexAttribPointer(aTexHandle, 2, GLES20.GL_FLOAT, false, 0, mTexBuffer)
            // 启用顶点位置的句柄
            GLES20.glEnableVertexAttribArray(uPosHandle)
            GLES20.glEnableVertexAttribArray(aTexHandle)
        }


        private fun convertToFloatBuffer(buffer: FloatArray): FloatBuffer {
            val fb = ByteBuffer.allocateDirect(buffer.size * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
            fb.put(buffer)
            fb.position(0)
            return fb
        }

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
            mSurfaceTexture = SurfaceTexture(createOESTextureObject())
            createProgram()
            try {
                camera = Camera.open(0)
                camera.setPreviewTexture(mSurfaceTexture)
                camera.startPreview()
                camera.autoFocus(object : Camera.AutoFocusCallback {
                    override fun onAutoFocus(success: Boolean, camera: Camera?) {
                        if (success) {
//                            camera?.cancelAutoFocus();
                        }
                    }

                })
            } catch (e: Exception) {
                e.printStackTrace()
            }

            activeProgram()


        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            GLES20.glViewport(0, 0, width, height)
//            Matrix.scaleM(mMVPMatrix, 0, 1f, -1f, 1f)
//            val ratio = width.toFloat() / height
//            // 3和7代表远近视点与眼睛的距离，非坐标点
//            Matrix.orthoM(mProjectMatrix, 0, -1f, 1f, -ratio, ratio, 1f, 7f)
//            // 3代表眼睛的坐标点
//            Matrix.setLookAtM(mCameraMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
//            Matrix.multiplyMM(mMVPMatrix, 0, mProjectMatrix, 0, mCameraMatrix, 0)
            // 矩阵旋转
//            Matrix.rotateM(mMVPMatrix,0,180F,1F,0F,0F)
//            Matrix.rotateM(mMVPMatrix,0,90F,0F,0F,1F)
        }

        override fun onDrawFrame(gl: GL10?) {
            if (mBoolean) {
                activeProgram()
                mBoolean = false
            }
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
            mSurfaceTexture.updateTexImage()
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0)
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, mPosCoordinate1.size / 2)

        }

    }

    fun createOESTextureObject(): Int {
        val tex: IntArray = IntArray(1)
        // 生成一个纹理
        GLES20.glGenBuffers(1, tex, 0)
        // 将此纹理绑定到外部纹理上
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tex[0])
        // 设置纹理过滤参数
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST.toFloat()
        );
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat()
        );
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE.toFloat()
        );
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE.toFloat()
        );
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        return tex[0]
    }


}