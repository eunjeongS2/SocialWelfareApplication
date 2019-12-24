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
    private val disposeBag = CompositeDisposable()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(NoticeViewModel::class.java)
            viewModel.getData()

//            viewModel.addData(Notice("도시락", "2019/05/16", "5월 16일 도시락 메뉴", "5월 16일 도시락 메뉴", "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"))
//            viewModel.addData(Notice("실습", "2019/05/16", "2019년 하계방학 사회복지실습", "2019년 하계방학 사회복지실습", "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"))
//            viewModel.addData(Notice("도시락", "2019/05/15", "5월 15일 도시락 메뉴", "5월 15일 도시락 메뉴", "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"))
//            viewModel.addData(Notice("도시락", "2019/05/14", "5월 14일 도시락 메뉴", "5월 14일 도시락 메뉴", "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"))
//            viewModel.addData(Notice("도시락", "2019/05/13", "5월 13일 도시락 메뉴", "5월 13일 도시락 메뉴", "https://ssl.pstatic.net/mimgnews/image/112/2018/11/09/201811091110220474475_20181109111030_01_20181109111920216.jpg?type=w540"))

        }

        val adapter = NoticeItemListAdapter()
        view?.noticeRecyclerView?.adapter = adapter

        if (viewModel.noticeList.isNotEmpty()) {
            setupItems(adapter, viewModel.noticeList)
        }

        view?.noticeRecyclerView?.let { setupRecyclerView(it, adapter, RecyclerView.VERTICAL) }

        viewModel.noticePublisher.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ noticeList ->
                setupItems(adapter, noticeList)

                view?.let {
                }

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