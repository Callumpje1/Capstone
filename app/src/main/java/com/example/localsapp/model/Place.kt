package com.example.localsapp.model

data class Place(

    val placeTitle: String,

    val placeDescription: String,

    val placeImage: String,

    val latitude: Double,

    val longitude: Double,

    var id: Long? = null

)
