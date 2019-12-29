package com.example.socialwelfareapplication.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.models.Contact
import kotlinx.android.synthetic.main.fragment_contact_detail.view.*
import kotlinx.android.synthetic.main.fragment_contact_detail.view.image
import kotlinx.android.synthetic.main.item_contact_select.view.*


class ContactDetailFragment(private val item: Contact?) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_detail, container, false)

        val transaction = parentFragmentManager.beginTransaction()

        view.backButton.setOnClickListener {
            transaction.remove(this)
            transaction.commit()
        }

        item?.let { item ->
//            view. = false
            view.call.setOnClickListener {
                intent(Intent.ACTION_DIAL, "tel:${item.phoneNumber}", it.context)
            }

            view.message.setOnClickListener {
                intent(Intent.ACTION_SENDTO, "sms:${item.phoneNumber}", it.context)
            }

            view.nameText.setText(item.name)
            view.phoneEditText.setText(item.phoneNumber)
            view.emergencyEditText.setText(item.emergencyNumber)
            view.addressEditText.setText(item.address)

            val monitoring = "2019.05.20 집에 안계서서 문앞에 두고 감. \n2019.02.14 십분동안 담소를 나눴다."
            view.monitoringEditText.text = monitoring

            if (item.image == "") {
                view.image.setImageResource(R.drawable.ic_contact)
            } else {
                Glide.with(view.context).load(item.image)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(view.image)
            }
        }



        return view
    }
}

fun intent (action: String, uriString: String, context: Context) {
    val intent = Intent(action).apply {
        data = Uri.parse(uriString)
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}