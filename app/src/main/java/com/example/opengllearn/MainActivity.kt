package com.example.opengllearn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.opengllearn.opengl.CustomGlSurfaceView

class MainActivity : AppCompatActivity() {

    private lateinit var customGlSurfaceView: CustomGlSurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customGlSurfaceView = CustomGlSurfaceView(this)
        setContentView(customGlSurfaceView)
    }
}