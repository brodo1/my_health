package com.example.my_health.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.my_health.R
import com.example.my_health.databinding.FragmentCreateReminderListBinding
import com.example.my_health.model.Reminder
import com.example.my_health.util.ButtonAddReminderClickListener
import com.example.my_health.util.NotificationHelper
import com.example.my_health.util.ReminderWorker
import com.example.my_health.viewmodel.DetailsReminderViewModel
import com.example.my_health.viewmodel.ReminderListViewModel
import kotlinx.android.synthetic.main.fragment_create_reminder_list.*
import java.util.concurrent.TimeUnit


class CreateReminderListFragment : Fragment(),ButtonAddReminderClickListener {

    private lateinit var viewModel: DetailsReminderViewModel
    private lateinit var dataBinding: FragmentCreateReminderListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_create_reminder_list,container,false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=ViewModelProvider(this).get(DetailsReminderViewModel::class.java)
        //napravit prazni reminder
        dataBinding.reminder = Reminder("","",0,0)
        dataBinding.listener=this
       /* btnAdd.setOnClickListener {

            val reminder= Reminder(txtTitle.text.toString(),txtDescription.text.toString())
            viewModel.addReminder(listOf(reminder))

            val myWorkRequest= OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(30, TimeUnit.SECONDS)
                .setInputData(workDataOf("title" to txtTitle.text.toString(),
                    "message" to txtDescription.text.toString())).build() // u reminderWorker klasi pristupa se input.data i ovdje se postavlja taj input

            WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)

            Navigation.findNavController(it).popBackStack()
        }

        */

    }

    override fun onButtonAddReminder(v: View) {

        viewModel.addReminder(listOf(dataBinding.reminder!!))

        val myWorkRequest= OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(30, TimeUnit.SECONDS)
            .setInputData(workDataOf("title" to txtTitle.text.toString(),
                "message" to txtDescription.text.toString())).build() // u reminderWorker klasi pristupa se input.data i ovdje se postavlja taj input

        WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)

        Navigation.findNavController(v).popBackStack()
    }


}