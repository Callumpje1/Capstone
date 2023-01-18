package com.example.localsapp.ui.favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
                place.favourite = !place.favourite!!
                updateFavourite(place.favourite!!, place.id!!)
                if (place.favourite!!) {
                    Toast.makeText(it.context, "Added to favourites", Toast.LENGTH_SHORT)
                        .show()
                    binding.ivFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    Toast.makeText(it.context, "Removed from favourites", Toast.LENGTH_SHORT)
                        .show()
                    binding.ivFavourite.setImageResource(
                        R.drawable.ic_baseline_favorite_border_24
                    )
                }
            }

            if (place.favourite!!) binding.ivFavourite.setImageResource(R.drawable.ic_baseline_favorite_24) else binding.ivFavourite.setImageResource(
                R.drawable.ic_baseline_favorite_border_24
            )
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
            "https://maps.googleapis.com/maps/api/place/photo?photoreference=%s&sensor=false&maxheight=1000&maxwidth=1000&key=%s"
    }
}
