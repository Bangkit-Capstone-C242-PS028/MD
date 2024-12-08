package com.bangkit.dermascan.ui.articleDetail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        setSupportActionBar(binding.toolbarr)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val articleId = intent.getStringExtra(EXTRA_ARTICLE_ID)

        if (articleId != null) {
            viewModel.showArticleDetail(articleId)
        }else {
            // Tampilkan pesan error atau finish activity
            Toast.makeText(this, "Article ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.article.observe(this) { articleResponse ->
            binding.tvTitledetail.text = articleResponse.title
            binding.tvContextdetail.text = articleResponse.content

            Glide.with(binding.ivDetailPhoto.context)
                .load(articleResponse.imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .into(binding.ivDetailPhoto)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.errorMessage.observe(this) {
            if (it != null) Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.favoriteArticle.observe(this) { favoriteArticle ->
            if (favoriteArticle != null) {
                // Article is already in favorites
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_active)
                binding.fabFavorite.setOnClickListener {
                    viewModel.deleteFavoriteArticle(favoriteArticle)
                    Toast.makeText(
                        this,
                        getString(R.string.favorite_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Article is not in favorites
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_unactive)
                binding.fabFavorite.setOnClickListener {
                    viewModel.article.value?.let { articleDetail ->
                        viewModel.insertFavoriteArticle(articleDetail)
                        Toast.makeText(
                            this,
                            getString(R.string.favorite_added),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
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