package com.example.localsapp.ui.favourites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.localsapp.databinding.FragmentFavouritesBinding
import com.example.localsapp.model.Place
import com.example.localsapp.ui.spots.SpotsActivity
import com.example.localsapp.ui.spots.SpotsViewModel

class FavouritesFragment : Fragment() {

    private val spotsViewModel: SpotsViewModel by activityViewModels()

    private val places = arrayListOf<Place>()

    private val favouritesAdapter = FavouritesAdapter(places){ favourite, id ->
        spotsViewModel.updateFavourites(favourite, id)
    }
    private var _binding: FragmentFavouritesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)

        observePlaces(viewLifecycleOwner)

        spotsViewModel.getFavourites()

        return binding.root
    }

    /**
     * Observe places for changes and update
     * @param viewLifeCycleOwner
     */
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
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recyclerView.adapter = favouritesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}