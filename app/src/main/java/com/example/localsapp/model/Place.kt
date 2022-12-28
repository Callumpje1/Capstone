package com.example.localsapp.model

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "placeTable")
data class Place(

    @ColumnInfo(name = "title")
    var placeTitle: String,

    @ColumnInfo(name = "title")
    var placeDescription: String,

    @ColumnInfo(name = "title")
    var placeImage: Uri?,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null

)
