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
        firestore.collection("Places").document("Place")

    private val _places: MutableLiveData<List<Place>> = MutableLiveData()

    val places: LiveData<List<Place>>
        get() = _places

    //the CreateQuizFragment can use this to see if creation succeeded
    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    suspend fun getPlaces() {
        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
                val data = firestore.collection("Places")
                    .get().addOnSuccessListener {
                        val list = mutableListOf<Place>()
                        for (document in it) {
                            list.add(document.toObject(Place::class.java))
                        }

                        _places.value = list
                    }
            }
        } catch (e: Exception) {
            throw PlaceRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    suspend fun addPlace(place: Place) {
        // persist data to firestore
        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
                firestore.collection("Places").document(place.id ?: UUID.randomUUID().toString())
                    .set(place).await()

                _createSuccess.value = true
            }

        } catch (e: Exception) {
            throw PlaceSaveError(e.message.toString(), e)
        }
    }

    class PlaceSaveError(message: String, cause: Throwable) : Exception(message, cause)
    class PlaceRetrievalError(message: String) : Exception(message)
}