package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.adapters.NoticeItemListAdapter
import com.example.socialwelfareapplication.models.Notice
import com.example.socialwelfareapplication.viewmodels.NoticeViewModel
import com.jakewharton.rxbinding3.widget.queryTextChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_notice.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class NoticeFragment : Fragment() {

    private lateinit var viewModel: NoticeViewModel
    private lateinit var adapter: NoticeItemListAdapter
    private val disposeBag = CompositeDisposable()
    private val searchDisposeBag = CompositeDisposable()

    companion object {
        const val QUERY_TIMEOUT = 500L
        const val REQUEST_CODE = 300

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val date = SimpleDateFormat("yyyy/MM", Locale.KOREA).format(Date())

        activity?.let {
            viewModel = ViewModelProvider(it).get(NoticeViewModel::class.java)
            viewModel.getData()
            viewModel.getCurrentMenuData(date)

            adapter = NoticeItemListAdapter(viewModel, R.layout.item_notice)
            view?.noticeRecyclerView?.let { recyclerView ->
                setupRecyclerView(recyclerView, adapter, RecyclerView.VERTICAL)
            }
        }

        viewModel.noticePublisher.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ noticeList ->
                setupItems(adapter, noticeList)

                view?.noticeSearchView?.setSearch(noticeList)


            }, { e ->
                Log.d(MonitoringFragment.TAG, "e : ", e)

            })
            .addTo(disposeBag)

        viewModel.currentMenuPublisher.observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view?.title?.text = it.title
                view?.date?.text = it.date.replace("/", ".")

                val detailFragment = NoticeDetailFragment(it, viewModel)
                detailFragment.setTargetFragment(this, REQUEST_CODE)

                view?.cardView?.setOnClickListener {
                    if (detailFragment.isAdded) {
                        return@setOnClickListener
                    }

                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.add(R.id.fragmentContainer, detailFragment).commit()
                }

            }, { e ->
                Log.d(MonitoringFragment.TAG, "e : ", e)


            }).addTo(disposeBag)

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

        view.allMenuButton.setOnClickListener {
            val allMenuFragment = AllMenuFragment(viewModel)

            if (allMenuFragment.isAdded) {
                return@setOnClickListener
            }
            val transaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainer, allMenuFragment).commit()
        }

        return view
    }

    private fun SearchView.setSearch(noticeList: List<Notice>) {
        searchDisposeBag.clear()
        this.queryTextChanges().debounce(QUERY_TIMEOUT, TimeUnit.MILLISECONDS)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ search ->
                if (search.isBlank()) {
                    adapter.noticeList = noticeList
                    adapter.notifyDataSetChanged()

                    return@subscribe
                }
                val result = query(noticeList, search.toString())

                adapter.noticeList = result
                adapter.notifyDataSetChanged()

            }, { e ->
                e.printStackTrace()
            })?.addTo(disposeBag)

    }

    private fun query(monitoringList: List<Notice>, search: String): List<Notice> {
        return monitoringList.filter {
            it.date.contains(search) || it.title.contains(search) || it.body.contains(search)

        }
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
