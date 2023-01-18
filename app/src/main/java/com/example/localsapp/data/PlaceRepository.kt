package com.example.localsapp.data

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.localsapp.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*

class PlaceRepository {
    private var fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val collection = fireStore.collection("Places")

    private val _places: MutableLiveData<MutableList<Place>> = MutableLiveData()

    val places: LiveData<MutableList<Place>>
        get() = _places

    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    fun getPlaces() {
        try {
            collection
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

    fun getFavourites() {
        try {
            collection
                .whereEqualTo("favourite", true)
                .get().addOnSuccessListener {
                    val list = mutableListOf<Place>()
                    for (document in it) {
                        Log.i(TAG, "$document")
                        list.add(document.toObject(Place::class.java))
                    }
                    _places.value = list
                }
        } catch (e: Exception) {
            throw PlaceRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    suspend fun updateFavourites(favourite: Boolean, id: String) {
        try {
            collection
                .document(id)
                .update("favourite", favourite)
                .await()
            _createSuccess.value = true
            val place = _places.value?.find {
                it.id == id
            }
            place?.favourite = favourite

        } catch (e: Exception) {
            throw PlaceSaveError(e.message.toString(), e)
        }
    }

    suspend fun addPlace(place: Place) {
        try {
            collection.document(place.id ?: UUID.randomUUID().toString())
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