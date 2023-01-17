package com.example.localsapp

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.localsapp.databinding.ActivityMainBinding
import com.example.localsapp.ui.spots.SpotsViewModel
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private val AUTOCOMPLETE_REQUEST_CODE = 1

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        FirebaseFirestore.setLoggingEnabled(true)
//        FirebaseApp.initializeApp(this)

        if(!Places.isInitialized()){
            Places.initialize(this,getString(R.string.api_key))
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            when (resultCode) {
//                Activity.RESULT_OK -> {
//                    data?.let {
//                        val place = Autocomplete.getPlaceFromIntent(data)
//                        Log.i(TAG, "Place: ${place.name}, ${place.id}")
//                    }
//                }
//                AutocompleteActivity.RESULT_ERROR -> {
//                    // TODO: Handle the error.
//                    data?.let {
//                        val status = Autocomplete.getStatusFromIntent(data)
//                        Log.i(TAG, status.statusMessage ?: "")
//                    }
//                }
//                Activity.RESULT_CANCELED -> {
//                    // The user canceled the operation.
//                }
//            }
//            return
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }


}