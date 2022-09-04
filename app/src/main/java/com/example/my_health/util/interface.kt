package com.example.my_health.util

import android.view.View
import com.example.my_health.model.Reminder


interface ReminderEditClickListener{
    fun onReminderEditClick(v:View)
}

interface ReminderDeleteClickListener{
    fun onReminderDeleteClick(reminder: Reminder)
}

interface ReminderSaveChangesListener{
    fun onReminderSaveChanges(v:View,reminder:Reminder)
}

interface DeleteAll{
    fun onDeleteAllClick(v:View)
}

interface ButtonAddClickListener{
    fun onButtonAdd(v:View)
}

interface DateClickListener{
    fun onDateClick(v:View)
}

interface TimeClickListener{
    fun onTimeClick(v:View)
}

