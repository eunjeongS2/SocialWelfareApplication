package com.eunjeong.socialwelfareapplication.dialog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.eunjeong.socialwelfareapplication.R
import com.eunjeong.socialwelfareapplication.fragments.intent
import com.eunjeong.socialwelfareapplication.models.Contact
import com.eunjeong.socialwelfareapplication.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.dialog_message.view.*

class SendMessageDialog(private val viewModel: UserViewModel) : DialogFragment() {

    var item: Contact = Contact()

    companion object {
        const val TAG = "SendMessageDialog"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_message, container, false)

        view.cancelButton.setOnClickListener { dismiss() }

        view.userText.text = item.name
        view.sendButton.setOnClickListener {
            context?.let {
                intent(Intent.ACTION_SENDTO, "sms:${item.phoneNumber}", it)
                dismiss()
            }
        }

        view.groupButton.setOnClickListener {
            viewModel.layoutPublisher.onNext("check")
            viewModel.selectList.add(item)

            dismiss()
        }


        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}