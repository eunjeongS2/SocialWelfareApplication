package com.eunjeong.socialwelfareapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eunjeong.socialwelfareapplication.R
import com.eunjeong.socialwelfareapplication.models.Comment
import com.eunjeong.socialwelfareapplication.viewholders.CommentItemViewHolder

class CommentItemListAdapter : RecyclerView.Adapter<CommentItemViewHolder>() {
    var commentList: List<Comment> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)

        return CommentItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: CommentItemViewHolder, position: Int) {
        val item = commentList[position]
        holder.bind(item)

    }
}