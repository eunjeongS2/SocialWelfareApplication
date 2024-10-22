package com.eunjeong.socialwelfareapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.eunjeong.socialwelfareapplication.R
import com.eunjeong.socialwelfareapplication.fragments.NoticeDetailFragment
import com.eunjeong.socialwelfareapplication.models.Notice
import com.eunjeong.socialwelfareapplication.viewholders.NoticeItemViewHolder
import com.eunjeong.socialwelfareapplication.viewmodels.NoticeViewModel
import kotlinx.android.synthetic.main.item_menu.view.*

class NoticeItemListAdapter(private val viewModel: NoticeViewModel, private val layout: Int) : RecyclerView.Adapter<NoticeItemViewHolder>() {

    var noticeList: List<Notice> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
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

        if(layout == R.layout.item_menu) {
            holder.itemView.deleteButton.setOnClickListener {
                viewModel.removeData(item.key, item.image) {
                    viewModel.getMenuData()
                    viewModel.getCurrentMenuData()
                }
            }
        }

    }

}