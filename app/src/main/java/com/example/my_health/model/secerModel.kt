package com.example.my_health.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="secer_table")
data class Secer(
    @ColumnInfo(name = "X")
    var X:Long,
    @ColumnInfo(name = "Y")
    var Y:Double

){
    @PrimaryKey(autoGenerate = true)
    var uuid:Int=0
}