package com.bangkit.dermascan.ui.articleAdd

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.dermascan.R
import com.bangkit.dermascan.databinding.ActivityArticleAddBinding
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.article.ArticleActivity
import com.bangkit.dermascan.ui.article.ArticleViewModel
import com.bangkit.dermascan.util.getImageUri
import com.bangkit.dermascan.util.reduceFileImage
import com.bangkit.dermascan.util.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ArticleAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleAddBinding
    private val viewModel by viewModels<ArticleAddViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.currentImageUri.observe(this) { uri ->
            uri?.let {
                binding.previewImageView.setImageURI(it)
            }
        }

        viewModel.uploadResult.observe(this) { isSuccess ->
            showLoading(false)
            if (isSuccess) {
                showToast(getString(R.string.article_uploaded))
                val intent = Intent(this, ArticleActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                viewModel.errorMessage.value?.let { showToast(it) }
            }
        }

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.cameraButton.setOnClickListener {
            startCamera()
        }

        binding.buttonAdd.setOnClickListener {
            if (viewModel.currentImageUri.value == null) {
                showToast("Choose image first")
            } else {
                lifecycleScope.launch {
                    uploadArticle()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.setImageUri(uri)
            showImage()
        } else {
            Log.d("ArticleAddActivity", "Image uri is null")
        }
    }

    private fun startCamera() {
        viewModel.setImageUri(getImageUri(this))
        launcherIntentCamera.launch(viewModel.currentImageUri.value!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            viewModel.setImageUri(null)
        }
    }

    private suspend fun uploadArticle() {
        val imageFile = uriToFile(viewModel.currentImageUri.value!!, this).reduceFileImage()
        val title = binding.edAddTitle.editText?.text.toString()
        val content = binding.edAddContent.editText?.text.toString()
        showLoading(true)

        val titleRequestBody = title.toRequestBody("text/plain".toMediaType())
        val contentRequestBody = content.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )

        viewModel.createArticle(titleRequestBody, contentRequestBody, multipartBody)
    }

    private fun showImage() {
        viewModel.currentImageUri.value?.let { uri ->
            binding.previewImageView.setImageURI(uri)
        } ?: showToast("No image to display")
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}