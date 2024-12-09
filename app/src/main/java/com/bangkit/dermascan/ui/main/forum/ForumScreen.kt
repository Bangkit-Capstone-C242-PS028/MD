package com.bangkit.dermascan.ui.main.forum

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dermascan.databinding.ActivityForumBinding
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.article.ArticleViewModel
import com.bangkit.dermascan.ui.forum.ForumAdapter
import com.bangkit.dermascan.ui.forum.ForumViewModel
import com.bangkit.dermascan.ui.forumAdd.ForumAddActivity
import com.bangkit.dermascan.ui.main.feeds.ComposeArticleScreen

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

@Composable
fun ComposeForumScreen(roles: String) {
    val context = LocalContext.current
    val viewModel = ViewModelFactory.getInstance(context).create(ForumViewModel::class.java)

    // Observe data changes, loading state, and error messages in Compose
    val forumResponse by viewModel.listForum.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState(null)

    // Show error toast if error message exists
    errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }

    // Handle the visibility of the floating action button based on roles
    val fabVisibility = if (roles != "PATIENT") View.GONE else View.VISIBLE
    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Panggil fungsi untuk memperbarui data
            viewModel.showForums()
        }
    }
    // Use AndroidView to display RecyclerView, but let Compose handle the data and UI updates
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            val binding = ActivityForumBinding.inflate(LayoutInflater.from(ctx))
            val view = binding.root

            // Setup RecyclerView and adapter
            val adapter = ForumAdapter()
            binding.rvForums.adapter = adapter
            binding.rvForums.layoutManager = LinearLayoutManager(ctx)


            // FloatingActionButton click listener
            binding.fabAddForum.apply {
                visibility = fabVisibility
                setOnClickListener {
                    val intent = Intent(ctx, ForumAddActivity::class.java)
                    activityResultLauncher.launch(intent)
                }
            }

            // Show the RecyclerView and loading progress bar based on state
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.INVISIBLE
            }

            view
        },
        update = { view ->
            // Update RecyclerView when forumResponse data changes
            val binding = ActivityForumBinding.bind(view)
            val adapter = binding.rvForums.adapter as ForumAdapter
            adapter.submitList(forumResponse)

            // Fetch data if it's necessary, for example when the screen is first loaded
            viewModel.showForums()
        }
    )

}

