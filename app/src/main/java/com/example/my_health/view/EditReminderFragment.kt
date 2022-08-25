package com.example.my_health.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.my_health.R
import com.example.my_health.databinding.FragmentEditReminderListBinding
import com.example.my_health.model.Reminder
import com.example.my_health.util.*
import com.example.my_health.viewmodel.DetailsReminderViewModel
import kotlinx.android.synthetic.main.fragment_create_reminder_list.*
import kotlinx.android.synthetic.main.fragment_create_reminder_list.txtDescription
import kotlinx.android.synthetic.main.fragment_create_reminder_list.txtTitle
import kotlinx.android.synthetic.main.fragment_edit_reminder_list.*
import java.util.*
import java.util.concurrent.TimeUnit


class EditReminderFragment : Fragment(), ReminderSaveChangesListener, DateClickListener,TimeClickListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    var year=0
    var month=0
    var day=0
    var hour=0
    var minute=0

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
        dataBinding.listenerDate=this
        dataBinding.listenerTime=this

        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.reminderLD.observe(viewLifecycleOwner, Observer {
            dataBinding.reminder=it //reminder u layoutu postaje reminderLD
            dataBinding.reminder!!.date=""
            dataBinding.reminder!!.time=""
            dataBinding.reminder!!.description=""
       // txtTitle.setText(it.title) //uzme od remindera odabranog sa grab naslov i description
         //   txtDescription.setText(it.description)
        })
    }

    override fun onReminderSaveChanges(v: View, reminder: Reminder) {
        if(reminder.workRequestID.length>0) {
            val sameUuid: UUID = UUID.fromString((reminder.workRequestID))
            WorkManager.getInstance(requireActivity().applicationContext).cancelWorkById(sameUuid)
        }

        if(reminder.date.length>0 && reminder.time.length>0) {
            val calendar = Calendar.getInstance()
            val today=Calendar.getInstance()
            calendar.set(year, month, day, hour, minute)

            //dijeli se sa 1000 da se prebaci u sekunde, L long
            //pretpostavlja se da se odabire datum u buducnosti
            //razlika u sekundama se postavlja kao vrijeme koje mora proci da se trigera notifikacija -setinitialdelay
            val razlika = (calendar.timeInMillis / 1000L) - (today.timeInMillis / 1000L)
            Log.d(
                "picked time",
                year.toString() + " " + month.toString() + " " + day.toString() + " " + hour.toString() + " " + minute.toString()
            )
            val myWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(razlika, TimeUnit.SECONDS)
                .setInputData(
                    workDataOf(
                        "title" to txtTitleEdit.text.toString(),
                        "message" to txtDescriptionEdit.text.toString()
                    )
                )
                .build() // u reminderWorker klasi pristupa se input.data i ovdje se postavlja taj input
            val workrequestId = myWorkRequest.id.toString()
            reminder.workRequestID= workrequestId
            WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)
        }
        viewModel.update(reminder.title,reminder.description,reminder.uuid,reminder.date,reminder.time,reminder.workRequestID)
        Navigation.findNavController(v).popBackStack()
    }



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


    override fun onDateClick(v: View) {
        val calendar= Calendar.getInstance()
        val year=calendar.get(Calendar.YEAR)
        val month=calendar.get(Calendar.MONTH)
        val day=calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker= DatePickerDialog(requireActivity(),this,year,month,day)
        datePicker.datePicker.minDate=System.currentTimeMillis() - 1000
        datePicker.show()
    }

    override fun onTimeClick(v: View) {
        val calendar= Calendar.getInstance()
        val hour=calendar.get(Calendar.HOUR_OF_DAY)
        val minute=calendar.get(Calendar.MINUTE)
        TimePickerDialog(activity,this,hour,minute,DateFormat.is24HourFormat(activity)).show()

    }

}