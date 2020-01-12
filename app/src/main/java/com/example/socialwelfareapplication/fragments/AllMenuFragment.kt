package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.adapters.NoticeItemListAdapter
import com.example.socialwelfareapplication.models.Notice
import com.example.socialwelfareapplication.viewmodels.NoticeViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_notice_menu.view.*

class AllMenuFragment(private val viewModel: NoticeViewModel) : Fragment() {
    private val disposeBag = CompositeDisposable()

    companion object {
        const val TAG = "AllMenuFragment"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notice_menu, container, false)

        view.backButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.remove(this).commit()
        }

        val adapter = NoticeItemListAdapter(viewModel, R.layout.item_menu)
        setupRecyclerView(view.menuRecyclerView, adapter, RecyclerView.VERTICAL)

        viewModel.getMenuData()
        viewModel.menuListPublisher.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ menuList ->
                setupItems(adapter, menuList)

            }, { e ->
                Log.d(TAG, "e : ", e)

            })
            .addTo(disposeBag)

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