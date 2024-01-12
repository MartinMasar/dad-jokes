package com.example.dadjokes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.adapters.ViewBindingAdapter.setClickListener
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
        viewModel = ViewModelProvider(this, JokesViewModelFactory(app.repository))
            .get(MainViewModel::class.java)

        //val supplierList = generateData()
        adapter = JokeAdapter()
        recyclerView.adapter = adapter

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.jokeList.observe(this, Observer { jokes ->
            // Update the RecyclerView adapter with the new data
            adapter.submitList(jokes)
        })

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
                //TODO: warning
                //no more than 3 characters
            }
            else{
                viewModel.getJokeByWord(searchText)
                /*while (viewModel.jokesResponseValue.value?.current_page ?: 0 < viewModel.jokesResponseValue.value?.next_page ?: 0 && viewModel.jokesResponseValue.value?.current_page ?: 0 < 5){
                    viewModel.getJokeByWordAndPage(searchText, viewModel.jokesResponseValue.value?.next_page ?: 0)
                }*/
                //adapter = BeerSupplierAdapter(supplierList)
                //recyclerView.adapter = adapter
            }
        }
    }

    fun convertToDadJokes(jokesResponse: JokesResponse): List<DadJokes> {
        return jokesResponse.results.map { DadJokes(it.joke) }
    }

    fun convertToDadJokes(jokeItem: JokeItem): DadJokes {
        return DadJokes(jokeItem.joke)
    }

}