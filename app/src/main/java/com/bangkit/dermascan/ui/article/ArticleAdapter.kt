package com.bangkit.dermascan.ui.article

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dermascan.R
import com.bangkit.dermascan.data.model.response.ListArticleItem
import com.bangkit.dermascan.databinding.ItemArticleCardBinding
import com.bangkit.dermascan.ui.articleDetail.ArticleDetailActivity
import com.bangkit.dermascan.util.formatTime
import com.bumptech.glide.Glide

class ArticleAdapter : ListAdapter<ListArticleItem, ArticleAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: ItemArticleCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(articleItem: ListArticleItem) {
            binding.apply {
                tvTitlecard.text = articleItem.title
                tvNamecard.text = articleItem.author
                binding.tvTimecard.text = formatTime(itemView.context, articleItem.createdAt)
            }

            Glide.with(binding.ivPhotocard.context)
                .load(articleItem.imageUrl)
                .into(binding.ivPhotocard)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemArticleCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)

        holder.itemView.setOnClickListener {
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.itemView.findViewById(R.id.ivPhotocard), "image"),
                    Pair(holder.itemView.findViewById(R.id.tvTitlecard), "title"),
                )

            val context = holder.itemView.context

            val intent = Intent(context, ArticleDetailActivity::class.java)
            intent.putExtra(ArticleDetailActivity.EXTRA_ARTICLE_ID, article.id)
            startActivity(context, intent, optionsCompat.toBundle())
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListArticleItem>() {
            override fun areItemsTheSame(
                oldItem: ListArticleItem,
                newItem: ListArticleItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListArticleItem,
                newItem: ListArticleItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}