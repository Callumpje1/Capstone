package com.example.localsapp.model

import com.google.android.gms.maps.model.LatLng

data class Place(

    val title: String?,

    val address: String?,

    val imageUrl: String?,

    val favourite: Boolean?,

    val LatLng: LatLng?,

    var id: String?

) {
    constructor() : this(null, null, null, null, null, null)
}
