package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.viewmodels.NoticeViewModel
import io.reactivex.disposables.CompositeDisposable

class NoticeFragment : Fragment() {

    private lateinit var viewModel: NoticeViewModel
    private val disposeBag = CompositeDisposable()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProvider(it).get(NoticeViewModel::class.java)
//            viewModel.getData()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notice, container, false)

        return view
    }

}