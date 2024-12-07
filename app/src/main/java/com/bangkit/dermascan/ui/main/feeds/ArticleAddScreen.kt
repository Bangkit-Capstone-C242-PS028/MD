
package com.bangkit.dermascan.ui.main.feeds

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.bangkit.dermascan.R
import com.bangkit.dermascan.databinding.ActivityArticleAddBinding
import com.bangkit.dermascan.ui.articleAdd.ArticleAddViewModel
import com.bangkit.dermascan.util.getImageUri
import com.bangkit.dermascan.util.reduceFileImage
import com.bangkit.dermascan.util.uriToFile
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ArticleAddScreen(navController: NavController){
//    val navController = rememberNavController()
    Scaffold(
        content = {
            Box(modifier = Modifier
                .fillMaxSize()
            ) {
                ArticleAdd(navController)
            }
        }
    )
}



@OptIn(DelicateCoroutinesApi::class)
@Composable
fun ArticleAdd(navController: NavController) {
    val viewModel = hiltViewModel<ArticleAddViewModel>()
    val context = LocalContext.current


    // Inisialisasi launcher
    val launcherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        // Tangani hasil pemilihan gambar
        uri?.let {
            viewModel.setImageUri(it)
        }
    }

    // Inisialisasi launcher untuk menangkap foto menggunakan kamera
    val launcherIntentCamera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            viewModel.currentImageUri.value?.let {
                viewModel.setImageUri(it)
            }
        } else {
            // Handle error jika kamera gagal
            context.showToast("Camera failed")
        }
    }


    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { _ ->
            val binding = ActivityArticleAddBinding.inflate(LayoutInflater.from(context))
            val view = binding.root
            binding.previewImageView.setImageResource(R.drawable.ic_place_holder)

//            launcherGallery = viewModel.handleImageSelection(context)
//            launcherIntentCamera = viewModel.handleCameraCapture(context)

            // Listener untuk tombol gallery
            binding.galleryButton.setOnClickListener {
                launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            // Listener untuk tombol kamera
            binding.cameraButton.setOnClickListener {
                val imageUri = getImageUri(context)
                viewModel.setImageUri(imageUri)
                launcherIntentCamera.launch(imageUri)
            }

            // Listener untuk tombol upload
            binding.buttonAdd.setOnClickListener {
                if (viewModel.currentImageUri.value == null) {
                    Toast.makeText(context, "Choose image first", Toast.LENGTH_SHORT).show()
                } else {
                    GlobalScope.launch {
                        // Menjalankan fungsi suspend di luar siklus hidup
                        uploadArticle(binding, context, viewModel)
                    }
                }
            }

            // Observer untuk currentImageUri
            viewModel.currentImageUri.observe(context as LifecycleOwner) { uri ->
                if (uri != null) {
                    binding.previewImageView.setImageURI(uri)
                } else {
                    binding.previewImageView.setImageResource(R.drawable.ic_place_holder)
                }
            }

            // Observer untuk upload result
            viewModel.uploadResult.observe(context) { isSuccess ->
                binding.showLoading(false)
                if (isSuccess) {
//                    context.showToast(context.getString(R.string.article_uploaded))
//                    val intent = Intent(context, MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                    context.startActivity(intent)
                    navController.navigate("main")
                } else {
                    Log.d("ArticleAddActivity", viewModel.errorMessage.value.toString())
//                    viewModel.errorMessage.value?.let { }
                    context.showToast("Please fill all the fields")
                }
            }

            view
        }
    )
}

suspend fun uploadArticle(binding: ActivityArticleAddBinding, context: Context, viewModel: ArticleAddViewModel) {
    val imageUri = viewModel.currentImageUri.value ?: return
    val imageFile = uriToFile(imageUri, context).reduceFileImage()
    val title = binding.edAddTitle.editText?.text.toString()
    val content = binding.edAddContent.editText?.text.toString()

    val titleRequestBody = title.toRequestBody("text/plain".toMediaType())
    val contentRequestBody = content.toRequestBody("text/plain".toMediaType())
    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
    val multipartBody = MultipartBody.Part.createFormData(
        "image",
        imageFile.name,
        requestImageFile
    )

    withContext(Dispatchers.Main) {
        viewModel.createArticle(titleRequestBody, contentRequestBody, multipartBody)
    }
//    viewModel.createArticle(titleRequestBody, contentRequestBody, multipartBody)
}

fun ActivityArticleAddBinding.showImage(imageUri: Uri?) {
    imageUri?.let {
        previewImageView.setImageURI(it)
    } ?: run {
        Toast.makeText(root.context, "No image to display", Toast.LENGTH_SHORT).show()
    }
}

// Fungsi ekstensi untuk menampilkan loading
fun ActivityArticleAddBinding.showLoading(isLoading: Boolean) {
    progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
}

// Fungsi ekstensi untuk menampilkan toast
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


