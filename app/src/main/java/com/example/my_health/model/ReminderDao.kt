package com.example.my_health.model

import androidx.room.*

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) //ako se npr ubaci isti id onda se zamjeni
    suspend fun insertAll(vararg reminder: Reminder) //vararg-> vise objekata reminder

    @Query("SELECT * FROM reminder_table")
    suspend fun selectAllReminders():List<Reminder>

    @Query("SELECT * FROM reminder_table WHERE uuid= :id") //uuid poprima vrijednost iz zagrade(id:Int)
    suspend fun selectReminder(id:Int):Reminder

    @Query("UPDATE reminder_table SET title= :title, description=:description WHERE uuid=:uuId")
    suspend fun update(title:String,description:String,uuId:Int)


    @Delete
    suspend fun deleteReminder(reminder: Reminder)
}