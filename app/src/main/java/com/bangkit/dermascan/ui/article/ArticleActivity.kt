package com.bangkit.dermascan.ui.article

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dermascan.R
import com.bangkit.dermascan.databinding.ActivityArticleBinding
import com.bangkit.dermascan.ui.ViewModelFactory
import com.bangkit.dermascan.ui.articleAdd.ArticleAddActivity
import kotlinx.coroutines.launch

class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private val viewModel by viewModels<ArticleViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setSupportActionBar(binding.toolbar)

        val adapter = ArticleAdapter()
        binding.rvArticles.adapter = adapter
        binding.rvArticles.layoutManager = LinearLayoutManager(this)

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

        binding.fabAddarticle.setOnClickListener {
            val intent = Intent(this, ArticleAddActivity::class.java)
            startActivity(intent)
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.appbar_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_logout -> {
//                lifecycleScope.launch {
//                    viewModel.logout()
//                    val intent = Intent(this@ArticleActivity, LoginActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//                true
//            }
//
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}