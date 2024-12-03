package com.bangkit.dermascan.ui.scan

//import androidx.compose.ui.platform.LocalLifecycleOwner
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.bangkit.dermascan.R
import com.bangkit.dermascan.ui.theme.Blue
import com.bangkit.dermascan.ui.theme.LightBlue
import com.bangkit.dermascan.ui.upload.SharedViewModel
import java.io.File
import java.io.FileInputStream

@Composable
fun ScanScreen(
    viewModel: SharedViewModel,
    onBackClick: () -> Unit,
    onUploadClick: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // State untuk kontrol kamera
    var isFrontCamera by rememberSaveable { mutableStateOf(false) }
    var imageCapture by remember {
        mutableStateOf(
            ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setTargetResolution(Size(1920, 1080))
                .build()
        )
    }

    // Inisialisasi PreviewView di luar composable untuk memastikannya tersedia
    val previewView = remember { PreviewView(context) }
    var zoomLevel by rememberSaveable { mutableFloatStateOf(0f) }
    var isMultiTouch by remember { mutableStateOf(false) }
    // State untuk menyimpan preview
    var preview by remember { mutableStateOf<Preview?>(null) }

    // Launcher untuk memilih foto dari galeri
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            Log.d("ScanScreen.kt", "onCreate called uri : $selectedUri")
            viewModel.setImageUri(selectedUri)
            onUploadClick()
        }
    }

    // Fungsi untuk memeriksa izin kamera
    fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        context, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    // Launcher untuk meminta izin kamera
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera(context, lifecycleOwner, isFrontCamera, previewView) { captureInstance, previewInstance ->
                imageCapture = captureInstance
                preview = previewInstance
                preview!!.setSurfaceProvider(previewView.surfaceProvider)
            }
        }
    }

    // Memulai kamera saat komponen dimuat
    LaunchedEffect(isFrontCamera) {
        if (hasCameraPermission()) {
            startCamera(context, lifecycleOwner, isFrontCamera, previewView) { captureInstance, previewInstance ->
                imageCapture = captureInstance
                preview = previewInstance
                preview!!.setSurfaceProvider(previewView.surfaceProvider)
            }
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTransformGestures(
                // Hanya proses zoom jika lebih dari satu jari
                panZoomLock = true,
                onGesture = { _, _, gestureZoom, _ ->
                    // Pastikan hanya zoom yang dilakukan dengan dua jari
                    if (gestureZoom != 0f) {
                        isMultiTouch = true

                        // Hitung zoom ratio baru
                        val newZoomRatio = (zoomLevel * gestureZoom).coerceIn(0.1f, 1f)

                        // Update zoom
                        zoomLevel = newZoomRatio

                        // Terapkan zoom ke kamera
                        updateZoom(
                            zoomLevel,
                            context,
                            lifecycleOwner,
                            isFrontCamera,

                        )
                    } else {
                        isMultiTouch = false
                    }
                }
            )
        }
    ) {
        // Tampilan pratinjau kamera
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        // Tombol-tombol kontrol
        CameraControls(
            zoomLevel = zoomLevel,
            onZoomChange = { newZoomLevel ->
                zoomLevel = newZoomLevel
                updateZoom(newZoomLevel, context, lifecycleOwner, isFrontCamera)
            },
            onCaptureClick = {
                capturePhoto(context, imageCapture) { uri ->
                    viewModel.setImageUri(uri)
                    onUploadClick()
                }
            },
            onGalleryClick = {
                galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onRotateCameraClick = {
                isFrontCamera = !isFrontCamera // Balikkan status kamera
            },
            onBackClick = onBackClick
        )
    }
}

@Composable
fun CameraControls(
    zoomLevel: Float,
    onZoomChange: (Float) -> Unit,
    onCaptureClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onRotateCameraClick: () -> Unit,
    onBackClick: () -> Unit
) {
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val context = LocalContext.current

    // Root Constraint Layout equivalent in Compose
    Box(modifier = Modifier.fillMaxSize()) {

        // Back button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .offset(y = 12.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // Define zoomLevel state


        // SeekBar replacement (Composable Slider)
        Slider(
            value = zoomLevel,
            onValueChange = { newZoomLevel ->
                onZoomChange(newZoomLevel)
            },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = Blue,
                activeTrackColor = LightBlue,
                inactiveTrackColor = Color.LightGray,
                activeTickColor = Color.Yellow
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
                .width(200.dp)
        )

        // Control buttons row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .offset(y = (-15).dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Gallery Icon
            IconButton(onClick = onGalleryClick, modifier = Modifier.size(80.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_gallery),
                    contentDescription = "Open Gallery",
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
            }

            // Capture Icon
            IconButton(onClick = onCaptureClick, modifier = Modifier.size(80.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_capt),
                    contentDescription = "Capture Photo",
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
            }

            // Rotate Icon
            IconButton(onClick = onRotateCameraClick, modifier = Modifier.size(80.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_rotate),
                    contentDescription = "Rotate Camera",
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                )
            }
        }
    }
}

