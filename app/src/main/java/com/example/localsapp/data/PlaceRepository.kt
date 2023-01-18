package com.example.localsapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.localsapp.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import java.util.UUID

class PlaceRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var placeDocument =
        firestore.collection("Places")

    private val _places: MutableLiveData<MutableList<Place>> = MutableLiveData()

    val places: LiveData<MutableList<Place>>
        get() = _places

    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    fun getPlaces() {
        try {
            firestore.collection("Places")
                .get().addOnSuccessListener {
                    val list = mutableListOf<Place>()
                    for (document in it) {
                        list.add(document.toObject(Place::class.java))
                    }
                    _places.value = list
                }
        } catch (e: Exception) {
            throw PlaceRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    suspend fun updateFavourites(place: Place){
        try {
            firestore.collection("Places")
                .document("Favourites")
        }catch (e: Exception) {
            throw PlaceSaveError(e.message.toString(), e)
        }
    }

    suspend fun addPlace(place: Place) {
        try {
            firestore.collection("Places").document(place.id ?: UUID.randomUUID().toString())
                .set(place).await()
            _createSuccess.value = true
            _places.value?.add(place)


        } catch (e: Exception) {
            throw PlaceSaveError(e.message.toString(), e)
        }
    }

    class PlaceSaveError(message: String, cause: Throwable) : Exception(message, cause)
    class PlaceRetrievalError(message: String) : Exception(message)
}