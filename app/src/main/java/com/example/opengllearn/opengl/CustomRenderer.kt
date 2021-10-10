package com.example.opengllearn.opengl

import android.opengl.GLSurfaceView
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import com.example.opengllearn.opengl.shapes.Square
import com.example.opengllearn.opengl.shapes.Triangle
import javax.microedition.khronos.egl.EGLConfig

class CustomRenderer : GLSurfaceView.Renderer {
    var width = 800
    var height = 480

    private val mRotationMatrix = FloatArray(16)

    @Volatile
    var mAngle = 0f

    @Volatile
    var mTranslationX = 0f

    @Volatile
    var mTranslationY = 0f

    fun getAngle(): Float {
        return mAngle
    }

    fun setAngle(angle: Float) {
        mAngle = angle
    }


    //    lateinit var square: Square
    lateinit var triangle: Triangle

    private val mMVPMatrix =
        floatArrayOf(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f)
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
//        square = Square()
        triangle = Triangle()
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        // Redraw background color
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glViewport(0, 0, width, height)
        val ratio = width.toFloat() / height
        // 这个投影矩阵被应用于对象坐标在onDrawFrame（）方法中
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    override fun onDrawFrame(gl: GL10) {

        // 设置相机位置（查看矩阵）
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // 计算投影和视图变换
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)
        val scratch = FloatArray(16)
        // 为三角形创建一个旋转变换
//        long time = SystemClock.uptimeMillis() % 4000L;
//        float angle = 9f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0f, 0f, -1.0f)

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0)
        // 绘制形状
//        mTriangle.draw(scratch);

//        float x = mTranslationX / (float) AserbaoApplication.screenWidth;
//        float y = mTranslationY / (float) AserbaoApplication.screenHeight;
//        Matrix.translateM(scratch,0,0.5f,0f,0);
//        Matrix.translateM(scratch,0,x,-y,0);
//        Log.e("Matrix.translateM", "onDrawFrame: x=" +x + " y="+ y  );
        triangle.draw(scratch)
    }

    /**
     * 创建编译着色器
     * @param type
     * @param shaderCode
     * @return
     */
    fun compileShader(type: Int, shaderCode: String?): Int {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        val shader = GLES20.glCreateShader(type)
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        // Get the compilation status.
        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(
            shader, GLES20.GL_COMPILE_STATUS,
            compileStatus, 0
        )
        // Verify the compile status.
        if (compileStatus[0] == 0) {
            // If it failed, delete the shader object.
            GLES20.glDeleteShader(shader)
            Log.e("loadShader", "Compilation of shader failed.")
            return 0
        }
        return shader
    }
}