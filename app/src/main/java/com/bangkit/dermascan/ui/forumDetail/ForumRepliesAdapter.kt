package com.bangkit.dermascan.ui.forumDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.dermascan.data.model.response.ForumReply
import com.bangkit.dermascan.databinding.ItemRepliesCardBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ForumRepliesAdapter(private val replies: List<ForumReply>) :
    RecyclerView.Adapter<ForumRepliesAdapter.ReplyViewHolder>() {

    inner class ReplyViewHolder(private val binding: ItemRepliesCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reply: ForumReply) {
            binding.tvResponderName.text = "${reply.responder?.firstName} ${reply.responder?.lastName}"
            binding.tvResponderRole.text = reply.responderRole
            binding.tvReplyContent.text = reply.content

            reply.createdAt?.let {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
                val date = inputFormat.parse(it)
                binding.tvReplyDate.text = date?.let { outputFormat.format(it) }
            }
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
        holder.bind(replies[position])
    }

    override fun getItemCount() = replies.size
}