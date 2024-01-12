package com.example.dadjokes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dadjokes.databinding.ItemDadJokeBinding
import com.example.dadjokes.model.DadJokes

class JokeAdapter : ListAdapter<DadJokes, JokeAdapter.ViewHolder>(DadJokesDiffCallback()) {

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
