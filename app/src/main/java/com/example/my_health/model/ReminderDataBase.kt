package com.example.my_health.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Reminder::class), version = 2)
abstract class ReminderDataBase:RoomDatabase() {
    abstract fun reminderDao(): ReminderDao //pristup dao funkcijama

    companion object{
        @Volatile private var INSTANCE: ReminderDataBase ?= null //volatile moze se koristit s drugim thredovima
        private val LOCK=Any() //osigurava da postoji samo jedna instanca baze

        fun getDataBase(context: Context): ReminderDataBase {
            synchronized(this){
                    var instance= INSTANCE
                    if(instance==null){
                        instance=Room.databaseBuilder(context.applicationContext,
                            ReminderDataBase::class.java,
                            "reminderDb").fallbackToDestructiveMigration().build()
                        INSTANCE=instance
                    }
            return instance
            }

        }


    }
}