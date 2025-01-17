package com.eunjeong.socialwelfareapplication.viewholders

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.eunjeong.socialwelfareapplication.R
import com.eunjeong.socialwelfareapplication.fragments.CommentFragment
import com.eunjeong.socialwelfareapplication.models.Monitoring
import com.eunjeong.socialwelfareapplication.models.imageReference
import com.eunjeong.socialwelfareapplication.viewmodels.CommentViewModel
import kotlinx.android.synthetic.main.item_monitoring.view.*

class MonitoringItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Monitoring) {

        val date = "${item.date.split("/")[1]}/${item.date.split("/")[2]}/${item.date.split("/")[0]}"

        view.monitoringDate.text = date
        view.visitPlace.text = item.place
        view.visitPurpose.text = item.purpose
        view.state.text = item.state

        if (item.remark.isBlank()) {
            view.remarkText.visibility = View.GONE
            view.remark.visibility = View.GONE
        } else {
            view.remarkText.visibility = View.VISIBLE
            view.remark.visibility = View.VISIBLE
            view.remark.text = item.remark
        }

        if(item.comments) {
            CommentViewModel().getCommentCount(item.monitoringKey){
                view.commentButton.text = setCommentText(it)
            }
        } else {
            view.commentButton.text = setCommentText("0")
        }

        val commentFragment = CommentFragment(item.monitoringKey)

        view.commentButton.setOnClickListener {
            if (commentFragment.isAdded) {
                return@setOnClickListener
            }

            val transaction = (view.context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainer, commentFragment).commit()
        }

        if(item.image == "") {
            view.monitoringImage.visibility = View.GONE
        } else {
            view.monitoringImage.visibility = View.VISIBLE
            Glide.with(view.context)
                .load(imageReference("monitoring/${item.image}"))
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(view.monitoringImage)
        }
    }

    private fun setCommentText(count: String): String {
        return "댓글 ${count}개 모두 보기"
    }
}