package com.example.my_health.model

import androidx.room.*

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll( reminder: Reminder)

    @Query("SELECT * FROM reminder_table")
    suspend fun selectAllReminders():List<Reminder>

    @Query("SELECT * FROM reminder_table WHERE uuid= :id")
    suspend fun selectReminder(id:Int):Reminder

    @Query("UPDATE reminder_table SET title= :title, description=:description, date=:date,time=:time,workRequestID=:workRequestID WHERE uuid=:uuId")
    suspend fun update(title:String,description:String,uuId:Int,date:String,time:String,workRequestID:String)


    @Delete
    suspend fun deleteReminder(reminder: Reminder)
}