package com.example.my_health.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.format.DateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.my_health.R
import com.example.my_health.databinding.FragmentCreateReminderListBinding
import com.example.my_health.model.Reminder
import com.example.my_health.util.*
import com.example.my_health.viewmodel.DetailsReminderViewModel
import com.example.my_health.viewmodel.ReminderListViewModel
import kotlinx.android.synthetic.main.fragment_create_reminder_list.*
import kotlinx.android.synthetic.main.fragment_create_reminder_list.view.*

import java.util.*
import java.util.concurrent.TimeUnit


class CreateReminderListFragment : Fragment(),ButtonAddReminderClickListener,
    DateClickListener,TimeClickListener, DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    var year=0
    var month=0
    var day=0
    var hour=0
    var minute=0
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
        dataBinding.reminder = Reminder("","","",  "","")
        dataBinding.listener=this
        dataBinding.listenerDate=this
        dataBinding.listenerTime=this
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

        if(dataBinding.reminder!!.title.length>0 ) {
            val calendar=Calendar.getInstance()
            val today=Calendar.getInstance()
            calendar.set(year,month,day,hour,minute)
            //dijeli se sa 1000 da se prebaci u sekunde, L long
            //pretpostavlja se da se odabire datum u buducnosti
            //razlika u sekundama se postavlja kao vrijeme koje mora proci da se trigera notifikacija -setinitialdelay
            val razlika=(calendar.timeInMillis/1000L)-(today.timeInMillis/1000L)

            if(dataBinding.reminder!!.date.length>0 && dataBinding.reminder!!.time.length>0) {
                val myWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInitialDelay(razlika, TimeUnit.SECONDS)
                    .setInputData(
                        workDataOf(
                            "title" to txtTitle.text.toString(),
                            "message" to txtDescription.text.toString()
                        )
                    )
                    .build() // u reminderWorker klasi pristupa se input.data i ovdje se postavlja taj input

                val workrequestId = myWorkRequest.id.toString()
                dataBinding.reminder!!.workRequestID = workrequestId
                WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)
            }
            viewModel.addReminder(listOf(dataBinding.reminder!!))

            Navigation.findNavController(v).popBackStack()
        }
        else{
            Toast.makeText(context,"Unesi naziv!",Toast.LENGTH_SHORT).show()
        }
    }
//ove dvije su funkcije iz interfacea koje su databindeane, kad se stisne izbacuje dijalog za odabir datuma
    override fun onDateClick(v: View) {
        val calendar= Calendar.getInstance()
        val year=calendar.get(Calendar.YEAR)
        val month=calendar.get(Calendar.MONTH)
        val day=calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker= DatePickerDialog(requireActivity(),this,year,month,day)// activity?.let { DatePickerDialog(it....
        datePicker.datePicker.minDate=System.currentTimeMillis() - 1000
        datePicker.show()
    }

    override fun onTimeClick(v: View) {
        val calendar= Calendar.getInstance()
        val hour=calendar.get(Calendar.HOUR_OF_DAY)
        val minute=calendar.get(Calendar.MINUTE)
        Log.d("onClickCreate","time clicked")
        TimePickerDialog(activity,this,hour,minute,DateFormat.is24HourFormat(activity)).show()
    }

    //ove dvije su dialozi, overrieadne metode datepickerdialog.onDateSetListener
    @SuppressLint("SetTextI18n") //ovo je samo da se ne žuti setText
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {

        Calendar.getInstance().let{
            it.set(year,month,day) //02-02-2022
            dataBinding.txtDatum.setText(day.toString().padStart(2,'0')+"-"
                    +(month+1).toString().padStart(2,'0')+"-"+year)
            this.year=year
            this.day=day
            this.month=month
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        val calendar= Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,hour)
        calendar.set(Calendar.MINUTE,minute)
        if(calendar.timeInMillis>=System.currentTimeMillis()) {
            dataBinding.txtVrijeme.setText(
                hour.toString().padStart(2, '0')
                        + ":" + minute.toString().padStart(2, '0')
            )
            this.hour = hour
            this.minute = minute
        }
        else{
            Toast.makeText(context,"Neispravno vrijeme!",Toast.LENGTH_LONG).show()
        }
    }


}