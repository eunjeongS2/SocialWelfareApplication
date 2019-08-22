package com.example.socialwelfareapplication.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.socialwelfareapplication.models.Monitoring
import kotlinx.android.synthetic.main.item_monitoring.view.*

class MonitoringItemViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Monitoring) {
        view.monitoringDate.text = item.date
        view.visitPlace.text = item.place
        view.visitPurpose.text = item.purpose
        view.state.text = item.state
        view.remark.text = item.remark

        val comments = "댓글 ${item.comments}개 모두 보기"
        view.commentButton.text = comments

        Glide.with(view.context).load(item.image)
            .centerCrop()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(view.monitoringImage)

    }
}