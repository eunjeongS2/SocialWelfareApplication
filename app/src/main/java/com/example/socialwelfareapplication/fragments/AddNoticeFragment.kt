package com.example.socialwelfareapplication.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.checkDate
import com.example.socialwelfareapplication.models.Notice
import com.example.socialwelfareapplication.removeImage
import com.example.socialwelfareapplication.viewmodels.NoticeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_add_notice.*
import kotlinx.android.synthetic.main.fragment_add_notice.view.*
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*

class AddNoticeFragment(private val viewModel: NoticeViewModel) : Fragment() {

    private val disposeBag = CompositeDisposable()
    private var image: Pair<Sources, Uri>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_notice, container, false)

        view.cancelButton.setOnClickListener {
            context?.let {
                image?.removeImage(it)
                image = null
            }

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
                    view.bodyEditText.text.toString(),
                    image?.let { SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date()) } ?: ""
                ), image?.second) {

                context?.let {
                    image?.removeImage(it)
                    image = null
                }

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

        view.cameraButton.setOnClickListener {
            getImage(Sources.CAMERA) {
                view.removeImageButton.visibility = View.VISIBLE
                view.noticeImageView.visibility = View.VISIBLE

                Glide.with(this).load(image?.second)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(view.noticeImageView)
            }
        }

        view.albumButton.setOnClickListener {

            getImage(Sources.GALLERY) {
                view.removeImageButton.visibility = View.VISIBLE
                view.noticeImageView.visibility = View.VISIBLE

                Glide.with(this).load(image?.second)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(view.noticeImageView)
            }
        }

        view.removeImageButton.setOnClickListener {
            view.removeImageButton.visibility = View.GONE
            view.noticeImageView.visibility = View.GONE

            context?.let {
                image?.removeImage(it)
                image = null
            }

        }

        return view
    }

    private fun getImage(source: Sources, onSubscribe: (() -> Unit)? = null) {
        context?.let {
            image?.removeImage(it)
            image = null
        }

        disposeBag.clear()
        RxImagePicker.with(parentFragmentManager).requestImage(source)
            .subscribeBy(
                onNext = {
                    image = Pair(source, it)
                    onSubscribe?.invoke()
                }
            ).addTo(disposeBag)
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

