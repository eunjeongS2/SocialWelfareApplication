package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.checkDate
import com.example.socialwelfareapplication.models.Notice
import com.example.socialwelfareapplication.viewmodels.NoticeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_add_notice.*
import kotlinx.android.synthetic.main.fragment_add_notice.view.*
import org.threeten.bp.LocalDate

class AddNoticeFragment(private val viewModel: NoticeViewModel) : Fragment() {
    private val disposeBag = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_notice, container, false)

        view.cancelButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.remove(this).commit()
        }

        view.writeButton.setOnClickListener {
            if (view.spinner.selectedItem.toString() == "게시판 선택") {
                Toast.makeText(view.context, "게시판을 선택해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (view.titleEditText.text.isBlank()) {
                Toast.makeText(view.context, "제목을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (view.bodyEditText.text.isBlank()) {
                Toast.makeText(view.context, "내용을 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentDate = LocalDate.now()
            val currentMonth = currentDate.monthValue.toString().checkDate()
            val currentDay = currentDate.dayOfMonth.toString().checkDate()

            val date = "${currentDate.year}/$currentMonth/$currentDay"

            progressBar.visibility = View.VISIBLE

            viewModel.addData(
                Notice(
                    view.spinner.selectedItem.toString(),
                    date,
                    view.titleEditText.text.toString(),
                    view.bodyEditText.text.toString()
                    )
            ) {
                progressBar.visibility = View.GONE
                val transaction = parentFragmentManager.beginTransaction()
                transaction.remove(this).commit()
            }

        }

        val groupList = view.context.resources.getStringArray(R.array.notice_group).toList()
        val adapter = object : ArrayAdapter<String>(view.context, R.layout.item_spinner, groupList) {
            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }
        view.spinner.adapter = adapter
        view.spinner.setSelection(adapter.count)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.GONE
        super.onActivityCreated(savedInstanceState)

    }

    override fun onDestroy() {
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)?.visibility = View.VISIBLE
        disposeBag.dispose()
        super.onDestroy()

    }
}