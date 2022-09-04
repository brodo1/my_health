package com.example.my_health.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_health.R
import com.example.my_health.databinding.FragmentEditReminderListBinding
import com.example.my_health.databinding.FragmentReminderListBinding
import com.example.my_health.model.Reminder
import com.example.my_health.viewmodel.ReminderListViewModel
import com.jjoe64.graphview.series.DataPoint

//import kotlinx.android.synthetic.main.fragment_reminder_list.*


class ReminderListFragment : Fragment() {

    private lateinit var viewModel: ReminderListViewModel
    private val reminderListAdapter: ReminderAdapter= ReminderAdapter(arrayListOf(),{item->doClick(item)})
    private lateinit var dataBinding:FragmentReminderListBinding

    fun doClick(item:Any){
        viewModel.clearReminder(item as Reminder)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding= DataBindingUtil.inflate<FragmentReminderListBinding>(inflater,
            R.layout.fragment_reminder_list,container,false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=ViewModelProvider(this).get(ReminderListViewModel::class.java)
        viewModel.refresh()
        dataBinding.recReminderList.layoutManager=LinearLayoutManager(context)
        dataBinding.recReminderList.adapter=reminderListAdapter

        dataBinding.fabAdd.setOnClickListener {
            val action= ReminderListFragmentDirections.actionCreateRemind()
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()
    }


    fun observeViewModel(){
        viewModel.reminderLD.observe(viewLifecycleOwner, Observer {
            reminderListAdapter.updateReminderList(it)


        })
    }

}