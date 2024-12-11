package com.bangkit.dermascan.ui.forum

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dermascan.databinding.ActivityForumBinding
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.forumAdd.ForumAddActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ForumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForumBinding
    private val viewModel by viewModels<ForumViewModel> {
        ViewModelFactory.getInstance(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ForumAdapter()
        binding.rvForums.adapter = adapter
        binding.rvForums.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            viewModel.forumsPager
                .filterNotNull()
                .flatMapLatest { it }
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
        }
//        viewModel.listForum.observe(this) { forumResponse ->
//            adapter.submitList(forumResponse)
//        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.showForums()

        viewModel.errorMessage.observe(this) {
            if (it != null) Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        binding.fabAddForum.setOnClickListener {
            val intent = Intent(this, ForumAddActivity::class.java)
            startActivity(intent)
        }



    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}