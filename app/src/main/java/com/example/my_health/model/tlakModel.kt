package com.example.my_health.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jjoe64.graphview.series.DataPoint

@Entity(tableName="tlak_table")
data class Tlak(
    @ColumnInfo(name = "X")
    var X:Long,
    @ColumnInfo(name = "gornjiY")
    var gornjiY:Double,
    @ColumnInfo(name = "donjiY")
    var donjiY:Double
){
    @PrimaryKey(autoGenerate = true)
    var uuid:Int=0
}