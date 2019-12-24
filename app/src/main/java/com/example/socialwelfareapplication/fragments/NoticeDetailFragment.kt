package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.models.Notice
import kotlinx.android.synthetic.main.fragment_notice_detail.view.*

class NoticeDetailFragment(private val item: Notice) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notice_detail, container, false)

        val transaction = parentFragmentManager.beginTransaction()

        view.detailBackButton.setOnClickListener {
            transaction.remove(this)
            transaction.commit()
        }

        val titleText = "[${item.group}] ${item.title}"
        view.titleTextView.text = titleText
        view.dateTextView.text = item.date.toString()
        view.descriptionTextView.text = item.body

        Glide.with(view.context).load(item.image)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(view.itemImageView)

        return view
    }

}