package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.adapters.NoticeItemListAdapter
import com.example.socialwelfareapplication.models.Notice
import com.example.socialwelfareapplication.viewmodels.NoticeViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_notice.view.*


class NoticeFragment : Fragment() {

    private lateinit var viewModel: NoticeViewModel
    private lateinit var adapter: NoticeItemListAdapter
    private val disposeBag = CompositeDisposable()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(NoticeViewModel::class.java)
            viewModel.getData()

            adapter = NoticeItemListAdapter(viewModel)
            view?.noticeRecyclerView?.let { recyclerView ->
                setupRecyclerView(recyclerView, adapter, RecyclerView.VERTICAL)
            }
        }

        viewModel.noticePublisher.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ noticeList ->
                setupItems(adapter, noticeList)

            }, { e ->
                Log.d(MonitoringFragment.TAG, "e : ", e)

            })
            .addTo(disposeBag)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notice, container, false)

        view.noticeRecyclerView.setOnTouchListener { _, _ ->
            if (view.noticeSearchView.hasFocus())
                view.noticeSearchView.clearFocus()

            return@setOnTouchListener false
        }

        view.addNoticeButton.setOnClickListener {
            val addNoticeFragment = AddNoticeFragment(viewModel)

            if (addNoticeFragment.isAdded) {
                return@setOnClickListener
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainer, addNoticeFragment).commit()
        }

        return view
    }

    private fun setupItems(adapter: NoticeItemListAdapter, itemList: List<Notice>) {
        adapter.noticeList = itemList
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        disposeBag.dispose()
        super.onDestroy()
    }

}
