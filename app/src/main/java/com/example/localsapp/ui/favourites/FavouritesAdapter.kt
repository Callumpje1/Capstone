package com.example.localsapp.ui.favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.localsapp.R
import com.example.localsapp.databinding.ItemLocationBinding
import com.example.localsapp.model.Location

class FavouritesAdapter(private val locations: List<Location>) :
    RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemLocationBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.dataBind(reminders[position])
    }
}
