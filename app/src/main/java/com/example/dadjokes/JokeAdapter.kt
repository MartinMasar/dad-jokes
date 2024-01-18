package com.example.dadjokes

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dadjokes.databinding.ItemDadJokeBinding
import com.example.dadjokes.model.DadJokes

class JokeAdapter(private val viewModel: MainViewModel) : ListAdapter<DadJokes, JokeAdapter.ViewHolder>(DadJokesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDadJokeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val joke = getItem(position)
        holder.bind(joke)
    }

    inner class ViewHolder(private val binding: ItemDadJokeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(joke: DadJokes) {
            binding.textJoke.text = joke.joke

            val starButton = binding.buttonStar

            if (viewModel.isJokeSaved(joke)) {
                // Change star color to yellow
                val tintColor = Color.parseColor("#FFFF00")
                starButton.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
            } else {
                // Keep star color as white
                starButton.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            }

            starButton.setOnClickListener {
                /*if (joke.isFavorite) {
                    ContextCompat.getColor(itemView.context, R.color.yellow)  // Change to yellow when it is a favorite
                } else {
                    starButton.setColorFilter(Color.WHITE)   // Change to white when it is not a favorite
                }*/
                val tintColor = Color.parseColor("#FFFF00")

                starButton.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
                viewModel.addToFavorites(joke)

            }
        }
    }
}

class DadJokesDiffCallback : DiffUtil.ItemCallback<DadJokes>() {
    override fun areItemsTheSame(oldItem: DadJokes, newItem: DadJokes): Boolean {
        return oldItem.joke == newItem.joke
    }

    override fun areContentsTheSame(oldItem: DadJokes, newItem: DadJokes): Boolean {
        return oldItem == newItem
    }
}
