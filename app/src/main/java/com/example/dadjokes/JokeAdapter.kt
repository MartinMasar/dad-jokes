package com.example.dadjokes

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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

            // Deciding the star color while on display
            if (viewModel.isJokeSaved(joke)) {
                // Change star color to yellow
                starButton.setColorFilter(Color.parseColor("#FFFF00"), PorterDuff.Mode.SRC_IN)
            }
            else {
                // Change star color to white
                starButton.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN)
            }

            starButton.setOnClickListener {
                // Check if the joke is already saved as a favorite
                if (viewModel.isJokeSaved(joke)) {
                    // The joke is already saved, show a confirmation dialog for removing
                    val alertDialog = AlertDialog.Builder(binding.root.context)
                        .setTitle("Delete favorite")
                        .setMessage("Do you want to remove this joke from favorite?")
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("Yes") { dialog, _ ->
                            starButton.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
                            viewModel.removeFromFavorites(joke)
                            dialog.dismiss()
                        }
                        .create()
                    alertDialog.show()
                } else {
                    // The joke is not saved, mark it as a favorite and update UI
                    viewModel.addToFavorites(joke)
                    starButton.setColorFilter(Color.parseColor("#FFFF00"), PorterDuff.Mode.SRC_IN)
                }
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
