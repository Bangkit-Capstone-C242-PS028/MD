package com.bangkit.dermascan.ui.forum

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dermascan.data.model.response.ForumItem
import com.bangkit.dermascan.databinding.ItemForumCardBinding
import com.bangkit.dermascan.ui.forumDetail.ForumDetailActivity
import com.bangkit.dermascan.util.formatTime

class ForumAdapter : ListAdapter<ForumItem, ForumAdapter.ViewHolder>(DIFF_CALLBACK) {

    class ViewHolder(private val binding: ItemForumCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(forumItem: ForumItem) {
            binding.apply {
                tvTitlecard.text = forumItem.title
                tvContentcard.text = forumItem.content
                tvTimecard.text = formatTime(itemView.context, forumItem.createdAt)
                tvStatuscard.text = forumItem.status

                // Display patient name if available
                val patientName = forumItem.patient?.user?.let {
                    "${it.firstName} ${it.lastName}".trim()
                } ?: "Unknown Patient"
                tvNamecard.text = patientName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemForumCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forum = getItem(position)
        holder.bind(forum)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val forumId = forum.id

            if (!forumId.isNullOrBlank()) {
                val intent = Intent(context, ForumDetailActivity::class.java).apply {
                    putExtra("FORUM_ID", forumId)
                }
                Log.d("ForumActivity", "Navigating to ForumDetailActivity with ID: ${forum.id}")
                context.startActivity(intent)

            } else {
                // Log error jika forumId null atau kosong
                Toast.makeText(context, "Forum ID is invalid", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ForumItem>() {
            override fun areItemsTheSame(
                oldItem: ForumItem,
                newItem: ForumItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ForumItem,
                newItem: ForumItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}