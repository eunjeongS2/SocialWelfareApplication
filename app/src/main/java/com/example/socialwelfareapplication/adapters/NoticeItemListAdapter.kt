package com.example.socialwelfareapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.fragments.NoticeDetailFragment
import com.example.socialwelfareapplication.models.Notice
import com.example.socialwelfareapplication.viewholders.NoticeItemViewHolder
import com.example.socialwelfareapplication.viewmodels.NoticeViewModel

class NoticeItemListAdapter(private val viewModel: NoticeViewModel) : RecyclerView.Adapter<NoticeItemViewHolder>() {

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

        val detailFragment = NoticeDetailFragment(item, viewModel)

        holder.itemView.setOnClickListener {
            if (detailFragment.isAdded) {
                return@setOnClickListener
            }

            val transaction = (holder.itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainer, detailFragment).commit()
        }
    }

}