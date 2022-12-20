package com.example.localsapp.ui.home

import android.icu.lang.UCharacter.VerticalOrientation
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.localsapp.databinding.FragmentHomeBinding
import com.example.localsapp.model.Location

class HomeFragment : Fragment() {


    private val locations = arrayListOf<Location>()
    private val homeAdapter = HomeAdapter(locations)


    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateRv()

        initViews()
    }

    private fun populateRv() {
        locations.add(Location("hello"))
        locations.add(Location("hello"))
        locations.add(Location("hello"))
        locations.add(Location("hello"))
        locations.add(Location("hello"))
        locations.add(Location("hello"))
        locations.add(Location("hello"))
        locations.add(Location("hello"))

        homeAdapter.notifyDataSetChanged()
    }

    private fun initViews() {
        binding.rvCafes.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvCafes.adapter = homeAdapter

        binding.rvRestaurants.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvRestaurants.adapter = homeAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}