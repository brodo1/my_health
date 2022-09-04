package com.example.my_health.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.my_health.model.Reminder
import com.example.my_health.model.ReminderDataBase
import kotlinx.coroutines.*

import kotlin.coroutines.CoroutineContext

class DetailsReminderViewModel(application: Application):AndroidViewModel(application){

    val reminderLD = MutableLiveData<Reminder>()
    val db= ReminderDataBase.getDataBase(getApplication())

    fun grab(uuid:Int){
        viewModelScope.launch {

            reminderLD.value=db.reminderDao().selectReminder(uuid)

        }
    }


    fun addReminder(reminder: Reminder){
        viewModelScope.launch(Dispatchers.IO){
            db.reminderDao().insertAll(reminder) //list prebacuje elemente liste u pojedinacne reminder objekte i tako se ubacuje vise parametara odjednom

        }

    }
    fun update(title:String,description:String,uuid: Int,date:String,time:String,workRequestID:String){
        viewModelScope.launch(Dispatchers.IO) {
            db.reminderDao().update(title,description,uuid,date,time,workRequestID)

        }
    }



}