package com.example.dadjokes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dadjokes.databinding.ActivitySecondBinding
import com.example.dadjokes.model.DadJokes
import com.example.dadjokes.model.JokeItem
import com.example.dadjokes.model.JokesResponse

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JokeAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = binding.recyclerviewsave
        recyclerView.layoutManager = LinearLayoutManager(this)

        val app = application as MyApplication
        viewModel = ViewModelProvider(this, JokesViewModelFactory(app.apiRepository, app.databaseRepository))
            .get(MainViewModel::class.java)

        adapter = JokeAdapter(viewModel)
        recyclerView.adapter = adapter

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.getAllSavedJokes()

        viewModel.favoriteJokeList.observe(this, Observer { jokes ->
            adapter.submitList(jokes)

        })


        setClickListener()
    }

    private fun setClickListener() {
        binding.buttonHome.setOnClickListener {
            // Create an Intent to start the FavoriteActivity (replace FavoriteActivity with the actual name of your second activity)
            val intent = Intent(this@SecondActivity, MainActivity::class.java)
            // Add any extra data if needed
            // intent.putExtra("key", "value")
            // Start the FavoriteActivity
            startActivity(intent)
        }

        binding.buttonSearch.setOnClickListener {
            val searchTerm = binding.editTextText.text.toString()
            viewModel.getSavedJokeByWord(searchTerm)
        }
    }
}