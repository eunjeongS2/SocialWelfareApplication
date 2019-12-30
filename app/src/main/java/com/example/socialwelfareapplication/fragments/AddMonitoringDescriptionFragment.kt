package com.example.socialwelfareapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.socialwelfareapplication.R
import com.example.socialwelfareapplication.adapters.ContactItemListAdapter
import com.example.socialwelfareapplication.checkDate
import com.example.socialwelfareapplication.models.Monitoring
import com.example.socialwelfareapplication.models.auth
import com.example.socialwelfareapplication.viewmodels.MonitoringViewModel
import com.example.socialwelfareapplication.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.fragment_add_monitoring_description.*
import kotlinx.android.synthetic.main.fragment_add_monitoring_description.view.*
import org.threeten.bp.LocalDate

class AddMonitoringDescriptionFragment(private val viewModel: UserViewModel) : Fragment() {

    private lateinit var adapter: ContactItemListAdapter
    private lateinit var monitoringViewModel: MonitoringViewModel


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
        }

        view.writeButton.setOnClickListener {
            val date = "${currentDate.year}/$currentMonth/$currentDay"

            monitoringViewModel.addData(
                Monitoring(
                    viewModel.selectList[adapter.selectVisitPlace].phoneNumber,
                    date, auth.currentUser?.uid ?: "",
                    visitImage.toString(),
                    viewModel.selectList[adapter.selectVisitPlace].name, visitPurpose.toString(),
                    (view.findViewById(radioGroup.checkedRadioButtonId) as RadioButton).text.toString(),
                    remark.toString(), 0
                )
            ) {

                viewModel.selectList.removeAt(adapter.selectVisitPlace)
                adapter.notifyDataSetChanged()

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

}
