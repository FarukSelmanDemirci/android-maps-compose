package com.google.maps.android.compose.theme

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE

@Dao
interface LocationDao {

    @Insert(onConflict=REPLACE)
    suspend fun insertAll(vararg locations: LocationEntity)


    @Delete
    suspend fun delete(location: LocationEntity)

    @Query("SELECT * FROM locationEntity")
    fun getAll(): List<LocationEntity>

    @Query("DELETE FROM locationEntity WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * FROM locationEntity WHERE durum = :durum")
    fun getLocationsByDurum(durum: Boolean): LiveData<List<LocationEntity>>
}
