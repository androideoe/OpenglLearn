package com.example.opengllearn.opengl.shapes

import android.opengl.GLES20
import com.example.opengllearn.opengl.CustomRenderer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Triangle {
    private val vertexShaderCode = "attribute vec4 vPosition;" +
            "uniform mat4 uMVPMatrix;" +
            "varying  vec4 vColor;" +
            "attribute vec4 aColor;" +
            "void main() {" +
            "  gl_Position = uMVPMatrix*vPosition;" +
            "  vColor=aColor;" +
            "}"
    private val fragmentShaderCode = "precision mediump float;" +
            "varying vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}"
    private val mProgram: Int
    private val vertexBuffer: FloatBuffer
    private val colorBuffer: FloatBuffer
    private var mMVPMatrixHandle = 0

    // Set color with red, green, blue and alpha (opacity) values
    var color = floatArrayOf(
        1.0f, 0f, 0f, 1.0f,
        0f, 1.0f, 0f, 1.0f,
        0f, 0f, 1.0f, 1.0f
    )
    private var mPositionHandle = 0
    private var mColorHandle = 0
    private val vertexCount = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    fun draw(mvpMatrix: FloatArray?) {
        // 清除预设值的缓冲区
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        // 使用程序对象mProgram作为当前渲染状态的一部分
        GLES20.glUseProgram(mProgram)

        // 特定统一变量的位置
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")

        // 通过一致变量（uniform修饰的变量）引用将一致变量值传入渲染管线
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0)

        // 获取属性变量的索引
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition")

        // 启用通用顶点属性数组
        GLES20.glEnableVertexAttribArray(mPositionHandle)

        // 指定了渲染时索引值为 index 的顶点属性数组的数据格式和位置
        GLES20.glVertexAttribPointer(
            mPositionHandle, COORDS_PER_VERTEX,
            GLES20.GL_FLOAT, false,
            vertexStride, vertexBuffer
        )

        /* // 获取片段着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // 设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, colorBuffer, 0);*/

        //获取片元着色器的vColor成员的句柄
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor")
        //设置绘制三角形的颜色
        GLES20.glEnableVertexAttribArray(mColorHandle)
        GLES20.glVertexAttribPointer(
            mColorHandle, 4,
            GLES20.GL_FLOAT, false,
            0, colorBuffer
        )


        // 画三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)

        // 禁用顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle)
    }

    companion object {
        // number of coordinates per vertex in this array
        const val COORDS_PER_VERTEX = 3

        /*static float triangleCoords[] = {   // in counterclockwise order:
            0.0f,  0.5f, 0.0f, // top
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f  // bottom right
    };

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 255, 0, 0, 1.0f };*/
        var triangleCoords = floatArrayOf( // in counterclockwise order:
            0.0f, 0.622008459f, 0.0f,  // top
            -0.5f, -0.311004243f, 0.0f,  // bottom left
            0.5f, -0.311004243f, 0.0f // bottom right
        )
    }

    init {
        val bb = ByteBuffer.allocateDirect(triangleCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer.put(triangleCoords)
        vertexBuffer.position(0)
        val dd = ByteBuffer.allocateDirect(color.size * 4)
        dd.order(ByteOrder.nativeOrder())
        colorBuffer = dd.asFloatBuffer()
        colorBuffer.put(color)
        colorBuffer.position(0)
        val vertexShader: Int = CustomRenderer().compileShader(
            GLES20.GL_VERTEX_SHADER,
            vertexShaderCode
        )
        val fragmentShader: Int = CustomRenderer().compileShader(
            GLES20.GL_FRAGMENT_SHADER,
            fragmentShaderCode
        )

        // 创建空的OpenGL ES程序
        mProgram = GLES20.glCreateProgram()

        // 添加顶点着色器来编程
        GLES20.glAttachShader(mProgram, vertexShader)

        // 添加片段着色器来编程
        GLES20.glAttachShader(mProgram, fragmentShader)

        // 创建OpenGL ES程序可执行文件
        GLES20.glLinkProgram(mProgram)
    }
}