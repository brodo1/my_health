package com.example.my_health.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.my_health.model.Reminder
import com.example.my_health.model.ReminderDataBase
import com.example.my_health.model.Tlak
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TlakViewModel(application: Application): AndroidViewModel(application) {
    val tlakLD=MutableLiveData<List<Tlak>>()


    val db= ReminderDataBase.getDataBase(getApplication())

    fun insert(tlak: Tlak){
        viewModelScope.launch {
            db.tlakDao().insert(tlak)
        }
    }


    val tlakovi=db.tlakDao().selectAll()

    fun deleteTlak(x:Long){
        viewModelScope.launch {
            db.tlakDao().deleteTlak(x)
        }
    }


    fun deleteAll(){
        viewModelScope.launch{
            db.tlakDao().deleteAll()
        }
    }






}