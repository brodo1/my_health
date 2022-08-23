package com.example.my_health.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.my_health.R
import com.example.my_health.viewmodel.DetailsReminderViewModel
import kotlinx.android.synthetic.main.fragment_create_reminder_list.*


class EditReminderFragment : Fragment() {

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
        viewModel= ViewModelProvider(this).get(DetailsReminderViewModel::class.java)

        val uuid=EditReminderFragmentArgs.fromBundle(requireArguments()).uuid //u edit fragment u nav grafu se uveo argument uuid i ovako se uzme uuid od tog remindera
        viewModel.fetch(uuid)  //reminderLD iz viewmodela postaje reminder uuid reminder
        txtViewTitle.text="Ažuriraj"
        btnAdd.text="Spremi"

        btnAdd.setOnClickListener {
            viewModel.update(
                txtTitle.text.toString(),
                txtDescription.text.toString(),
                uuid)
            Navigation.findNavController(it).popBackStack()
        }

        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.reminderLD.observe(viewLifecycleOwner, Observer {
            txtTitle.setText(it.title) //uzme od remindera odabranog sa fetch naslov i description
            txtDescription.setText(it.description)
        })
    }
}