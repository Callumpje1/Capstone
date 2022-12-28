package com.example.localsapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.localsapp.model.Place
import com.google.android.libraries.places.api.Places

class PlaceRepository(context: Context) {

    private var placeDao: PlaceDao

    init {
        val placeRoomDatabase = PlaceRoomDatabase.getDatabase(context)
        placeDao = placeRoomDatabase!!.PlaceDao()
    }

    fun getAllReminders(): LiveData<List<Places>> {
        return placeDao.getAllPlaces()
    }

    suspend fun insertReminder(place: Place) {
        placeDao.insertPlace(place)
    }

    suspend fun deleteReminder(place: Place) {
        placeDao.deletePlace(place)
    }

    suspend fun updateReminder(place: Place) {
        placeDao.updatePlace(place)
    }
}