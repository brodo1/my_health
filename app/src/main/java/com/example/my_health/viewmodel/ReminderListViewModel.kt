package com.example.my_health.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.example.my_health.model.Reminder
import com.example.my_health.model.ReminderDataBase
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class ReminderListViewModel(application: Application):AndroidViewModel(application){
    val reminderLD=MutableLiveData<List<Reminder>>()
    val db=ReminderDataBase.getDataBase(getApplication())

    fun refresh(){
        viewModelScope.launch {
            reminderLD.value=db.reminderDao().selectAllReminders() //list<reminders>
        }
    }

    fun clearReminder(reminder: Reminder){
        viewModelScope.launch{
            if(reminder.workRequestID.length>0) {
                    val sameUuid: UUID = UUID.fromString((reminder.workRequestID))
                    WorkManager.getInstance(getApplication()).cancelWorkById(sameUuid)
                }

            withContext(Dispatchers.IO) {
                db.reminderDao().deleteReminder(reminder)
            }
            reminderLD.value=db.reminderDao().selectAllReminders()

        }

    }
}