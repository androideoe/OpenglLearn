package com.example.opengllearn.opengl

import java.nio.*

class DataUtils {
    private fun intBufferUtil(arr: IntArray): IntBuffer {
        val mBuffer: IntBuffer
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个int占4个字节
        val qbb = ByteBuffer.allocateDirect(arr.size * 4)
        // 数组排列用nativeOrder
        qbb.order(ByteOrder.nativeOrder())
        mBuffer = qbb.asIntBuffer()
        mBuffer.put(arr)
        mBuffer.position(0)
        return mBuffer
    }

    private fun floatBufferUtil(arr: FloatArray): FloatBuffer {
        val mBuffer: FloatBuffer
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个int占4个字节
        val qbb = ByteBuffer.allocateDirect(arr.size * 4)
        // 数组排列用nativeOrder
        qbb.order(ByteOrder.nativeOrder())
        mBuffer = qbb.asFloatBuffer()
        mBuffer.put(arr)
        mBuffer.position(0)
        return mBuffer
    }

    private fun shortBufferUtil(arr: ShortArray): ShortBuffer {
        val mBuffer: ShortBuffer
        // 初始化ByteBuffer，长度为arr数组的长度*2，因为一个short占2个字节
        val qbb = ByteBuffer.allocateDirect(arr.size * 2)
        qbb.order(ByteOrder.nativeOrder())
        mBuffer = qbb.asShortBuffer()
        mBuffer.put(arr)
        mBuffer.position(0)
        return mBuffer
    }
}