package com.example.my_health.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="reminder_table")
data class Reminder(
    @ColumnInfo(name = "title")
    var title:String,
    @ColumnInfo(name="description")
    var description:String,
    @ColumnInfo(name="reminder_date")
    var reminder_date:Int,
    @ColumnInfo(name = "reminder_time")
    var reminder_time:Int

){
    @PrimaryKey(autoGenerate = true)
    var uuid:Int=0
}