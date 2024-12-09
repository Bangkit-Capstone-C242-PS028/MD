package com.bangkit.dermascan.ui.articleFavorite

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dermascan.data.model.response.ArticleItem
import com.bangkit.dermascan.databinding.FragmentFavoriteBinding
import com.bangkit.dermascan.ui.article.ArticleAdapter
import com.bangkit.dermascan.ui.favorite.FavoriteArticleViewModelFactory

@Composable
fun FavoriteArticleScreen() {
    val context = LocalContext.current
    val favoriteViewModel: FavoriteArticleViewModel = viewModel(
        factory = FavoriteArticleViewModelFactory(context.applicationContext as Application)
    )

    val favoriteArticles by favoriteViewModel.getFavoriteArticles().observeAsState(emptyList())

    // Gunakan AndroidView untuk menampilkan RecyclerView
    AndroidView(
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
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
    )
}
