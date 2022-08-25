package com.example.my_health.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkManager
import com.example.my_health.model.Reminder
import com.example.my_health.model.ReminderDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class ReminderListViewModel(application: Application):AndroidViewModel(application),CoroutineScope {
    val reminderLD=MutableLiveData<List<Reminder>>()

    private var job= Job()  //job osigurava da sve sto se napise unutar launch funkcije bude u drugom threadu

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main //main thread , stavi se job unutar main threada da bi se imao pristup UI-u

    fun refresh(){
        launch {
            val db=ReminderDataBase.getDataBase(getApplication())
            //val db= buildDB(getApplication())
            reminderLD.value=db.reminderDao().selectAllReminders() //list<reminders>
        }//kad se pozove refresh fja live data se updejta
    }

    fun clearReminder(reminder: Reminder){
        launch {
            //val db= buildDB(getApplication())
            val db=ReminderDataBase.getDataBase(getApplication())
            db.reminderDao().deleteReminder(reminder)
            if(reminder.workRequestID.length>0) {
                val sameUuid: UUID = UUID.fromString((reminder.workRequestID))
                WorkManager.getInstance(getApplication()).cancelWorkById(sameUuid)
            }
            reminderLD.value=db.reminderDao().selectAllReminders()
        }

    }
}