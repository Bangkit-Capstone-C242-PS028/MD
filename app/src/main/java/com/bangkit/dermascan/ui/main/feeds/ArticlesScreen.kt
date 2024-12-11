@file:Suppress("DEPRECATION")

package com.bangkit.dermascan.ui.main.feeds

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dermascan.databinding.ActivityArticleBinding
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.article.ArticleAdapter
import com.bangkit.dermascan.ui.article.ArticlePagingAdapter
import com.bangkit.dermascan.ui.article.ArticleViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedsScreen(navController: NavController, roles: String?) {
    Scaffold(
        topBar = {
            AddTopBar("Article",navController)
        },
        content = {
            Box(modifier = Modifier
                .fillMaxSize()

                .padding(top = 120.dp)
            ) {
                if (roles != null) {
                    ComposeArticleScreen(navController = navController, roles = roles, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    )

}


@SuppressLint("InflateParams")
@Composable
fun ComposeArticleScreen(navController: NavController, roles: String, modifier: Modifier) {
    val context = LocalContext.current
    val viewModel = ViewModelFactory.getInstance(context)
        .create(ArticleViewModel::class.java)
    val lifecycleOwner = LocalLifecycleOwner.current
//    val nav = rememberNavController()
    AndroidView(
        modifier = modifier,
        factory = { _ ->
            // Inflate layout XML yang sudah ada
            val binding = ActivityArticleBinding.inflate(LayoutInflater.from(context))
            val view = binding.root

            // Configurasi ViewModel


            // Setup RecyclerView
            val adapter = ArticlePagingAdapter()
            binding.rvArticles.adapter = adapter
            binding.rvArticles.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            adapter.addLoadStateListener { loadState ->
                binding.progressBar.visibility = if (loadState.refresh is LoadState.Loading) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            // Observasi perubahan artikel
//            viewModel.listArticle.observe(context as LifecycleOwner) { articleResponse ->
//                adapter.submitList(articleResponse)
//            }
            lifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.articlePager.collect {pagingData ->
                    adapter.submitData(pagingData)
                }
            }


            if (roles != "DOCTOR") {
                binding.fabAddarticle.visibility = View.GONE
            }
            // Setup FloatingActionButton
            binding.fabAddarticle.setOnClickListener {
//                val intent = Intent(context, ArticleAddActivity::class.java)
//                context.startActivity(intent)
                navController.navigate("articleAdd")
            }

            view
        },
        update = { _ ->
            // Misalnya, me-refresh data artikel
            viewModel.showArticles()

            // Atau melakukan operasi update lainnya sesuai kebutuhan
        }

    )
}