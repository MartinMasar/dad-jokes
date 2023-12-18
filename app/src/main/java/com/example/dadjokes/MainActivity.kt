package com.example.dadjokes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dadjokes.model.DadJokes

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JokeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val supplierList = generateData()
        adapter = JokeAdapter(supplierList)
        recyclerView.adapter = adapter
    }

    private fun generateData(): List<DadJokes> {
        val suppliers = mutableListOf<DadJokes>()
        for (i in 1..1000) {
            suppliers.add(DadJokes("Dad joke $i"))
        }
        return suppliers
    }
}