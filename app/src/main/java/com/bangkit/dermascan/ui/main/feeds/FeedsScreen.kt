package com.bangkit.dermascan.ui.main.feeds

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.bangkit.dermascan.R
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.article.ArticleViewModel

@Composable
fun FeedsScreen(){
    Text(text = "Feeds Screen")
    ComposeArticleScreen()
}


@SuppressLint("InflateParams")
@Composable
fun ComposeArticleScreen() {
    AndroidView(
        factory = { context ->
            val view = LayoutInflater.from(context)
                .inflate(R.layout.activity_article, null, false)

            // Optional: Additional setup if needed
            val viewModel = ViewModelFactory.getInstance(context)
                .create(ArticleViewModel::class.java)
            viewModel.showArticles()

            view
        }
    )
}