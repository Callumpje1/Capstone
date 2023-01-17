package com.example.localsapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.localsapp.R
import com.example.localsapp.databinding.ItemLocationBinding
import com.example.localsapp.model.Place

class HomeAdapter(private val locations: List<Place>, private val clickListener: (Place) -> Unit) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemLocationBinding.bind(itemView)

        fun dataBind(place: Place, clickListener: (Place) -> Unit) {
            binding.textView2.text = place.title
            binding.textView3.text = place.address
            binding.cvItem.setOnClickListener { clickListener(place) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_location, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(locations[position], clickListener)
    }
}