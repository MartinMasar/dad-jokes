package com.example.dadjokes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dadjokes.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JokeAdapter
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout and set it as the content view
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize RecyclerView and its layout manager
        recyclerView = binding.recyclerView
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

        // Observe LiveData for joke list updates
        viewModel.jokeList.observe(this, Observer { jokes ->
            adapter.submitList(jokes)
        })

        viewModel.getAllSavedJokes()

        setClickListener()
    }

    private fun setClickListener() {
        binding.buttonSearch.setOnClickListener {
            val searchText = binding.editTextText.text.toString()
            // Call the ViewModel method to fetch data based on the search text
            if(searchText == ""){
                viewModel.getRandomJoke()
            }
            else if(searchText.length < 3){
                // Show a warning dialog for short search text
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage("Warning: There must be none orat least 3 characters to search jokes")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                alertDialog.show()
            }
            else{
                viewModel.getJokeByWord(searchText)
            }
        }

        binding.buttonFavorite.setOnClickListener {
            // Create an Intent to start the FavoriteActivity
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        binding.buttonInfo.setOnClickListener {
            // Inflate the information dialog layout
            val infoDialogView = layoutInflater.inflate(R.layout.dialog_info, null)
            val infoBuilder = AlertDialog.Builder(this)
            infoBuilder.setView(infoDialogView)
            val infoDialog = infoBuilder.create()

            // Set a click listener for the close button in the information dialog
            infoDialogView.findViewById<AppCompatImageView>(R.id.imageView).setOnClickListener {
                infoDialog.dismiss()
            }

            // Set a click listener for the "Delete all favorites" button in the information dialog
            infoDialogView.findViewById<Button>(R.id.buttonDeleteData).setOnClickListener {
                val deleteDialogBuilder = AlertDialog.Builder(binding.root.context)
                    .setTitle("Delete all favorites")
                    .setMessage("Do you want to delete all favorite jokes?")
                    .setNegativeButton("No") { deleteDialog, _ ->
                        deleteDialog.dismiss()
                    }
                    .setPositiveButton("Yes") { deleteDialog, _ ->
                        // Perform the delete action here
                        viewModel.deleteAllJokes()
                        deleteDialog.dismiss()
                    }
                    .create()
                // Show the delete dialog
                deleteDialogBuilder.show()
            }
            // Show the information dialog
            infoDialog.show()
        }
    }
}