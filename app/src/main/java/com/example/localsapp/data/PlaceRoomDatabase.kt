package com.example.localsapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.localsapp.model.Place

@Database(entities = [Place::class], version = 1, exportSchema = false)
abstract class PlaceRoomDatabase : RoomDatabase() {

    abstract fun PlaceDao(): PlaceDao

    companion object {
        private const val DATABASE_NAME = "PLACE_DATABASE"

        @Volatile
        private var placeRoomDatabase: PlaceRoomDatabase? = null

        fun getDatabase(context: Context): PlaceRoomDatabase? {
            if (placeRoomDatabase == null) {
                synchronized(PlaceRoomDatabase::class.java) {
                    if (placeRoomDatabase == null) {
                        placeRoomDatabase = Room.databaseBuilder(
                            context.applicationContext,
                            PlaceRoomDatabase::class.java, DATABASE_NAME
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return placeRoomDatabase
        }
    }

}