package com.example.localsapp.ui.home

import android.os.Bundle
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
import com.example.localsapp.ui.favourites.FavouritesAdapter
import com.example.localsapp.ui.spots.SpotsViewModel

class HomeFragment : Fragment() {

    private val spotsViewModel: SpotsViewModel by activityViewModels()

    private val places = arrayListOf<Place>()

    private val favouritesAdapter = FavouritesAdapter(places){ favourite, id ->
        spotsViewModel.updateFavourites(favourite, id)
    }

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        observePlaces(viewLifecycleOwner)

        spotsViewModel.getAllPlaces()

        return binding.root
    }

    private fun observePlaces(viewLifeCycleOwner: LifecycleOwner) {
        spotsViewModel.places.observe(viewLifeCycleOwner) { place ->
            places.clear()
            places.addAll(place)
            favouritesAdapter.notifyDataSetChanged()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        binding.rvRestaurants.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvRestaurants.adapter = favouritesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}