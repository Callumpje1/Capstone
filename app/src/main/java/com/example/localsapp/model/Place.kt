package com.example.localsapp.model

data class Place(

    val title: String?,

    val address: String?,

    val imageUrl: String?,

    var favourite: Boolean?,

    var id: String?

) {
    constructor() : this(null, null, null,  null, null)
}
