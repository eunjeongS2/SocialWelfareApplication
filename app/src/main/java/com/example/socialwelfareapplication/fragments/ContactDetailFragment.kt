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
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_contact_detail.view.*


class ContactDetailFragment(private val item: Contact) : Fragment() {
    private val disposeBag = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_detail, container, false)


        view.backButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.remove(this).commit()
        }

        with(view) {
            call.setOnClickListener {
                intent(Intent.ACTION_DIAL, "tel:${item.phoneNumber}", it.context)
            }

            message.setOnClickListener {
                intent(Intent.ACTION_SENDTO, "sms:${item.phoneNumber}", it.context)
            }

            editButton.setOnClickListener {
                val editFragment = AddContactFragment(item, "")
                    if (editFragment.isAdded) {
                        return@setOnClickListener
                    }
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.add(R.id.fragmentContainer, editFragment).commit()
            }


            nameText.text = item.name
            phoneEditText.text = item.phoneNumber
            emergencyEditText.text = item.emergencyNumber
            addressEditText.text = item.address
            star.isSelected = item.star


            val monitoring = "2019.05.20 집에 안계서서 문앞에 두고 감. \n2019.02.14 십분동안 담소를 나눴다."
            monitoringEditText.text = monitoring

            if (item.image == "") {
                image.setImageResource(R.drawable.ic_contact)
            } else {
                Glide.with(view.context).load(item.image)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(image)
            }
        }


        return view
    }

    override fun onDestroy() {
        disposeBag.dispose()
        super.onDestroy()
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