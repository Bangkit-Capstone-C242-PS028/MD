package com.bangkit.dermascan.ui.forumDetail

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dermascan.data.model.response.ForumReply
import com.bangkit.dermascan.databinding.ItemRepliesCardBinding
import com.bangkit.dermascan.ui.article.ArticlePagingAdapter.Companion.DIFF_CALLBACK
import java.text.SimpleDateFormat
import java.util.Locale

class ForumRepliesAdapterPaging : PagingDataAdapter<ForumReply, ForumRepliesAdapterPaging.ReplyViewHolder>(
    DIFF_CALLBACK
) {

    class ReplyViewHolder(private val binding: ItemRepliesCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(reply: ForumReply) {
            Log.d("ForumRepliesAdapterPaging", "Binding reply: $reply")
            binding.tvResponderName.text = "${reply.responder?.firstName} ${reply.responder?.lastName}"
            binding.tvResponderRole.text = reply.responderRole
            binding.tvReplyContent.text = reply.content
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            if (reply.createdAt != null) {
                val date = inputFormat.parse(reply.createdAt)
                binding.tvReplyDate.text = date?.let { outputFormat.format(it) }
            }

//            reply.createdAt
//            reply.createdAt?.let { it ->

//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val binding = ItemRepliesCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReplyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        val reply = getItem(position) // Dapatkan data dari PagingData
        if (reply != null) {
            holder.bind(reply)
        }

    }

    // Callback untuk membandingkan item ForumReply
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ForumReply>() {
            override fun areItemsTheSame(
                oldItem: ForumReply,
                newItem: ForumReply
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ForumReply,
                newItem: ForumReply
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
