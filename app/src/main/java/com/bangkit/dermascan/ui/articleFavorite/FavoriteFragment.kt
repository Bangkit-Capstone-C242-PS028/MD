package com.bangkit.dermascan.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.dermascan.data.model.response.ArticleItem
import com.bangkit.dermascan.databinding.FragmentFavoriteBinding
import com.bangkit.dermascan.ui.article.ArticleAdapter

class FavoriteArticleFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteViewModel: FavoriteArticleViewModel
    private lateinit var adapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = FavoriteArticleViewModelFactory(requireActivity().application)
        favoriteViewModel = ViewModelProvider(this, factory)[FavoriteArticleViewModel::class.java]

        adapter = ArticleAdapter()
        binding.rvListArticle.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListArticle.adapter = adapter

        favoriteViewModel.getFavoriteArticles().observe(viewLifecycleOwner) { favoriteArticles ->
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}