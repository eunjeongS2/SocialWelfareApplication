package com.eunjeong.socialwelfareapplication.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.eunjeong.socialwelfareapplication.models.Notice
import kotlinx.android.synthetic.main.item_notice.view.*

class NoticeItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Notice) {

        with(view) {
            val titleText = "[${item.group}] ${item.title}"
            title.text = titleText
            date.text = item.date.replace("/", ".")

        }
    }

}