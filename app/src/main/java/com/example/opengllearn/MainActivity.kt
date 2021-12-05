package com.example.opengllearn

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.opengllearn.opengl.CustomGlSurfaceView
import com.example.opengllearn.opengl.sticker.CameraGlSurfaceViewShowActivity
import com.example.opengllearn.opengl.sticker.CameraSurfaceViewShowActivity
import com.example.opengllearn.opengl.sticker.CameraTextureViewShowActivity
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import androidx.annotation.NonNull


@RuntimePermissions
class MainActivity : AppCompatActivity(), View.OnClickListener {

    //    private lateinit var customGlSurfaceView: CustomGlSurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        customGlSurfaceView = CustomGlSurfaceView(this)
//        setContentView(customGlSurfaceView)
        findViewById<Button>(R.id.btn_gl_surface_show).setOnClickListener(this)
        findViewById<Button>(R.id.btn_surface_show).setOnClickListener(this)
        findViewById<Button>(R.id.btn_texture_show).setOnClickListener(this)
        requestCameraWithPermissionCheck()
    }


    @NeedsPermission(Manifest.permission.CAMERA)
    fun requestCamera() {
        Toast.makeText(this@MainActivity, "request camera success...", Toast.LENGTH_LONG).show()

    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun deniedCamera() {
        Toast.makeText(this@MainActivity, "confuse camera permissions...", Toast.LENGTH_LONG).show()
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btn_gl_surface_show -> startActivity(
                Intent(
                    this,
                    CameraGlSurfaceViewShowActivity::class.java
                )
            )

            R.id.btn_surface_show -> startActivity(
                Intent(
                    this,
                    CameraSurfaceViewShowActivity::class.java
                )
            )

            R.id.btn_texture_show -> startActivity(
                Intent(
                    this,
                    CameraTextureViewShowActivity::class.java
                )
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        this.onRequestPermissionsResult(requestCode, grantResults)
    }
}