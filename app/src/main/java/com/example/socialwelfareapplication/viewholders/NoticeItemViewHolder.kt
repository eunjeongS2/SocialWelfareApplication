package com.example.socialwelfareapplication.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.models.Notice
import kotlinx.android.synthetic.main.item_notice.view.*

class NoticeItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Notice) {
        val title = "[${item.group}] ${item.title}"

        view.title.text = title
        view.date.text = item.date.replace("/", ".")

    }

}