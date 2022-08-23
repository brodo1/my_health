package com.example.my_health.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.my_health.model.Reminder
import com.example.my_health.model.ReminderDataBase
import com.example.my_health.util.buildDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ReminderListViewModel(application: Application):AndroidViewModel(application),CoroutineScope {
    val reminderLD=MutableLiveData<List<Reminder>>()

    private var job= Job()  //job osigurava da sve sto se napise unutar launch funkcije bude u drugom threadu

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main //main thread , stavi se job unutar main threada da bi se imao pristup UI-u

    fun refresh(){
        launch {
            val db= buildDB(getApplication())
            reminderLD.value=db.reminderDao().selectAllReminders()
        }//kad se pozove refresh fja live data se updejta
    }

    fun clearReminder(reminder: Reminder){
        launch {
            val db= buildDB(getApplication())
            db.reminderDao().deleteReminder(reminder)
            reminderLD.value=db.reminderDao().selectAllReminders()
        }

    }
}