package com.eunjeong.socialwelfareapplication.viewholders

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.eunjeong.socialwelfareapplication.models.Comment
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentItemViewHolder(private val view : View) : RecyclerView.ViewHolder(view) {
    fun bind(item: Comment) {
        val source = "<b>${item.writer}</b>&ensp;${item.body}"

        view.body.text = source.htmlToString()
        view.timeStamp.text = item.timestamp.substring(6, item.timestamp.length)

    }

    private fun String.htmlToString() : Spanned {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(this)
        }
    }


}
