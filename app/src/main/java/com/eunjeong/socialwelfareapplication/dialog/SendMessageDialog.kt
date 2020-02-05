package com.eunjeong.socialwelfareapplication.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.eunjeong.socialwelfareapplication.R
import kotlinx.android.synthetic.main.dialog_message.view.*

class SendMessageDialog : DialogFragment() {

    var sendText: String = ""

    companion object {
        const val TAG = "SendMessageDialog"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_message, container, false)

        view.cancelButton.setOnClickListener { dismiss() }

        view.userText.text = sendText


        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}