package com.example.my_health.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Reminder::class), version = 1)
abstract class ReminderDataBase:RoomDatabase() {
    abstract fun reminderDao(): ReminderDao //pristup dao funkcijama

    companion object{
        @Volatile private var instance: ReminderDataBase ?= null //volatile moze se koristit s drugim thredovima
        private val LOCK=Any() //osigurava da postoji samo jedna instanca baze

        private fun buildDataBase(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            ReminderDataBase::class.java,
            "reminderDb"
        ).build()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){ //ako je instance null onda locka i napravi instancu
            instance ?: buildDataBase(context).also {
                instance=it
            }
        }
    }
}