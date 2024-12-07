package com.bangkit.dermascan.ui.forumAdd

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bangkit.dermascan.R
import com.bangkit.dermascan.databinding.ActivityForumAddBinding
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.forum.ForumActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class ForumAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForumAddBinding
    private val viewModel by viewModels<ForumAddViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupButtonListener()
    }

    private fun setupObservers() {
        viewModel.uploadResult.observe(this) { isSuccess ->
            showLoading(false)
            if (isSuccess) {
                showToast(getString(R.string.forum_created))
                navigateToForumList()
            } else {
                viewModel.errorMessage.value?.let { showToast(it) }
            }
        }
    }

    private fun setupButtonListener() {
        binding.buttonAdd.setOnClickListener {
            val title = binding.edAddTitle.editText?.text.toString()
            val content = binding.edAddContent.editText?.text.toString()

            viewModel.createForum(title, content)
        }
    }

    private fun navigateToForumList() {
        val intent = Intent(this, ForumActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}