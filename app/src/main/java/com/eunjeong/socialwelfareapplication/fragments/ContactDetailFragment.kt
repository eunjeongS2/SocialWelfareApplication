package com.eunjeong.socialwelfareapplication.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.eunjeong.socialwelfareapplication.R
import com.eunjeong.socialwelfareapplication.models.Contact
import com.eunjeong.socialwelfareapplication.models.imageReference
import com.eunjeong.socialwelfareapplication.viewmodels.UserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_contact_detail.view.*


class ContactDetailFragment(private val item: Contact, private val viewModel: UserViewModel) : Fragment() {
    private val disposeBag = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_detail, container, false)

        var editFragment = AddContactFragment(item, "", viewModel)

        viewModel.userPublisher.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ contactList ->
                val current = contactList.find { c: Contact -> c.key == item.key }
                setView(view, current ?: item)
                editFragment = AddContactFragment(current ?: item, "", viewModel)

            }, { e ->
                Log.d(ContactFragment.TAG, "e : ", e)

            }).addTo(disposeBag)


        if (item.group != "광교복지관") {
            viewModel.getMonitoringString(item.key)
            viewModel.monitoringPublisher.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ monitoringList ->
                    var monitoring = ""

                    monitoringList.forEach {
                        monitoring += it.date.replace("/", ".") + " "+ it.remark + "\n"
                    }
                    if(monitoringList.size > 2) {
                        view.monitoringButton.visibility = View.GONE
                    }
                    view.monitoringEditText.text = monitoring.substring(0, monitoring.length-1)

                }, {

                }).addTo(disposeBag)

            view.monitoringButton.setOnClickListener { viewModel.getMonitoringList(item.key) }

        } else {
            view.monitoringTextView.visibility = View.GONE
            view.monitoringEditText.visibility = View.GONE
            view.monitoringButton.visibility = View.GONE
        }

        val dialogListener = DialogInterface.OnClickListener { _, p1 ->
            when(p1) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.removeData(item.key, item.image) {

                        val transaction = parentFragmentManager.beginTransaction()
                        transaction.remove(this@ContactDetailFragment).commit()
                    }
                }
                DialogInterface.BUTTON_NEGATIVE -> {

                }

            }
        }

        with(view) {
            backButton.setOnClickListener {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.remove(this@ContactDetailFragment).commit()
            }

            call.setOnClickListener {
                intent(Intent.ACTION_DIAL, "tel:${item.phoneNumber}", it.context)
            }

            message.setOnClickListener {
                intent(Intent.ACTION_SENDTO, "sms:${item.phoneNumber}", it.context)
            }

            editButton.setOnClickListener {
                if (editFragment.isAdded) {
                    return@setOnClickListener
                }
                val transaction = parentFragmentManager.beginTransaction()
                transaction.add(R.id.fragmentContainer, editFragment).commit()
            }

            removeButton.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("연락처 삭제").setPositiveButton("삭제", dialogListener).setNegativeButton("취소", dialogListener).show()

            }

            setView(view, item)
        }


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.remove(this@ContactDetailFragment).commit()
            }
        })
    }

    override fun onDestroy() {
        disposeBag.dispose()
        super.onDestroy()
    }

    private fun setView(view: View, item: Contact) {
        view.nameText.text = item.name
        view.phoneEditText.text = item.phoneNumber
        view.emergencyEditText.text = item.emergencyNumber
        view.addressEditText.text = item.address
        view.star.isSelected = item.star

        if (item.image == "") {
            view.image.setImageResource(R.drawable.ic_contact)
        } else {
            Glide.with(view.context).load(imageReference("user/${item.image}"))
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(view.image)
        }
    }

}

fun intent(action: String, uriString: String, context: Context) {
    val intent = Intent(action).apply {
        data = Uri.parse(uriString)
    }

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}