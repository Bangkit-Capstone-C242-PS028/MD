package com.bangkit.dermascan.ui.articleFavorite

import android.annotation.SuppressLint
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dermascan.R
import com.bangkit.dermascan.data.model.response.ArticleItem
import com.bangkit.dermascan.databinding.FragmentFavoriteBinding
import com.bangkit.dermascan.ui.article.ArticleAdapter
import com.bangkit.dermascan.ui.favorite.FavoriteArticleViewModelFactory
import com.bangkit.dermascan.ui.theme.Black
import com.bangkit.dermascan.ui.theme.Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteArticleScreen(navController: NavController) {
    val context = LocalContext.current
    val favoriteViewModel: FavoriteArticleViewModel = viewModel(
        factory = FavoriteArticleViewModelFactory(context.applicationContext as Application)
    )

    val favoriteArticles by favoriteViewModel.getFavoriteArticles().observeAsState(emptyList())

    // Menampilkan TopAppBar di bagian atas
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Article") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
                , colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = colorResource(id = R.color.blue),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )

        },
        content = {  paddingValues ->
            // Gunakan AndroidView untuk menampilkan RecyclerView
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(start = 16.dp, end = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                // Gunakan AndroidView untuk menampilkan RecyclerView
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    factory = { ctx ->
                        // Membuat binding dan RecyclerView
                        val binding = FragmentFavoriteBinding.inflate(LayoutInflater.from(ctx))
                        val view = binding.root
                        val recyclerView = binding.rvListArticle
                        recyclerView.layoutManager = LinearLayoutManager(ctx)

                        // Adapter
                        val adapter = ArticleAdapter()
                        recyclerView.adapter = adapter

                        favoriteViewModel.getFavoriteArticles().observe(ctx as LifecycleOwner) { favoriteArticles ->
                            if (favoriteArticles.isNullOrEmpty()) {
                                // Show empty state message and hide RecyclerView
                                binding.tvEmptyState.visibility = View.VISIBLE
                                binding.rvListArticle.visibility = View.GONE
                            } else {
                                // Show RecyclerView and hide empty state message
                                binding.tvEmptyState.visibility = View.GONE
                                binding.rvListArticle.visibility = View.VISIBLE

                                // Convert FavoriteArticle to ArticleItem for adapter
                                val articleItems = favoriteArticles.map { favoriteArticle ->
                                    ArticleItem(
                                        id = favoriteArticle.id,
                                        title = favoriteArticle.title,
                                        content = favoriteArticle.content,
                                        imageUrl = favoriteArticle.imageUrl,
                                        createdAt = favoriteArticle.createdAt,
                                        updatedAt = favoriteArticle.updatedAt,
                                        name = favoriteArticle.name,
                                        avatar = favoriteArticle.avatar
                                    )
                                }
                                adapter.submitList(articleItems)
                            }
                        }

                        view
                    },

                )
            }
        },

    )
}

