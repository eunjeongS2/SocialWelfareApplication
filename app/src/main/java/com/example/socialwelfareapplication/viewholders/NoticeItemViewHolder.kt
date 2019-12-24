package com.example.socialwelfareapplication.viewholders

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.fragments.MonitoringFragment
import com.example.socialwelfareapplication.fragments.NoticeDetailFragment
import com.example.socialwelfareapplication.models.Notice
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.item_notice.view.*
import java.util.concurrent.TimeUnit

class NoticeItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(item: Notice) {

        with(view) {
            val titleText = "[${item.group}] ${item.title}"
            title.text = titleText
            date.text = item.date.replace("/", ".")

            val detailFragment = NoticeDetailFragment(item)

            this.clicks()
                .throttleFirst(600, TimeUnit.MILLISECONDS)
                .subscribe({
                    val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()

                    transaction.add(R.id.fragmentContainer, detailFragment)
                    transaction.commit()

                }, {e->
                    Log.d(NoticeItemViewHolder::class.java.name, "view click failed : ", e)

                })
        }
    }

}