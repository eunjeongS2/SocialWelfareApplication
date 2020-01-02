package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.adapters.CommentItemListAdapter
import com.example.socialwelfareapplication.models.Comment
import com.example.socialwelfareapplication.models.auth
import com.example.socialwelfareapplication.viewmodels.CommentViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_comment.view.*
import java.text.SimpleDateFormat
import java.util.*

class CommentFragment(private val key: String) : Fragment() {

    private lateinit var viewModel: CommentViewModel
    private var disposeBag = CompositeDisposable()

    companion object {
        const val TAG = "CommentFragment"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.GONE

        disposeBag = CompositeDisposable()

        activity?.let {
            viewModel = ViewModelProvider(it).get(CommentViewModel::class.java)
            viewModel.getData(key)
        }

        val adapter = CommentItemListAdapter()
        view?.recyclerView?.let { setupRecyclerView(it, adapter, RecyclerView.VERTICAL) }

        viewModel.commentPublisher.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ commentList ->
                setupItems(adapter, commentList)
                view?.progressBar?.visibility = View.GONE

            }, { e ->
                Log.d(TAG, "e : ", e)

            })
            .addTo(disposeBag)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_comment, container, false)

        view.backButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.remove(this).commit()
        }

        view.postButton.setOnClickListener {
            if (view.commentEditText.text.isBlank()) return@setOnClickListener

            view.progressBar.visibility = View.VISIBLE

            auth.currentUser?.let {
                viewModel.addData(
                    key, Comment(
                        it.displayName ?: "익명",
                        view.commentEditText.text.toString(),
                        SimpleDateFormat("yyyy년 M월 d일 HH:mm", Locale.KOREA).format(Date())
                        )
                )
            }
            view.commentEditText.text.clear()
        }

        return view
    }

    private fun setupItems(adapter: CommentItemListAdapter, itemList: List<Comment>) {
        adapter.commentList = itemList
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.VISIBLE

        disposeBag.dispose()
        super.onDestroy()

    }
}