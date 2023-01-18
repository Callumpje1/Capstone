package com.example.localsapp.ui.spots

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.localsapp.R
import com.example.localsapp.databinding.FragmentSpotsBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class SpotsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var locationResult: LocationResult

    private lateinit var locationRequest: LocationRequest

    private var _binding: FragmentSpotsBinding? = null

    private val binding get() = _binding!!

    private var mapView: MapView? = null

    private var map: GoogleMap? = null

    private var lastLocation: Location? = null

    private var locationCallback: LocationCallback? = null

    private var currLocationMarker: Marker? = null

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val spotsViewModel: SpotsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpotsBinding.inflate(inflater, container, false)
        val view = inflater.inflate(R.layout.fragment_spots, container, false)
        _binding!!.root.addView(view)
        // Gets the MapView from the XML layout and creates it
        mapView = view?.findViewById<View>(R.id.mapview) as MapView
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync(this)

        binding.btnAdd.setOnClickListener {
            openAutoCompleteDialog()
        }
        return binding.root
    }

    private fun openAutoCompleteDialog() {
        val dialogLayout = layoutInflater.inflate(R.layout.fragment_add_location_dialog, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogLayout).show()

        setupPlacesAutoComplete()

        dialogLayout.findViewById<Button>(R.id.ok_button).setOnClickListener {
            Toast.makeText(requireContext(), "Location added", Toast.LENGTH_SHORT).show()
            builder.hide()
        }
        dialogLayout.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            builder.hide()
        }
    }

    private fun setupPlacesAutoComplete() {
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.PHOTO_METADATAS,
            )
        )

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                spotsViewModel.addPlace(
                    place.name,
                    place.address,
                    place.photoMetadatas?.get(0).toString(),
                    false,
                    place.id,
                )
            }

            override fun onError(status: Status) {
                Toast.makeText(requireContext(), "Location could not be added", Toast.LENGTH_SHORT)
                    .show()
                Log.i(TAG, status.statusMessage.toString())
            }
        })
        autocompleteFragment.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        locationResult = LocationResult()

        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient?.requestLocationUpdates(
                locationRequest, locationCallback!!, Looper.myLooper()
            )
            map!!.isMyLocationEnabled = true
        } else {
            checkLocationPermission()
        }

        val locationList = locationResult.locations
        if (locationList.isNotEmpty()) {
            val location = locationList.last()
            lastLocation = location
            if (currLocationMarker != null) {
                currLocationMarker?.remove()
            }
            val latLng = LatLng(location.latitude, location.longitude)
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title("Current Position")
            currLocationMarker = map!!.addMarker(markerOptions)

            map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0F))
        }

    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(requireContext()).setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION
                        )
                    }.create().show()

            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient?.requestLocationUpdates(
            locationRequest, locationCallback!!, null /* Looper */
        )
    }

    // stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient?.removeLocationUpdates(locationCallback!!)
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView!!.onDestroy()
    }

    companion object {
        const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }
}