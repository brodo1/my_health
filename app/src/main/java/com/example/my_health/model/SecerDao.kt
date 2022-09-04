package com.example.my_health.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface SecerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) //ako se npr ubaci isti id onda se zamjeni
    suspend fun insert( secer: Secer)

    @Query("SELECT * FROM secer_table")
    fun selectAll(): LiveData<List<Secer>>

    @Query("DELETE FROM secer_table WHERE X= :x ")
    suspend fun deleteSecer(x:Long)

    @Query("DELETE FROM secer_table")
    suspend fun deleteAll()
}