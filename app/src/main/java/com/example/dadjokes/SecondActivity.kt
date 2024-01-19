package com.example.dadjokes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dadjokes.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JokeAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout and set it as the content view
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize RecyclerView and its layout manager
        recyclerView = binding.recyclerviewsave
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize ViewModel using the JokesViewModelFactory
        val app = application as MyApplication
        viewModel = ViewModelProvider(this, JokesViewModelFactory(app.apiRepository, app.databaseRepository))
            .get(MainViewModel::class.java)

        // Initialize RecyclerView adapter
        adapter = JokeAdapter(viewModel)
        recyclerView.adapter = adapter

        // Set up data binding
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Observe LiveData for favorite joke list updates
        viewModel.favoriteJokeList.observe(this, Observer { jokes ->
            adapter.submitList(jokes)
        })

        viewModel.getAllSavedJokes()

        setClickListener()
    }

    private fun setClickListener() {
        binding.buttonHome.setOnClickListener {
            // Navigate back to the main activity
            val intent = Intent(this@SecondActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        binding.buttonSearch.setOnClickListener {
            // Search for saved jokes based on the entered search term
            val searchTerm = binding.editTextText.text.toString()
            viewModel.getSavedJokeByWord(searchTerm)
        }
    }
}