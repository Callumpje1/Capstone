package com.example.localsapp.ui.favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.localsapp.R
import com.example.localsapp.databinding.ItemLocationBinding
import com.example.localsapp.model.Place

class FavouritesAdapter(
    private val locations: List<Place>,
    private val updateFavourite: (favourite: Boolean, id: String) -> Unit
) :
    RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemLocationBinding.bind(itemView)
        fun dataBind(place: Place) {
            Glide
                .with(itemView)
                .load(
                    String.format(
                        IMAGE_URL,
                        place.imageUrl,
                        itemView.context.getString(
                            R.string.api_key
                        )
                    )
                )
                .into(binding.ivPhoto)
            binding.tvTitle.text = place.title
            binding.tvAddress.text = place.address
            binding.ivFavourite.setOnClickListener {
                if (place.favourite == false) {
                    updateFavourite(true, place.id!!)
                    binding.ivFavourite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                } else {
                    updateFavourite(false, place.id!!)
                    binding.ivFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
            }
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
        holder.dataBind(locations[position])
    }

    companion object {
        const val IMAGE_URL =
            "https://maps.googleapis.com/maps/api/place/photo?photoreference=%s&sensor=false&maxheight=300&maxwidth=600&key=%s"
    }
}