private fun updateZoom(level: Float, context: Context, lifecycleOwner: LifecycleOwner, isFrontCamera: Boolean) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    try {
        val cameraProvider = cameraProviderFuture.get()
        val cameraSelector = if (isFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        val camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector)

        // Ensure that camera is available
        val cameraControl = camera.cameraControl
        val zoomRatio = 1.0f + (level * 9.0f)  // Map 0-1 slider to 1.0-10.0 zoom range
        cameraControl.setZoomRatio(zoomRatio.coerceIn(1.0f, 10.0f))
    } catch (e: Exception) {
        Log.e("CameraX", "Zoom update failed: ${e.message}", e)
    }
}

private fun startCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    isFrontCamera: Boolean,
    previewView: PreviewView,
    onCameraInitialized: (ImageCapture, Preview) -> Unit
) {
    val cameraSelector = if (isFrontCamera) {
        CameraSelector.DEFAULT_FRONT_CAMERA
    } else {
        CameraSelector.DEFAULT_BACK_CAMERA
    }

    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    cameraProviderFuture.addListener({
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        // Membangun Preview
        val preview = Preview.Builder().build()
        preview.setSurfaceProvider(previewView.surfaceProvider)

        val imageCapture = ImageCapture.Builder().build()

        // Mengikat lifecycle kamera
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
            onCameraInitialized(imageCapture, preview)
        } catch (exc: Exception) {
            Log.e("CameraX", "Gagal mengikat kamera: ${exc.message}")
        }

    }, ContextCompat.getMainExecutor(context))
}

private fun capturePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onPhotoCaptured: (Uri) -> Unit
) {
    // Pastikan direktori untuk menyimpan foto ada
    val outputDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        ?: context.filesDir // Fallback ke internal storage jika external storage tidak tersedia

    // Buat file untuk menyimpan foto
    val photoFile = File(
        outputDirectory,
        "dermascan_${System.currentTimeMillis()}.jpg"
    )

    // Pastikan direktori bisa dibuat
    photoFile.parentFile?.mkdirs()

    // Konfigurasi opsi keluaran dengan metadata tambahan
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
        .setMetadata(
            ImageCapture.Metadata().apply {
                // Sesuaikan dengan kebutuhan rotasi
                isReversedHorizontal = false
            }
        )
        .build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)

                // Verifikasi file benar-benar ada
                if (photoFile.exists()) {
                    Log.d("CameraX", "Photo saved successfully: $savedUri")
                    Toast.makeText(context, "Photo saved successfully: $savedUri", Toast.LENGTH_SHORT).show()

                    // Gunakan MediaStore untuk memastikan foto terlihat di galeri
                    savePhotoToMediaStore(context, photoFile)

                    onPhotoCaptured(savedUri)
                } else {
                    Log.e("CameraX", "Photo file does not exist")
                }
            }

            override fun onError(exc: ImageCaptureException) {
                Log.e("CameraX", "Photo capture failed: ${exc.message}", exc)

                // Detail error handling
                val errorMsg = when (exc.imageCaptureError) {
                    ImageCapture.ERROR_UNKNOWN -> "Unknown error"
                    ImageCapture.ERROR_FILE_IO -> "File I/O error"
                    ImageCapture.ERROR_CAMERA_CLOSED -> "Camera closed"
                    ImageCapture.ERROR_CAPTURE_FAILED -> "Capture failed"
                    ImageCapture.ERROR_INVALID_CAMERA -> "Invalid camera"
                    else -> "Unexpected error"
                }

                Log.e("CameraX", "Capture error: $errorMsg")
            }
        }
    )
}

private fun savePhotoToMediaStore(context: Context, photoFile: File) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Untuk Android 10 (Q) dan di atasnya
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, photoFile.name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val contentResolver = context.contentResolver
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            try {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    FileInputStream(photoFile).use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            } catch (e: Exception) {
                Log.e("CameraX", "Error saving to MediaStore", e)
            }
        }
    } else {
        // Untuk versi Android sebelum 10
        MediaScannerConnection.scanFile(
            context,
            arrayOf(photoFile.absolutePath),
            arrayOf("image/jpeg")
        ) { path, uri ->
            Log.d("CameraX", "Scanned $path: $uri")
        }
    }
}