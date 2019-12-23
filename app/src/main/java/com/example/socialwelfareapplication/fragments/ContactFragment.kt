package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.adapters.ContactGroupItemListAdapter
import com.example.socialwelfareapplication.adapters.ContactItemListAdapter
import com.example.socialwelfareapplication.models.Contact
import com.example.socialwelfareapplication.viewmodels.UserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_monitoring_description.view.backButton
import kotlinx.android.synthetic.main.fragment_contact.view.*

class ContactFragment : Fragment() {

    private lateinit var groupAdapter: ContactGroupItemListAdapter
    private lateinit var contactAdapter: ContactItemListAdapter

    private lateinit var viewModel: UserViewModel
    private val disposeBag = CompositeDisposable()

    companion object {
        const val TAG = "ContactFragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(UserViewModel::class.java)

        }

        groupAdapter = ContactGroupItemListAdapter(viewModel)

        context?.let {
            contactAdapter = ContactItemListAdapter(it, viewModel, R.layout.item_contact)
        }

        view?.let {
            setupRecyclerView(it.contactRecyclerView, contactAdapter, RecyclerView.VERTICAL)
            setupRecyclerView(it.groupRecyclerView, groupAdapter, RecyclerView.VERTICAL)
        }

        viewModel.userPublisher.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ contactList ->

                view?.let {
                    setupItems(contactAdapter, contactList)
                    val selectGroupText = "${groupAdapter.selectGroup}(${contactAdapter.itemCount})"
                    it.selectGroup.text = selectGroupText
                }

            }, { e ->
                Log.d(TAG, "e : ", e)

            }).addTo(disposeBag)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        view.backButton.visibility = View.GONE


        return view
    }

    override fun onDestroy() {
        disposeBag.dispose()
        super.onDestroy()
    }

}

fun setupItems(adapter: ContactItemListAdapter, itemList: List<Contact>) {
    adapter.contactList = itemList
    adapter.notifyDataSetChanged()
}

fun setupRecyclerView(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>, layout: Int) {
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context, layout, false)

}

