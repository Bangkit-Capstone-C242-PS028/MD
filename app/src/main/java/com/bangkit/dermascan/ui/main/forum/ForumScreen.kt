@file:Suppress("DEPRECATION")

package com.bangkit.dermascan.ui.main.forum

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dermascan.databinding.ActivityForumBinding
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.article.ArticleViewModel
import com.bangkit.dermascan.ui.forum.ForumAdapter
import com.bangkit.dermascan.ui.forum.ForumViewModel
import com.bangkit.dermascan.ui.forumAdd.ForumAddActivity
import com.bangkit.dermascan.ui.main.feeds.ComposeArticleScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ForumScreen(roles: String){
    Scaffold(

        content = {


            Box(modifier = Modifier
                .fillMaxSize()
            ) {
//                if (roles != null) {
//                    ComposeArticleScreen(navController = navController, roles = roles)
//                }
                ComposeForumScreen(roles = roles)

            }
        }
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun ComposeForumScreen(roles: String) {
    val context = LocalContext.current
    val viewModel = ViewModelFactory.getInstance(context).create(ForumViewModel::class.java)


    val errorMessage by viewModel.errorMessage.observeAsState(null)
    val lifecycleOwner = LocalLifecycleOwner.current
    val adapter = ForumAdapter()

    // Show error toast if error message exists
    errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }

    // Handle the visibility of the floating action button based on roles
    val fabVisibility = if (roles != "PATIENT") View.GONE else View.VISIBLE
    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("ForumScreen", result.resultCode.toString())
            // Panggil fungsi untuk memperbarui data
//            viewModel.refreshForums()
            lifecycleOwner.lifecycleScope.launch {
                // Menggunakan collectLatest untuk mengumpulkan PagingData yang diterbitkan oleh Flow
                viewModel.forumsPager
                    .filterNotNull()
                    .flatMapLatest { it }
                    .collectLatest {
                        pagingData -> adapter.submitData(pagingData)
                    }
            }
        }
    }
    // Use AndroidView to display RecyclerView, but let Compose handle the data and UI updates
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            val binding = ActivityForumBinding.inflate(LayoutInflater.from(context))
            val view = binding.root

            // Setup RecyclerView and adapter

            binding.rvForums.adapter = adapter
            binding.rvForums.layoutManager = LinearLayoutManager(context)

            binding.swipeRefreshLayout.setOnRefreshListener {
                adapter.refresh()
            }
            adapter.addLoadStateListener { loadState ->
                binding.swipeRefreshLayout.isRefreshing = loadState.refresh is LoadState.Loading
                if (loadState.refresh is LoadState.NotLoading) {
                    binding.rvForums.scrollToPosition(0)
                }
            }

            lifecycleOwner.lifecycleScope.launch {
                // Menggunakan collectLatest untuk mengumpulkan PagingData yang diterbitkan oleh Flow
                viewModel.forumsPager
                    .filterNotNull()
                    .flatMapLatest { it }
                    .collectLatest { pagingData ->
                        adapter.submitData(pagingData)
                    }
            }

            // FloatingActionButton click listener
            binding.fabAddForum.apply {
                visibility = fabVisibility
                setOnClickListener {
                    val intent = Intent(context, ForumAddActivity::class.java)
                    activityResultLauncher.launch(intent)
                }
            }


            view
        },
        update = {

            lifecycleOwner.lifecycleScope.launch {
                // Menggunakan collectLatest untuk mengumpulkan PagingData yang diterbitkan oleh Flow
                viewModel.forumsPager
                    .filterNotNull()
                    .flatMapLatest { it }
                    .collectLatest { pagingData ->
                        adapter.submitData(pagingData)
                    }
            }

        }
    )

}

