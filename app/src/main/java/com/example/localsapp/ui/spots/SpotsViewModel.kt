package com.example.localsapp.ui.spots

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.localsapp.data.PlaceRepository
import com.example.localsapp.model.Place
import kotlinx.coroutines.launch

class SpotsViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "FIRESTORE"
    private val placeRepository: PlaceRepository = PlaceRepository()

    val place: LiveData<Place> = placeRepository.place

    val createSuccess: LiveData<Boolean> = placeRepository.createSuccess

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText

    fun getPlace() {
        viewModelScope.launch {
            try {
                placeRepository.getPlace()
            } catch (ex: PlaceRepository.PlaceRetrievalError) {
                val errorMsg = "Something went wrong while retrieving this place.\n" +
                        "It could be that you still need to install your own google-services.json file from Firestore."
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }

    fun addPlace(title:String,description:String,image:String,latitude:Double,longitude:Double,id:Long) {
        // persist data to firestore
        val place = Place(title,description,image,latitude,longitude,id)
        viewModelScope.launch {
            try {
                placeRepository.addPlace(place)
            } catch (ex: PlaceRepository.PlaceSaveError) {
                val errorMsg = "Something went wrong while saving this place.\n" +
                        "It could be that you still need to install your own google-services.json file from Firestore."
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }
}