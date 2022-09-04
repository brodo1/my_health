package com.example.my_health.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.my_health.model.ReminderDataBase
import com.example.my_health.model.Secer

import kotlinx.coroutines.launch

class SecerViewModel(application: Application): AndroidViewModel(application) {
    val secerLD= MutableLiveData<List<Secer>>()

    val db= ReminderDataBase.getDataBase(getApplication())

    fun insert(secer: Secer){
        viewModelScope.launch {
            db.secerDao().insert(secer)
        }
    }

    val seceri=db.secerDao().selectAll()

    fun deletesecer(x:Long){
        viewModelScope.launch {
            db.secerDao().deleteSecer(x)
        }
    }


    fun deleteAll(){
        viewModelScope.launch{
            db.secerDao().deleteAll()
        }
    }


}