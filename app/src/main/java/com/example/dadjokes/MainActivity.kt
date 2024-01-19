package com.example.dadjokes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dadjokes.databinding.ActivityMainBinding
import com.example.dadjokes.model.DadJokes
import androidx.lifecycle.ViewModelProvider
import com.example.dadjokes.model.JokeItem
import com.example.dadjokes.model.JokesResponse

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JokeAdapter
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        //binding.txtLabel.text = "Hello UTB"

        val app = application as MyApplication
        viewModel = ViewModelProvider(this, JokesViewModelFactory(app.apiRepository, app.databaseRepository))
            .get(MainViewModel::class.java)

        //val supplierList = generateData()
        adapter = JokeAdapter(viewModel)
        recyclerView.adapter = adapter

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.jokeList.observe(this, Observer { jokes ->
            // Update the RecyclerView adapter with the new data
            adapter.submitList(jokes)
        })

        viewModel.getAllSavedJokes()

        //?.jokeList?.observe(this,updateObserver)
        //observeViewModel()
        setClickListener()
    }

    /*var updateObserver: Observer<ArrayList<DadJokes>?> =
        Observer<ArrayList<DadJokes>?>(userArrayList ->
            jokeAdapter)*/


    /*private fun observeViewModel() {
        // Observe the LiveData in the ViewModel
        viewModel.jokesResponseValue.observe(this, Observer { jokesResponse ->
            // Update the RecyclerView adapter with the new data
            adapter.updateData(jokesResponse.results)
        })
    }*/

    private fun setClickListener() {
        // Set a click listener for the Search button
        binding.buttonSearch.setOnClickListener {
            // Get the text from the EditText
            val searchText = binding.editTextText.text.toString()
            // Call the ViewModel method to fetch data based on the search text
            if(searchText == ""){
                viewModel.getRandomJoke()
                //val dadJoke: DadJokes = convertToDadJokes(viewModel.jokes.value)
                //adapter = JokeAdapter()
                //recyclerView.adapter = adapter
            }
            else if(searchText.length < 3){
                //no more than 3 characters
                //Toast.makeText(this, "Warning: This is a warning message.", Toast.LENGTH_SHORT).show()
                val alertDialog = AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage("Warning: There must be none orat least 3 characters to search jokes")
                    .setPositiveButton("OK") { dialog, _ ->
                        // Handle the OK button click if needed
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
            // Create an Intent to start the FavoriteActivity (replace FavoriteActivity with the actual name of your second activity)
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
        binding.buttonInfo.setOnClickListener {
            val infoDialogView = layoutInflater.inflate(R.layout.dialog_info, null)
            val infoBuilder = AlertDialog.Builder(this)
            infoBuilder.setView(infoDialogView)
            val infoDialog = infoBuilder.create()

            // Set a click listener for the close button in the information dialog
            infoDialogView.findViewById<AppCompatImageView>(R.id.imageView).setOnClickListener {
                infoDialog.dismiss() // Close the information dialog when the close button is clicked
            }

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

    fun convertToDadJokes(jokesResponse: JokesResponse): List<DadJokes> {
        return jokesResponse.results.map { DadJokes(it.joke) }
    }

    fun convertToDadJokes(jokeItem: JokeItem): DadJokes {
        return DadJokes(jokeItem.joke)
    }

}