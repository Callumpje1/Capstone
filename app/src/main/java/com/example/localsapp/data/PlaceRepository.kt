package com.example.localsapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.localsapp.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*

class PlaceRepository {
    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _places: MutableLiveData<MutableList<Place>> = MutableLiveData()

    val places: LiveData<MutableList<Place>>
        get() = _places

    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * Get places
     * returns a List of all places
     */
    fun getPlaces() {
        try {
            fireStore.collection("Places")
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

    /**
     * Get favourites
     * returns a List of all favourite places
     */
    fun getFavourites() {
        try {
            fireStore.collection("Places")
                .whereEqualTo("favourite", true)
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

    /**
     * Update favourites
     * Updates the favourite field for given place id
     */
    suspend fun updateFavourites(favourite: Boolean, id: String) {
        try {
            fireStore.collection("Places")
                .document(id)
                .update("favourite", favourite)
                .await()
            _createSuccess.value = true

        } catch (e: Exception) {
            throw PlaceSaveError(e.message.toString(), e)
        }
    }

    /**
     * Add place
     * Add place to Places collection, add randomUUID if id null
     */
    suspend fun addPlace(place: Place) {
        try {
            fireStore.collection("Places")
                .document(place.id ?: UUID.randomUUID().toString())
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