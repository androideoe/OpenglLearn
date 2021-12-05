package com.example.opengllearn.opengl.sticker

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.PixelFormat
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.TextureView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.opengllearn.R


class CameraTextureViewShowActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {
    private lateinit var mCameraTextureView: TextureView
    private lateinit var mBtn: Button
    private var mCamera: Camera? = null
    private lateinit var mParameters: Camera.Parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_textureview)
        mCameraTextureView = findViewById(R.id.texture_view)
        mBtn = findViewById(R.id.btn_change)
        mCameraTextureView.setSurfaceTextureListener(this)


        mBtn.setOnClickListener {
            val valuesHolder: PropertyValuesHolder
            ? = PropertyValuesHolder.ofFloat("rotationY", 0.0f, 360.0f, 0.0F)
            val valuesHolder2: PropertyValuesHolder
            ? = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.5f, 1.0f);
            val valuesHolder3: PropertyValuesHolder
            ? = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.5f, 1.0f);
            val valuesHolder4: PropertyValuesHolder
            ? = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.7f, 1.0F);

            val animator: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                mCameraTextureView,
                valuesHolder, valuesHolder2, valuesHolder3,valuesHolder4
            )
            animator.duration = 500;
            animator.start()
        }

    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        try {
            mCamera = Camera.open(0)
            mCamera?.setDisplayOrientation(90)
            mCamera?.setPreviewTexture(mCameraTextureView.surfaceTexture)
            mCamera?.startPreview()

            mCamera?.autoFocus(object : Camera.AutoFocusCallback {
                override fun onAutoFocus(success: Boolean, camera: Camera?) {
                    if (success) {
                        mParameters = mCamera?.getParameters()!!;
                        mParameters.setPictureFormat(PixelFormat.JPEG); //图片输出格式
//                    mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//预览持续发光
                        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//持续对焦模式
                        mCamera?.setParameters(mParameters);
                        mCamera?.startPreview();
                        mCamera?.cancelAutoFocus();
                    }
                }

            })
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        mCamera?.stopPreview();
        mCamera?.release();
        mCamera = null;

        return false
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }
}