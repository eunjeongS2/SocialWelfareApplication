package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.adapters.ContactGroupItemListAdapter
import com.example.socialwelfareapplication.adapters.ContactItemListAdapter
import com.example.socialwelfareapplication.models.Contact
import com.example.socialwelfareapplication.viewmodels.UserViewModel
import com.jakewharton.rxbinding3.widget.queryTextChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add_monitoring_description.view.backButton
import kotlinx.android.synthetic.main.fragment_contact.view.*
import java.util.concurrent.TimeUnit

class ContactFragment : Fragment() {

    private lateinit var groupAdapter: ContactGroupItemListAdapter
    private lateinit var contactAdapter: ContactItemListAdapter

    private lateinit var viewModel: UserViewModel
    private val disposeBag = CompositeDisposable()
    private val searchDisposeBag = CompositeDisposable()

    companion object {
        const val TAG = "ContactFragment"
        const val QUERY_TIMEOUT = 500L

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(UserViewModel::class.java)

            groupAdapter = ContactGroupItemListAdapter(viewModel)
            contactAdapter = ContactItemListAdapter(viewModel, R.layout.item_contact)
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

                    setView(it, groupAdapter.selectGroup)

                    view?.searchView?.setSearch(contactList)
                }

            }, { e ->
                Log.d(TAG, "e : ", e)

            }).addTo(disposeBag)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        view.backButton.visibility = View.GONE
        view.saveButton.visibility = View.GONE

        view.addButton.setOnClickListener {
            val contactDetailFragment = AddContactFragment(null, groupAdapter.selectGroup, viewModel)

            if (contactDetailFragment.isAdded) {
                return@setOnClickListener
            }

            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainer, contactDetailFragment).commit()

        }
        return view
    }

    override fun onDestroy() {
        disposeBag.dispose()
        searchDisposeBag.dispose()
        super.onDestroy()
    }

    private fun setView(view: View, group: String) {
        if (group == "전체" || group == "중요") {
            view.addButton.visibility = View.INVISIBLE
        } else {
            view.addButton.visibility = View.VISIBLE
        }
    }

    private fun SearchView.setSearch(contactList: List<Contact>) {
        searchDisposeBag.clear()
        this.queryTextChanges().debounce(QUERY_TIMEOUT, TimeUnit.MILLISECONDS)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ search ->
                if (search.isBlank()) {
                    contactAdapter.contactList = contactList
                    contactAdapter.notifyDataSetChanged()

                    return@subscribe
                }
                val result = query(contactList, search.toString())

                contactAdapter.contactList = result
                contactAdapter.notifyDataSetChanged()

            }, { e ->
                e.printStackTrace()
            })?.addTo(disposeBag)

    }

    private fun query(contactList: List<Contact>, search: String): List<Contact> {
        return contactList.filter {
            it.name.contains(search) || it.phoneNumber.contains(search) || it.emergencyNumber.contains(search)
        }
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

