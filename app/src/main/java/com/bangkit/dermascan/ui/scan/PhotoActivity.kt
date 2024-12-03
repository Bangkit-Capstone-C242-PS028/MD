//package com.bangkit.dermascan.ui.scan
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.media.MediaScannerConnection
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.util.Log
//import android.view.MotionEvent
//import android.view.ScaleGestureDetector
//import android.widget.ImageButton
//import android.widget.SeekBar
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.result.PickVisualMediaRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.camera.core.CameraControl
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.Preview
//import androidx.camera.core.ZoomState
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.LifecycleOwner
//import androidx.lifecycle.LiveData
//import com.bangkit.dermascan.databinding.ActivityPhotoBinding
//import com.google.common.util.concurrent.ListenableFuture
//import java.io.File
//import androidx.camera.view.PreviewView
//import com.bangkit.dermascan.R
//import com.bangkit.dermascan.ui.upload.UploadScreen.kt
//
//
//class PhotoActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityPhotoBinding
//    private lateinit var previewView: PreviewView
//    private lateinit var btnCapture: ImageButton
//    private lateinit var btnGallery: ImageButton
//    private lateinit var btnRotate: ImageButton
//    private lateinit var imageCapture: ImageCapture
//    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
//    private var isFrontCamera = false
//    private var isImageCaptureInitialized = false
//    private lateinit var cameraControl: CameraControl
//    private lateinit var zoomState: LiveData<ZoomState>
//    private lateinit var scaleGestureDetector: ScaleGestureDetector
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityPhotoBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        previewView =  binding.cameraPreview
//        btnCapture = binding.btnCapture
//        btnGallery = binding.btnGallery
//        btnRotate = binding.btnRotate
//
//
//        checkCameraPermission()
//
//        scaleGestureDetector = ScaleGestureDetector(this, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
//            override fun onScale(detector: ScaleGestureDetector): Boolean {
//                val scaleFactor = detector.scaleFactor
//                val newZoomRatio = Math.min(Math.max(zoomState.value!!.zoomRatio * scaleFactor, zoomState.value!!.minZoomRatio), zoomState.value!!.maxZoomRatio)
//                cameraControl.setZoomRatio(newZoomRatio)
//                return true
//            }
//        })
//
//        btnGallery.setOnClickListener { openGallery() }
//
//
//        btnCapture.setOnClickListener { capturePhoto() }
//
//
//        btnRotate.setOnClickListener { rotateCamera() }
//    }
//
//    private fun checkCameraPermission() {
//        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.CAMERA),
//                CAMERA_PERMISSION_REQUEST_CODE
//            )
//        } else {
//            cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//            cameraProviderFuture.addListener({
//                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//                startCamera(cameraProvider)
//            }, ContextCompat.getMainExecutor(this))
//            startCamera(cameraProviderFuture.get())
//        }
//    }
//
//    private fun startCamera(cameraProvider: ProcessCameraProvider) {
//        imageCapture = ImageCapture.Builder()
//            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//            .build()
//        isImageCaptureInitialized = true
//        try {
//
//            cameraProvider.unbindAll()
//
//            val cameraSelector = CameraSelector.Builder()
//                .requireLensFacing(if (isFrontCamera) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK)
//                .build()
//
//
//            val preview = Preview.Builder()
//                .build()
//                .also {
//                    it.surfaceProvider = previewView.surfaceProvider
//                }
//
//
//
//
//
//            val camera = cameraProvider.bindToLifecycle(
//                this as LifecycleOwner,
//                cameraSelector,
//                preview,
//                imageCapture
//            )
//
//
//            cameraControl = camera.cameraControl
//            zoomState = camera.cameraInfo.zoomState
//
//
//            zoomState.observe(this) { state ->
//
//                binding.zoomSeekBar.progress = ((state.zoomRatio - state.minZoomRatio) /
//                        (state.maxZoomRatio - state.minZoomRatio) * 100).toInt()
//            }
//
//
//            binding.zoomSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                    if (fromUser) {
//                        zoomState.value?.let { state ->
//                            val zoomRatio = state.minZoomRatio + (progress / 100f) *
//                                    (state.maxZoomRatio - state.minZoomRatio)
//                            cameraControl.setZoomRatio(zoomRatio)
//                        }
//                    }
//                }
//
//                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
//                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
//            })
//
//        } catch (exc: Exception) {
//            Log.e("CameraX", "Use case binding failed", exc)
//            Toast.makeText(
//                this,
//                "Failed to start camera: ${exc.message}",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//
//    private fun rotateCamera() {
//        isFrontCamera = !isFrontCamera
//        cameraProviderFuture.addListener({
//            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//            startCamera(cameraProvider)
//        }, ContextCompat.getMainExecutor(this))
//    }
//
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        scaleGestureDetector.onTouchEvent(event)
//        return super.onTouchEvent(event)
//    }
//
//    private fun capturePhoto() {
//        if (!isImageCaptureInitialized) {
//            showToast("Kamera belum diinisialisasi")
//            return
//        }
//
//        val photoFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "story_${System.currentTimeMillis()}.jpg")
//        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
//                    val savedUri = Uri.fromFile(photoFile)
//                    MediaScannerConnection.scanFile(
//                        this@PhotoActivity,
//                        arrayOf(photoFile.toString()),
//                        null
//                    ) { path, uri ->
//                        Log.d("PhotoCapture", "Scanned $path:")
//                        Log.d("PhotoCapture", "-> uri=$uri")
//                    }
//                    val intent = UploadScreen.kt.createIntent(this@PhotoActivity, savedUri)
//                    startActivity(intent)
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    exception.printStackTrace()
//                }
//            }
//        )
//    }
//
//    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 1001 && resultCode == RESULT_OK) {
//            val selectedImageUri = data?.data
//            if (selectedImageUri != null) {
//                val intent = UploadScreen.kt.createIntent(this, selectedImageUri)
//                startActivity(intent)
//            }
//        }
//    }
//
//    private fun openGallery() {
//        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//    }
//
//    private val launcherGallery = registerForActivityResult(
//        ActivityResultContracts.PickVisualMedia()
//    ) { uri: Uri? ->
//        uri?.let {
//            val intent = UploadScreen.kt.createIntent(this, it)
//            startActivity(intent)
//        } ?: showToast("No media selected")
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//                cameraProviderFuture.addListener({
//                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//                    startCamera(cameraProvider)
//                }, ContextCompat.getMainExecutor(this))
//                startCamera(cameraProviderFuture.get())
//            } else {
////                showToast(getString(R.string.need_permission_camera))
//            }
//        }
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putBoolean("isFrontCamera", isFrontCamera)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        isFrontCamera = savedInstanceState.getBoolean("isFrontCamera", false)
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }
//
//    companion object {
//        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
//    }
//
//}