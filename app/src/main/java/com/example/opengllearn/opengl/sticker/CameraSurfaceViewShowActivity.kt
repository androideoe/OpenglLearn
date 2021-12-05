package com.example.opengllearn.opengl.sticker

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.PixelFormat
import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import com.example.opengllearn.R
import android.view.SurfaceHolder
import android.view.View
import android.widget.Button


class CameraSurfaceViewShowActivity : AppCompatActivity(), SurfaceHolder.Callback {
    private lateinit var surfaceView: SurfaceView;
    private lateinit var mHolder: SurfaceHolder
    private lateinit var mBtn: Button
    private var mCamera: Camera? = null
    private lateinit var mParameters: Camera.Parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surfaceview)
        surfaceView = findViewById(R.id.surface_view)
        mBtn = findViewById(R.id.btn_change)
        mHolder = surfaceView.holder;
        mHolder.addCallback(this)
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)


        mBtn.setOnClickListener {
            val valuesHolder: PropertyValuesHolder
            ? = PropertyValuesHolder.ofFloat("rotationY", 0.0f, 360.0f, 0.0F)
            val valuesHolder2: PropertyValuesHolder
            ? = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.5f, 1.0f);
            val valuesHolder3: PropertyValuesHolder
            ? = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.5f, 1.0f);

            val animator: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                surfaceView,
                valuesHolder, valuesHolder2, valuesHolder3
            )
            animator.duration = 500;
            animator.start()
        }

    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        try {
            mCamera = Camera.open(0)
            mCamera?.setDisplayOrientation(90)
            mCamera?.setPreviewDisplay(mHolder)
            mCamera?.startPreview()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
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


    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mCamera?.stopPreview();
        mCamera?.release();
        mCamera = null;
    }
}