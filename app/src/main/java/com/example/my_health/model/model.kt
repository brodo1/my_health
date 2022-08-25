package com.example.my_health.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName="reminder_table")
data class Reminder(
    @ColumnInfo(name = "title")
    var title:String,
    @ColumnInfo(name="description")
    var description:String,
    @ColumnInfo(name="date")
    var date:String,
    @ColumnInfo(name = "time")
    var time:String,
    @ColumnInfo(name="workRequestID")
    var workRequestID:String

){
    @PrimaryKey(autoGenerate = true)
    var uuid:Int=0
}