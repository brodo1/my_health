package com.example.my_health.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.my_health.model.Reminder
import com.example.my_health.model.ReminderDataBase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailsReminderViewModel(application: Application):AndroidViewModel(application),CoroutineScope {
   private var job= Job()
    val reminderLD = MutableLiveData<Reminder>()

    fun grab(uuid:Int){

        launch {
            val db= ReminderDataBase.getDataBase(getApplication())
            reminderLD.value=db.reminderDao().selectReminder(uuid)
        }
    }

    override val coroutineContext: CoroutineContext
        get()=job+Dispatchers.Main

    fun addReminder(list: List<Reminder>){
        launch {
            //val db= buildDB(getApplication())
            val db= ReminderDataBase.getDataBase(getApplication())
            db.reminderDao().insertAll(*list.toTypedArray()) //*list prebacuje elemente liste u pojedinacne reminder objekte i tako se ubacuje vise parametara odjednom


        }
    }

    fun update(title:String,description:String,uuid: Int,date:String,time:String,workRequestID:String){
        launch {
            val db= ReminderDataBase.getDataBase(getApplication())
            //val db= buildDB(getApplication())
            db.reminderDao().update(title,description,uuid,date,time,workRequestID)
        }
    }

}