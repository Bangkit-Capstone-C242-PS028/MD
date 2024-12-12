package com.bangkit.dermascan.ui.articleDetail

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bangkit.dermascan.R
import com.bangkit.dermascan.databinding.ActivityArticleDetailBinding
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.article.ArticleViewModel
import com.bangkit.dermascan.ui.main.profile.settings.SettingsViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleDetailBinding
    private lateinit var settingsViewModel: SettingsViewModel
    private val viewModel by viewModels<ArticleDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var currentThemeMode: Int = AppCompatDelegate.getDefaultNightMode()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Inisialisasi SettingsViewModel
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        lifecycleScope.launch {
            val isDarkMode = settingsViewModel.isDarkTheme.first() // Ambil nilai awal
            val newThemeMode = if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }

            // Hanya lakukan perubahan jika tema berubah
            if (currentThemeMode != newThemeMode) {
                currentThemeMode = newThemeMode
                AppCompatDelegate.setDefaultNightMode(newThemeMode)
                recreate() // Buat ulang hanya jika ada perubahan tema
            }
        }


        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set warna background berdasarkan tema
        val backgroundColor = if (isDarkMode()) {
            ContextCompat.getColor(this, R.color.background)
        } else {
            ContextCompat.getColor(this, R.color.background)
        }
        binding.root.setBackgroundColor(backgroundColor)

        setSupportActionBar(binding.toolbarr)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val articleId = intent.getStringExtra(EXTRA_ARTICLE_ID)
        Log.e("ArticleDetailActivityID", "Article ID: $articleId")
        if (articleId != null) {
            viewModel.showArticleDetail(articleId)
            Log.e("ArticleDetailActivity", "Article ID: $articleId")
        }else {
            // Tampilkan pesan error atau finish activity
            Toast.makeText(this, "Article ID not found", Toast.LENGTH_SHORT).show()
            finish()
        }

        viewModel.article.observe(this) { articleResponse ->
            if (articleResponse != null) {
                binding.tvTitledetail.text = articleResponse.title
                binding.tvContextdetail.text = articleResponse.content

                Glide.with(binding.ivDetailPhoto.context)
                    .load(articleResponse.imageUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .into(binding.ivDetailPhoto)
            }

        }
        showLoading(false)
//        viewModel.isLoading.observe(this) {
//            showLoading(it)
//        }



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
                        " ${favoriteArticle.title} ${getString(R.string.favorite_deleted)}",
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
                            "${articleDetail.title} ${getString(R.string.favorite_added)}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    // Fungsi pembantu untuk mengecek mode gelap
    fun isDarkMode(): Boolean {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                finish()
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