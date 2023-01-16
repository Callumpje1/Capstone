package com.example.localsapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.localsapp.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class PlaceRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var quizDocument =
        firestore.collection("Places").document("Place")

    private val _place: MutableLiveData<Place> = MutableLiveData()

    val place: LiveData<Place>
        get() = _place

    //the CreateQuizFragment can use this to see if creation succeeded
    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    suspend fun getPlace() {
        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
                val data = quizDocument
                    .get()
                    .await()

                val title = data.getString("Title").toString()
                val description = data.getString("Description").toString()
                val image = data.getString("Image").toString()
                val latitude = data.getDouble("Latitude")!!
                val longitude = data.getDouble("Longitude")!!
                val id = data.getLong("Id")!!

                _place.value = Place(title,description,image,latitude,longitude,id)
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
                quizDocument
                    .set(place)
                    .await()

                _createSuccess.value = true
            }

        } catch (e: Exception) {
            throw PlaceSaveError(e.message.toString(), e)
        }
    }

    class PlaceSaveError(message: String, cause: Throwable) : Exception(message, cause)
    class PlaceRetrievalError(message: String) : Exception(message)
}