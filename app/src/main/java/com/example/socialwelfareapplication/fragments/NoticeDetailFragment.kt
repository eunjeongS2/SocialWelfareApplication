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
import com.example.socialwelfareapplication.viewmodels.NoticeViewModel
import kotlinx.android.synthetic.main.fragment_notice_detail.view.*
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.StorageReference
import com.bumptech.glide.module.AppGlideModule
import android.content.Context
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.example.socialwelfareapplication.models.imageReference
import java.io.InputStream



class NoticeDetailFragment(private val item: Notice, private val viewModel: NoticeViewModel) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notice_detail, container, false)

        val transaction = parentFragmentManager.beginTransaction()

        view.detailBackButton.setOnClickListener { transaction.remove(this).commit() }

        val titleText = "[${item.group}] ${item.title}"
        view.titleTextView.text = titleText
        view.dateTextView.text = item.date
        view.descriptionTextView.text = item.body

        if (item.image == "") {
            view.itemImageView.visibility = View.GONE
        } else {
            Glide.with(view.context).load(imageReference("notice/${item.image}"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(view.itemImageView)
        }

        view.deleteButton.setOnClickListener {
            viewModel.removeData(item.key) {
                if (targetFragment != null) {
                    viewModel.getCurrentMenuData()
                } else {
                    when (item.group) {
                        "도시락" -> {
                            viewModel.getMenuData()
                        }
                        "공지사항" -> {
                            viewModel.getData()
                        }
                    }
                }
                transaction.remove(this).commit()
            }
        }


        return view
    }

}

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        // Register FirebaseImageLoader to handle StorageReference
        registry.append(
            StorageReference::class.java, InputStream::class.java,
            FirebaseImageLoader.Factory()
        )
    }
}