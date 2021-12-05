package com.example.opengllearn.opengl.utils

import android.content.Context
import java.io.IOException
import java.io.InputStream

class AssetsUtils {

    companion object {

        fun read(context: Context, fileName: String): String? {
            var result: String? = null
            try {
                val inputStream: InputStream =
                    context.getResources().getAssets().open("Shader/$fileName")
                val length: Int = inputStream.available()
                val buffer = ByteArray(length)
                inputStream.read(buffer)
                result = String(buffer, Charsets.UTF_8)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return result
        }

    }

}