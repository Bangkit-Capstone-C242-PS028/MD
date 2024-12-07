//package com.bangkit.dermascan.ui.main.feeds
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.LifecycleOwner
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.bangkit.dermascan.databinding.ActivityArticleBinding
//import com.bangkit.dermascan.ui.ViewModelFactory
//import com.bangkit.dermascan.ui.article.ArticleAdapter
//import com.bangkit.dermascan.ui.article.ArticleViewModel
//import com.bangkit.dermascan.ui.articleAdd.ArticleAddActivity
//import com.bangkit.dermascan.ui.authentication.AuthViewModel
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun FeedsScreen(navController: NavController, roles: String?) {
//    Scaffold(
//        content = {
//            Box(modifier = Modifier
//                .fillMaxSize()
//                ) {
//                if (roles != null) {
//                    ComposeArticleScreen(navController = navController, roles = roles)
//                }
//            }
//        }
//    )
//}
//
//
//@SuppressLint("InflateParams")
//@Composable
//fun ComposeArticleScreen(navController: NavController, roles: String) {
//    val context = LocalContext.current
//    val viewModel = ViewModelFactory.getInstance(context)
//        .create(ArticleViewModel::class.java)
////    val nav = rememberNavController()
//    AndroidView(
//        modifier = Modifier.fillMaxSize(),
//        factory = { _ ->
//            // Inflate layout XML yang sudah ada
//            val binding = ActivityArticleBinding.inflate(LayoutInflater.from(context))
//            val view = binding.root
//
//            // Configurasi ViewModel
//
//
//            // Setup RecyclerView
//            val adapter = ArticleAdapter()
//            binding.rvArticles.adapter = adapter
//            binding.rvArticles.layoutManager = LinearLayoutManager(context)
//
//            // Observasi perubahan artikel
//            viewModel.listArticle.observe(context as LifecycleOwner) { articleResponse ->
//                adapter.submitList(articleResponse)
//            }
//
//            // Setup loading
//            viewModel.isLoading.observe(context as LifecycleOwner) { isLoading ->
//                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
//            }
//
//            // Memuat artikel
//            viewModel.showArticles()
//
//            if (roles != "DOCTOR") {
//                binding.fabAddarticle.visibility = View.GONE
//            }
//            // Setup FloatingActionButton
//            binding.fabAddarticle.setOnClickListener {
////                val intent = Intent(context, ArticleAddActivity::class.java)
////                context.startActivity(intent)
//                navController.navigate("articleAdd")
//            }
//
//            view
//        },
//        update = { _ ->
//            // Misalnya, me-refresh data artikel
//            viewModel.showArticles()
//
//            // Atau melakukan operasi update lainnya sesuai kebutuhan
//        }
//
//    )
//}