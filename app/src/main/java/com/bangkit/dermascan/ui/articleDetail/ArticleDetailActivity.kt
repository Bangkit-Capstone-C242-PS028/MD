package com.bangkit.dermascan.ui.articleDetail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.dermascan.R
import com.bangkit.dermascan.databinding.ActivityArticleDetailBinding
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.article.ArticleViewModel
import com.bumptech.glide.Glide

class ArticleDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleDetailBinding

    private val viewModel by viewModels<ArticleDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val articleId = intent.getStringExtra(EXTRA_ARTICLE_ID)

        if (articleId != null) {
            viewModel.showArticleDetail(articleId)
        }

        viewModel.article.observe(this) { articleResponse ->
            binding.tvTitledetail.text = articleResponse.title
            binding.tvContextdetail.text = articleResponse.content

            Glide.with(binding.ivDetailPhoto.context)
                .load(articleResponse.photoUrl)
                .placeholder(R.drawable.img_placeholder)
                .into(binding.ivDetailPhoto)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.errorMessage.observe(this) {
            if (it != null) Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        const val EXTRA_ARTICLE_ID = "extra_article_id"
    }
}