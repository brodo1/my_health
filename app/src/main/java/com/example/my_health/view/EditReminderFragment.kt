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
import com.example.my_health.R
import com.example.my_health.databinding.FragmentEditReminderListBinding
import com.example.my_health.model.Reminder
import com.example.my_health.util.ReminderSaveChangesListener
import com.example.my_health.viewmodel.DetailsReminderViewModel


class EditReminderFragment : Fragment(), ReminderSaveChangesListener{

    private lateinit var viewModel: DetailsReminderViewModel
    private lateinit var dataBinding:FragmentEditReminderListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding=DataBindingUtil.inflate<FragmentEditReminderListBinding>(inflater,
            R.layout.fragment_edit_reminder_list,container,false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProvider(this).get(DetailsReminderViewModel::class.java)

        val uuid=EditReminderFragmentArgs.fromBundle(requireArguments()).uuid //u edit fragment u nav grafu se uveo argument uuid i ovako se uzme uuid od tog remindera
        viewModel.grab(uuid)  //reminderLD iz viewmodela postaje reminder uuid reminder
        /*
        btnAdd.setOnClickListener {
            viewModel.update(
                txtTitle.text.toString(),
                txtDescription.text.toString(),
                uuid)
            Navigation.findNavController(it).popBackStack()
        }

         */
        dataBinding.listener=this

        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.reminderLD.observe(viewLifecycleOwner, Observer {
            dataBinding.reminder=it //reminder u layoutu postaje reminderLD

       // txtTitle.setText(it.title) //uzme od remindera odabranog sa grab naslov i description
         //   txtDescription.setText(it.description)
        })
    }

    override fun onReminderSaveChanges(v: View, reminder: Reminder) {
        viewModel.update(reminder.title,reminder.description,reminder.uuid)
        Log.d("update",reminder.toString())
        Navigation.findNavController(v).popBackStack()
    }
}