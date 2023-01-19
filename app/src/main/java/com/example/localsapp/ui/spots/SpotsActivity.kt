package com.example.localsapp.ui.spots

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.localsapp.MainActivity
import com.example.localsapp.R
import com.example.localsapp.databinding.ActivitySpotsBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class SpotsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPoiClickListener {

    private val spotsViewModel: SpotsViewModel by viewModels()

    private lateinit var binding: ActivitySpotsBinding

    private var map: GoogleMap? = null

    private lateinit var placesClient: PlacesClient

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)

    private var locationPermissionGranted = false

    private var lastKnownLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySpotsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        Places.initialize(applicationContext, getString(R.string.api_key))
        placesClient = Places.createClient(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.drawable.locals_logo_white)

        binding.btnAdd.setOnClickListener {
            openAutoCompleteDialog()
        }

    }

    /**
     * On options item selected
     * Intent for returning to MainActivity:class
     * @param item
     * @return Boolean
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivityForResult(Intent(applicationContext, MainActivity::class.java), 0)
        return true
    }

    /**
     * Open auto complete dialog
     */
    private fun openAutoCompleteDialog() {

        val dialogLayout = layoutInflater.inflate(R.layout.fragment_add_location_dialog, null)

        addPlacesAutoComplete()

        val builder = AlertDialog.Builder(this).setView(dialogLayout).show()

        dialogLayout.findViewById<Button>(R.id.ok_button).setOnClickListener {
            Toast.makeText(applicationContext, R.string.location_added, Toast.LENGTH_SHORT).show()
            startActivityForResult(Intent(applicationContext, SpotsActivity::class.java), 0)
            builder.hide()
        }
        dialogLayout.findViewById<Button>(R.id.cancel_button).setOnClickListener {
            Toast.makeText(applicationContext, R.string.location_not_added, Toast.LENGTH_SHORT).show()
            startActivityForResult(Intent(applicationContext, SpotsActivity::class.java), 0)
            builder.hide()
        }

    }

    /**
     * Add places auto complete
     * Create AutoCompleteSupportFragment for autocompleting place predictions
     * Adds place to SpotsViewModel
     */
    private fun addPlacesAutoComplete() {
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.PHOTO_METADATAS
            )
        )

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val metaData = place.photoMetadatas?.get(0).toString()
                spotsViewModel.addPlace(
                    place.name,
                    place.address,
                    metaData.substring(metaData.indexOf("photoReference="), metaData.indexOf("}"))
                        .replace("photoReference=", ""),
                    false,
                    place.id,
                )
            }

            override fun onError(status: Status) {
                Toast.makeText(
                    applicationContext, "Location could not be added", Toast.LENGTH_SHORT
                ).show()
                Log.i(TAG, status.statusMessage.toString())
            }
        })
    }


    /**
     * On map ready
     * Update Ui if user grants permission for location
     * @param map
     */
    override fun onMapReady(map: GoogleMap) {
        this.map = map

        map.setOnPoiClickListener(this)

        getLocationPermission()

        getDeviceLocation()

        updateLocationUI()

    }

    /**
     * Get device location
     * Check if locationPermission is granted and move camera accordingly
     */
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude, lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        map?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                defaultLocation, DEFAULT_ZOOM.toFloat()
                            )
                        )
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    /**
     * Get location permission
     * Change permissionGranted based on user input
     */
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    /**
     * On request permissions result
     * UpdateLocationUi if permissionResult is true
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    /**
     * Update location UI
     * Update map based on user input
     */
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    /**
     * On poi click
     * Clicklistener for POI(Point of Interest) click
     * @param poi
     */
    override fun onPoiClick(poi: PointOfInterest) {
        Toast.makeText(applicationContext, poi.name, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }
}