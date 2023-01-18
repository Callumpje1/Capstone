package com.example.localsapp.ui.spots

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.localsapp.data.PlaceRepository
import com.example.localsapp.model.Place
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class SpotsViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "FIRESTORE"

    private val placeRepository: PlaceRepository = PlaceRepository()

    val places: LiveData<MutableList<Place>> = placeRepository.places

    val createSuccess: LiveData<Boolean> = placeRepository.createSuccess

    private val _errorText: MutableLiveData<String> = MutableLiveData()

    val errorText: LiveData<String>
        get() = _errorText

    fun getAllPlaces() {
        viewModelScope.launch {
            try {
                placeRepository.getPlaces()
            } catch (ex: PlaceRepository.PlaceRetrievalError) {
                val errorMsg = "Something went wrong while retrieving this place.\n" +
                        "It could be that you still need to install your own google-services.json file from Firestore."
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }

    fun updateFavourites(
        title: String?,
        address: String?,
        imageUrl: String?,
        favourite: Boolean,
        latLng: LatLng?,
        id: String?
    ) {
        val place =
            Place(title, address, imageUrl, favourite, latLng, id)
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


    fun addPlace(
        title: String?,
        address: String?,
        imageUrl: String?,
        favourite: Boolean,
        latLng: LatLng?,
        id: String?
    ) {
        val place =
            Place(title, address, imageUrl, favourite, latLng, id)
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