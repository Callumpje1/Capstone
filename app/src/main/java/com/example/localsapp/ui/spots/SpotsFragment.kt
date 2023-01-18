package com.example.localsapp.ui.spots

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.example.localsapp.R
import com.example.localsapp.databinding.FragmentSpotsBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener


class SpotsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentSpotsBinding? = null

    private val binding get() = _binding!!

    private var mapView: MapView? = null

    private var map: GoogleMap? = null

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
        val builder = android.app.AlertDialog.Builder(requireContext()).setView(dialogLayout).show()

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

        val sydney = LatLng(52.2, 4.3)
        map!!.addMarker(
            MarkerOptions().position(sydney)
                .title("Marker in Sydney") // below line is use to add custom marker on our map.
        )

        map!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        map!!.uiSettings.isMyLocationButtonEnabled = true
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED) {
            return
        }
        map!!.isMyLocationEnabled = true
    }

    override fun onResume() {
        mapView!!.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

}