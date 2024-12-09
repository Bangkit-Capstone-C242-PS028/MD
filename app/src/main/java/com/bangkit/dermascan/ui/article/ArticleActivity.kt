package com.bangkit.dermascan.ui.article

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dermascan.R
import com.bangkit.dermascan.databinding.ActivityArticleBinding
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.articleAdd.ArticleAddActivity
import com.bangkit.dermascan.ui.authentication.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class ArticleActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var binding: ActivityArticleBinding
    private val viewModel by viewModels<ArticleViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val authViewModel: AuthViewModel by viewModels()
//    val roles by authViewModel.roles.observeAsState("PATIENT")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)

        toolbar = Toolbar(this).apply {
            layoutParams = Toolbar.LayoutParams(
                Toolbar.LayoutParams.MATCH_PARENT,
                Toolbar.LayoutParams.WRAP_CONTENT
            ).apply {
                top = ConstraintLayout.LayoutParams.PARENT_ID
            }
            setBackgroundColor(ContextCompat.getColor(this@ArticleActivity, R.color.blue))
            setTitleTextColor(ContextCompat.getColor(this@ArticleActivity, R.color.white))
            title = "Articles"
        }
        toolbar.elevation = 8f

        // Menambahkan Toolbar ke dalam root layout
        (binding.root as ViewGroup).addView(toolbar, 0)

        // Menjadikan Toolbar sebagai Support ActionBar
        setSupportActionBar(toolbar)

        // Opsional: Tambahkan tombol back navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = ArticleAdapter()
        binding.rvArticles.adapter = adapter
        binding.rvArticles.layoutManager = LinearLayoutManager(this).apply {
            // Mengatur agar item di dalam RecyclerView di tengah
            stackFromEnd = false
            reverseLayout = false
        }

        binding.rvArticles.apply {
            setPadding(40, 120, 0, 0) // Padding atas dan bawah untuk memusatkan konten
            clipToPadding = false
        }
//        binding.rvArticles.layoutManager = LinearLayoutManager(this)

        viewModel.listArticle.observe(this) { articleResponse ->
            adapter.submitList(articleResponse)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.showArticles()

        viewModel.errorMessage.observe(this) {
            if (it != null) Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        binding.fabAddarticle.visibility = View.GONE
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onBackPressed()
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}