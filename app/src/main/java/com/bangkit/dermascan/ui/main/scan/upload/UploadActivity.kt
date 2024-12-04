package com.bangkit.dermascan.ui.main.scan.upload

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.dermascan.databinding.ActivityUploadBinding
import com.bangkit.dermascan.ui.main.MainActivity

//import com.example.storyapp.R
//import com.example.storyapp.databinding.ActivityUploadBinding
//import com.example.storyapp.utils.ResultStories
//import com.example.storyapp.utils.reduceFileImage
//import com.example.storyapp.utils.uriToFile
//import com.example.storyapp.view.ui.ViewModelFactory
//import com.example.storyapp.view.ui.main.MainActivity
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
//    private val viewModel by viewModels<SkinLesionViewModel>{
//        ViewModelFactory.getInstance(this)
//    }

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("UploadScreen.kt", "onCreate called uri : ${intent.getStringExtra(EXTRA_IMAGE_URI)}")
        currentImageUri = Uri.parse((intent.getStringExtra(EXTRA_IMAGE_URI)))

        binding.ivContent.setImageURI(currentImageUri)

        setupAction()
    }

    private fun uploadImage(lan:Float?,lon:Float?) {
        currentImageUri?.let {

//            val imageFile = uriToFile(it, this).reduceFileImage()


        }
    }

    private fun setupAction(){
        binding.btnUpload.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            finish()
            startActivity(intent)

        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



    companion object {
        private const val EXTRA_IMAGE_URI = "imageUri"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
                fun createIntent(context: Context, imageUri: Uri): Intent {
            return Intent(context, UploadActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_URI, imageUri.toString())
            }
        }
    }
}