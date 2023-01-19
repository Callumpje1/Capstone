package com.example.localsapp.ui.spots

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.localsapp.data.PlaceRepository
import com.example.localsapp.model.Place
import kotlinx.coroutines.launch

class SpotsViewModel(application: Application) : AndroidViewModel(application) {

    private val placeRepository: PlaceRepository = PlaceRepository()

    val places: LiveData<MutableList<Place>> = placeRepository.places

    private val _errorText: MutableLiveData<String> = MutableLiveData()

    /**
     * Get places
     * returns a List of all places
     */
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

    /**
     * Get favourites
     * returns a List of all favourite places
     */
    fun getFavourites() {
        viewModelScope.launch {
            try {
                placeRepository.getFavourites()
            } catch (ex: PlaceRepository.PlaceRetrievalError) {
                val errorMsg = "Something went wrong while retrieving this place."
                _errorText.value = errorMsg
            }
        }
    }

    /**
     * Update favourites
     * Updates the favourite field for given place id
     */
    fun updateFavourites(
        favourite: Boolean,
        id: String
    ) {
        viewModelScope.launch {
            try {
                placeRepository.updateFavourites(favourite, id)
            } catch (ex: PlaceRepository.PlaceSaveError) {
                val errorMsg = "Something went wrong while saving this place."
                _errorText.value = errorMsg
            }
        }
    }

    /**
     * Add place
     * Add place to Places collection, add randomUUID if id null
     */
    fun addPlace(
        title: String?,
        address: String?,
        imageUrl: String?,
        favourite: Boolean,
        id: String?
    ) {
        val place =
            Place(title, address, imageUrl.toString(), favourite, id)
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