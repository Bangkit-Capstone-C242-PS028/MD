package com.bangkit.dermascan.ui.forumDetail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dermascan.R
import com.bangkit.dermascan.databinding.ActivityForumDetailBinding
import com.bangkit.dermascan.ui.ViewModelFactory

class ForumDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForumDetailBinding
    private val viewModel by viewModels<ForumDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get forum ID from Intent
//        val forumId = intent.getStringExtra("FORUM_ID") ?: return
        val forumId = intent.getStringExtra("FORUM_ID")
        if (forumId.isNullOrBlank()) {
            Log.e("ForumDetailActivity", "Invalid Forum ID received!")
            Toast.makeText(this, "Error: Invalid Forum ID", Toast.LENGTH_SHORT).show()
            finish() // Tutup aktivitas jika ID tidak valid
            return
        } else {
            Log.d("ForumDetailActivity", "Forum ID received: $forumId")
        }

        setupObservers()
        setupReplySection(forumId)

        // Load Forum Details and Replies
        viewModel.showForumDetail(forumId)
    }

    private fun setupObservers() {
        viewModel.forum.observe(this) { forum ->
            binding.tvTitleDetail.text = forum.title
            binding.tvContentDetail.text = forum.content
        }

        viewModel.replies.observe(this) { replies ->
            val adapter = ForumRepliesAdapter(replies)
            binding.rvReplies.adapter = adapter
            binding.rvReplies.layoutManager = LinearLayoutManager(this)
        }

        viewModel.isLoading.observe(this) { showLoading(it) }

        viewModel.errorMessage.observe(this) {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupReplySection(forumId: String) {
        binding.btnSendReply.setOnClickListener {
            val content = binding.etReplyInput.text.toString()
            if (content.isNotBlank()) {
                viewModel.sendForumReply(forumId, content)
                binding.etReplyInput.text.clear()
            } else {
                Toast.makeText(this, getString(R.string.error_empty_reply), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
