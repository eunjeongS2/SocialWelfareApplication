package com.example.socialwelfareapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.models.Notice
import com.example.socialwelfareapplication.viewholders.NoticeItemViewHolder

class NoticeItemListAdapter : RecyclerView.Adapter<NoticeItemViewHolder>() {

    var noticeList: List<Notice> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notice, parent, false)
        return NoticeItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    override fun onBindViewHolder(holder: NoticeItemViewHolder, position: Int) {
        val item = noticeList[position]
        holder.bind(item)
    }

}