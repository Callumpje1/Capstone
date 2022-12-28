package com.example.localsapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.localsapp.model.Place
import com.google.android.libraries.places.api.Places

@Dao
interface PlaceDao {

    @Query("SELECT * FROM placeTable")
    fun getAllPlaces(): LiveData<List<Places>>

    @Insert
    suspend fun insertPlace(place: Place)

    @Delete
    suspend fun deletePlace(place: Place)

    @Update
    suspend fun updatePlace(place: Place)

}