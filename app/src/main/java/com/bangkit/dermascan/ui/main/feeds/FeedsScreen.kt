package com.bangkit.dermascan.ui.main.feeds

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dermascan.R
import com.bangkit.dermascan.databinding.ActivityArticleBinding
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.article.ArticleAdapter
import com.bangkit.dermascan.ui.article.ArticleViewModel
import com.bangkit.dermascan.ui.articleAdd.ArticleAddActivity
import com.bangkit.dermascan.ui.authentication.AuthViewModel
import com.bangkit.dermascan.ui.theme.Blue
import com.bangkit.dermascan.ui.theme.Typography

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
//    val nav = rememberNavController()
    AndroidView(
        modifier = modifier,
        factory = { _ ->
            // Inflate layout XML yang sudah ada
            val binding = ActivityArticleBinding.inflate(LayoutInflater.from(context))
            val view = binding.root

            // Configurasi ViewModel


            // Setup RecyclerView
            val adapter = ArticleAdapter()
            binding.rvArticles.adapter = adapter
            binding.rvArticles.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


            // Observasi perubahan artikel
            viewModel.listArticle.observe(context as LifecycleOwner) { articleResponse ->
                adapter.submitList(articleResponse)
            }

            // Setup loading
            viewModel.isLoading.observe(context as LifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
            }

            // Memuat artikel
            viewModel.showArticles()

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