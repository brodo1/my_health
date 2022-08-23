package com.example.my_health.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.my_health.R
import com.example.my_health.model.Reminder
import com.example.my_health.viewmodel.DetailsReminderViewModel
import com.example.my_health.viewmodel.ReminderListViewModel
import kotlinx.android.synthetic.main.fragment_create_reminder_list.*


class CreateReminderListFragment : Fragment() {

    private lateinit var viewModel: DetailsReminderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_reminder_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=ViewModelProvider(this).get(DetailsReminderViewModel::class.java)

        btnAdd.setOnClickListener {
            val reminder= Reminder(txtTitle.text.toString(),txtDescription.text.toString())
            viewModel.addReminder(listOf(reminder))
            Navigation.findNavController(it).popBackStack()
        }

    }


}