package com.example.my_health.util

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.my_health.model.ReminderDataBase

val DB_NAME="reminderDb"

/*
val MIGRATION_1_2= object: Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE reminder_table ADD COLUMN  )
    }
}

fun buildDB(context: Context):ReminderDataBase{
    val db=Room.databaseBuilder(context,
        ReminderDataBase::class.java, DB_NAME)
        .addMigrations().build()
    return db
}

*/

