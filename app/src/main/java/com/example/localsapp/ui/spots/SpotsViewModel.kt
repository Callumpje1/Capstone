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

    private val placeRepository: PlaceRepository = PlaceRepository()

    val places: LiveData<MutableList<Place>> = placeRepository.places

    private val _errorText: MutableLiveData<String> = MutableLiveData()

    fun getAllPlaces() {
        viewModelScope.launch {
            try {
                placeRepository.getPlaces()
            } catch (ex: PlaceRepository.PlaceRetrievalError) {
                val errorMsg = "Something went wrong while retrieving this place."
                _errorText.value = errorMsg
            }
        }
    }

    fun getFavourites(){
        viewModelScope.launch {
            try {
                placeRepository.getFavourites()
            }catch (ex: PlaceRepository.PlaceRetrievalError) {
                val errorMsg = "Something went wrong while retrieving this place."
                _errorText.value = errorMsg
            }
        }
    }

    fun updateFavourites(
        favourite: Boolean,
        id: String
    ) {
        viewModelScope.launch {
            try {
                placeRepository.updateFavourites(favourite,id)
            } catch (ex: PlaceRepository.PlaceSaveError) {
                val errorMsg = "Something went wrong while saving this place."
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
            Place(title, address, imageUrl.toString(), favourite, latLng, id)
        viewModelScope.launch {
            try {
                placeRepository.addPlace(place)
            } catch (ex: PlaceRepository.PlaceSaveError) {
                val errorMsg = "Something went wrong while saving this place."
                _errorText.value = errorMsg
            }
        }
    }
}