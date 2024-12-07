package com.bangkit.dermascan.ui.main.forum

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
    val viewModel = ViewModelFactory.getInstance(context)
        .create(ForumViewModel::class.java)

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { _ ->
            val binding = ActivityForumBinding.inflate(LayoutInflater.from(context))
            val view = binding.root

            // Setup RecyclerView
            val adapter = ForumAdapter()
            binding.rvForums.adapter = adapter
            binding.rvForums.layoutManager = LinearLayoutManager(context)

            // Observe data changes and update the adapter
            viewModel.listForum.observe(context as LifecycleOwner) { forumResponse ->
                adapter.submitList(forumResponse)
            }

            // Observe loading state
            viewModel.isLoading.observe(context as LifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
            }

            // Fetch data
            viewModel.showForums()

            // Observe error message
            viewModel.errorMessage.observe(context as LifecycleOwner) { errorMessage ->
                if (errorMessage != null) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            if(roles != "PATIENT"){
                binding.fabAddForum.visibility = View.GONE
            }

            // FloatingActionButton click listener
            binding.fabAddForum.setOnClickListener {
                val intent = Intent(context, ForumAddActivity::class.java)
                context.startActivity(intent)
            }

            view
        },
        update = { _ ->
            // Refresh data when necessary
            viewModel.showForums()
        }
    )

}
