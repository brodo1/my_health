package com.example.my_health.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Reminder::class,Tlak::class,Secer::class), version = 8,exportSchema = false)
abstract class ReminderDataBase:RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
    abstract fun tlakDao():TlakDao
    abstract fun secerDao():SecerDao

    companion object{
        @Volatile private var INSTANCE: ReminderDataBase ?= null

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