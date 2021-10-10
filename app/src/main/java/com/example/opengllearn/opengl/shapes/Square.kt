package com.example.opengllearn.opengl.shapes

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

/**
 * 正方形
 */
class Square {

    private var vertexBuffer: FloatBuffer? = null
    private var drawListBuffer: ShortBuffer? = null


    companion object {
        const val COORDS_PER_VERTEX = 3
        var squareCoords = floatArrayOf( // in counterclockwise order:
            -0.5f, 0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f, 0.5f, 0.0f
        )   // top right
        var drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)
    }

    fun Square() {
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个float占4个字节
        val bb = ByteBuffer.allocateDirect(squareCoords.size * 4)
        // 数组排列用nativeOrder
        bb.order(ByteOrder.nativeOrder())
        // 从ByteBuffer创建一个浮点缓冲区
        vertexBuffer = bb.asFloatBuffer()
        // 将坐标添加到FloatBuffer
        vertexBuffer!!.put(squareCoords)
        // 设置缓冲区来读取第一个坐标
        vertexBuffer!!.position(0)

        // 初始化ByteBuffer，长度为arr数组的长度*2，因为一个short占2个字节
        val dlb = ByteBuffer.allocateDirect(drawOrder.size * 2)
        dlb.order(ByteOrder.nativeOrder())
        drawListBuffer = dlb.asShortBuffer()
        drawListBuffer!!.put(drawOrder)
        drawListBuffer!!.position(0)

    }
}
