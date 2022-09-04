package com.example.my_health.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TlakDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert( tlak: Tlak)

    @Query("SELECT * FROM tlak_table")
    suspend fun selectAllTlak():List<Tlak>

    @Query("SELECT * FROM tlak_table")
    fun selectAll():LiveData<List<Tlak>>


    @Query("DELETE FROM tlak_table WHERE X= :x ")
    suspend fun deleteTlak(x:Long)

    @Query("DELETE FROM tlak_table")
    suspend fun deleteAll()

}

