package com.example.socialwelfareapplication.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.adapters.ContactItemListAdapter
import com.example.socialwelfareapplication.checkDate
import com.example.socialwelfareapplication.models.Monitoring
import com.example.socialwelfareapplication.removeImage
import com.example.socialwelfareapplication.viewmodels.MonitoringViewModel
import com.example.socialwelfareapplication.viewmodels.UserViewModel
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.fragment_add_monitoring_description.*
import kotlinx.android.synthetic.main.fragment_add_monitoring_description.view.*
import kotlinx.android.synthetic.main.fragment_add_monitoring_description.view.remark
import kotlinx.android.synthetic.main.fragment_add_monitoring_description.view.visitPurpose
import org.threeten.bp.LocalDate
import java.text.SimpleDateFormat
import java.util.*

class AddMonitoringDescriptionFragment(private val viewModel: UserViewModel) : Fragment() {

    private lateinit var adapter: ContactItemListAdapter
    private lateinit var monitoringViewModel: MonitoringViewModel
    private var image: Pair<Sources, Uri>? = null
    private val disposeBag = CompositeDisposable()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            monitoringViewModel = ViewModelProvider(it).get(MonitoringViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_monitoring_description, container, false)

        adapter = ContactItemListAdapter(viewModel, R.layout.item_contact_simple)

        setupRecyclerView(view.selectContactRecyclerView, adapter, RecyclerView.HORIZONTAL)

        adapter.contactList = viewModel.selectList
        adapter.notifyDataSetChanged()

        val currentDate = LocalDate.now()

        val currentMonth = currentDate.monthValue.toString().checkDate()
        val currentDay = currentDate.dayOfMonth.toString().checkDate()

        val dateText = "$currentMonth/$currentDay/${currentDate.year}"

        view.date.text = dateText

        view.backButton.setOnClickListener {
            viewModel.selectList.clear()
            parentFragmentManager.popBackStackImmediate()

            context?.let {
                image?.removeImage(it)
                image = null
            }
        }

        view.cameraButton.setOnClickListener {
            getImage(Sources.CAMERA) {
                view.visitImage.setText(image?.second.toString())
                view.imageCancelButton.visibility = View.VISIBLE
            }
        }

        view.albumButton.setOnClickListener {
            getImage(Sources.GALLERY) {
                view.visitImage.setText(image?.second.toString())
                view.imageCancelButton.visibility = View.VISIBLE
            }
        }

        view.purposeRadioGroup.setOnCheckedChangeListener { _, i ->
            if (i == R.id.other) {
                view.visitPurpose.visibility = View.VISIBLE
            } else {
                view.visitPurpose.visibility = View.GONE
            }

        }

        view.imageCancelButton.setOnClickListener {
            context?.let {
                image?.removeImage(it)
                image = null
            }
            view.visitImage.text.clear()
            view.imageCancelButton.visibility = View.INVISIBLE

        }

        view.writeButton.setOnClickListener {
            if ((view.findViewById(purposeRadioGroup.checkedRadioButtonId) as RadioButton).text.toString() == "기타" &&
                view.visitPurpose.text.isBlank()) {
                Toast.makeText(context, "항목을 모두 채워주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val date = "${currentDate.year}/$currentMonth/$currentDay"
            view.progressBar.visibility = View.VISIBLE

            var purpose = (view.findViewById(purposeRadioGroup.checkedRadioButtonId) as RadioButton).text.toString()
            if(!view.visitPurpose.text.isBlank()) {
                purpose += " (${view.visitPurpose.text})"
            }

            monitoringViewModel.addData(
                Monitoring(
                    viewModel.selectList[adapter.selectVisitPlace].key,
                    date, view.writerEditText.text.toString(),
                    image?.let { SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date()) } ?: "",
                    viewModel.selectList[adapter.selectVisitPlace].name,
                    purpose,
                    (view.findViewById(radioGroup.checkedRadioButtonId) as RadioButton).text.toString(),
                    view.remark.text.toString(), false, (view.findViewById(visitorRadioGroup.checkedRadioButtonId) as RadioButton).text.toString()
                ), image?.second

            ) {
                clearView(view)
                viewModel.selectList.removeAt(adapter.selectVisitPlace)
                adapter.notifyDataSetChanged()

                view.progressBar.visibility = View.GONE

                if (viewModel.selectList.size == 0) {
                    parentFragmentManager.popBackStack()

                    val fragment = MonitoringFragment()
                    val transaction = parentFragmentManager.beginTransaction()

                    transaction.replace(R.id.fragmentContainer, fragment).commit()
                    parentFragmentManager.popBackStack()
                }


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

    private fun clearView(view: View) {
        context?.let {
            image?.removeImage(it)
            image = null
        }

        view.visitPurpose.text.clear()
        view.visitImage.text.clear()
        view.remark.text.clear()
        view.radioGroup.check(R.id.stateComplete)
        view.purposeRadioGroup.check(R.id.lunchBox)

    }

    override fun onDestroy() {
        disposeBag.dispose()
        super.onDestroy()
    }

}
