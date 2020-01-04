package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.models.Monitoring
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.adapters.MonitoringItemListAdapter
import com.example.socialwelfareapplication.viewmodels.MonitoringViewModel
import com.jakewharton.rxbinding3.widget.queryTextChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_monitoring.view.*
import java.util.concurrent.TimeUnit

class MonitoringFragment : Fragment() {

    private lateinit var viewModel: MonitoringViewModel
    private val disposeBag = CompositeDisposable()
    private val searchDisposeBag = CompositeDisposable()

    private lateinit var monitoringCalendarFragment: MonitoringCalendarFragment
    private val adapter = MonitoringItemListAdapter()


    companion object {
        const val REQUEST_CODE = 300
        const val QUERY_TIMEOUT = 500L
        const val TAG = "MonitoringFragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(MonitoringViewModel::class.java)
            viewModel.getData()

            monitoringCalendarFragment = MonitoringCalendarFragment(viewModel)

        }

        view?.recyclerView?.let { setupRecyclerView(it, adapter, RecyclerView.VERTICAL) }

        viewModel.monitoringPublisher.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ monitoringList ->
                setupItems(adapter, monitoringList)
                view?.calendarImageView?.calendarIsSelected(false, monitoringCalendarFragment)

                monitoringCalendarFragment.dateList = viewModel.dateList

                view?.monitoringSearchView?.setSearch(monitoringList)


            }, { e ->
                Log.d(TAG, "e : ", e)

            })
            .addTo(disposeBag)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_monitoring, container, false)

        view.calendarImageView.setOnClickListener {
            view.monitoringSearchView.clearFocus()

            view.calendarImageView.calendarIsSelected(!view.calendarImageView.isSelected, monitoringCalendarFragment)

        }

        view.recyclerView.setOnTouchListener { _, _ ->
            if (view.monitoringSearchView.hasFocus())
                view.monitoringSearchView.clearFocus()
            else
                view.calendarImageView.calendarIsSelected(false, monitoringCalendarFragment)

            return@setOnTouchListener false
        }


        val addMonitoringFragment = AddMonitoringSelectContactFragment()

        view.addMonitoringButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            addMonitoringFragment.setTargetFragment(this, REQUEST_CODE)

            transaction.replace(R.id.fragmentContainer, addMonitoringFragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }

        return view
    }

    private fun ImageView.calendarIsSelected(isSelected: Boolean, fragment: Fragment) {
        this.isSelected = isSelected

        val transaction = parentFragmentManager.beginTransaction()
        fragment.setTargetFragment(this@MonitoringFragment, REQUEST_CODE)

        if (isSelected) {
            transaction.add(R.id.fragmentContainer, fragment)

        } else {
            transaction.remove(fragment)
        }

        transaction.commit()

    }

    private fun SearchView.setSearch(monitoringList: List<Monitoring>) {
        searchDisposeBag.clear()
        this.queryTextChanges().debounce(QUERY_TIMEOUT, TimeUnit.MILLISECONDS)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ search ->
                if (search.isBlank()) {
                    adapter.monitoringList = monitoringList
                    adapter.notifyDataSetChanged()

                    return@subscribe
                }
                val result = query(monitoringList, search.toString())

                adapter.monitoringList = result
                adapter.notifyDataSetChanged()

            }, { e ->
                e.printStackTrace()
            })?.addTo(disposeBag)

    }

    private fun query(monitoringList: List<Monitoring>, search: String): List<Monitoring> {
        return monitoringList.filter {
            it.place.contains(search) || it.writer.contains(search) || it.state.contains(search)
                    || it.purpose.contains(search) || it.remark.contains(search)

        }
    }

    private fun setupItems(adapter: MonitoringItemListAdapter, itemList: List<Monitoring>) {
        adapter.monitoringList = itemList
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        disposeBag.dispose()
        searchDisposeBag.dispose()
        super.onDestroy()
    }

}