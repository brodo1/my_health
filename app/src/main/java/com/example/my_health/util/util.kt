package com.example.my_health.util

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.my_health.model.ReminderDataBase

val DB_NAME="reminderDb"

fun buildDB(context: Context):ReminderDataBase{
    val db=Room.databaseBuilder(context,ReminderDataBase::class.java, DB_NAME).build()
    return db
}



