package com.example.localsapp.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.localsapp.databinding.FragmentHomeBinding
import com.example.localsapp.model.Place
import com.example.localsapp.ui.spots.SpotsViewModel

class HomeFragment : Fragment() {

    companion object {
        const val IMAGE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=%s&key=%s"
    }

    private val spotsViewModel: SpotsViewModel by activityViewModels()

    private val locations = arrayListOf<Place>()

    private val homeAdapter = HomeAdapter(locations)

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        observePlaces(viewLifecycleOwner)
        return binding.root
    }

    private fun observePlaces(viewLifeCycleOwner: LifecycleOwner) {
        spotsViewModel.getPlaces()

        spotsViewModel.places.observe(viewLifeCycleOwner) { place ->
            Log.i(TAG, "$place")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        binding.rvCafes.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvCafes.adapter = homeAdapter

        binding.rvRestaurants.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        binding.rvRestaurants.adapter = homeAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}