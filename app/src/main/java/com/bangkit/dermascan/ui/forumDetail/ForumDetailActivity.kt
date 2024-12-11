package com.bangkit.dermascan.ui.forumDetail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dermascan.R
import com.bangkit.dermascan.databinding.ActivityForumDetailBinding
import com.bangkit.dermascan.ui.ViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ForumDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForumDetailBinding
    private var forumId : String ?= null
    private val viewModel by viewModels<ForumDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
//    private lateinit var forumRepliesAdapter: ForumRepliesAdapterPaging
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = ForumRepliesAdapterPaging()
        forumId = intent.getStringExtra("FORUM_ID") ?: return
        if (forumId.isNullOrBlank()) {
            Log.e("ForumDetailActivity", "Invalid Forum ID received!")
            Toast.makeText(this, "Error: Invalid Forum ID", Toast.LENGTH_SHORT).show()
            finish() // Tutup aktivitas jika ID tidak valid
            return
        } else {
            Log.d("ForumDetailActivity", "Forum ID received: $forumId")
        }

        setupObservers(adapter)
        setupReplySection(forumId!!)

        viewModel.showForumDetail(forumId!!)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun setupObservers(adapter : ForumRepliesAdapterPaging) {
        viewModel.forum.observe(this) { forum ->
            binding.tvTitleDetail.text = forum.title
            binding.tvContentDetail.text = forum.content
        }
        // Inisialisasi adapter
        binding.rvReplies.layoutManager = LinearLayoutManager(this)
        binding.rvReplies.adapter = adapter
//        lifecycleScope.launch {
//            if (forumId != null) {
//                viewModel.getForumReplies(forumId!!).collectLatest { pagingData ->
//                    Log.d("ForumDetailActivity", "Paging data received: $pagingData")
//                    adapter.submitData( pagingData)
//                }
//            }
//
//        }
        lifecycleScope.launch {
            viewModel.forumRepliesPager
                .filterNotNull()
                .flatMapLatest { it }
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }

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
