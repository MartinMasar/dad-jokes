package com.example.dadjokes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dadjokes.model.DadJokes

class JokeAdapter(private val supplierList: List<DadJokes>) :
    RecyclerView.Adapter<JokeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dad_joke, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val supplier = supplierList[position]
        holder.supplierNameTextView.text = supplier.joke
        //holder.supplierDescriptionTextView.text = supplier.description
    }

    override fun getItemCount(): Int {
        return supplierList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val supplierNameTextView: TextView = itemView.findViewById(R.id.textJoke)
        //val supplierDescriptionTextView: TextView = itemView.findViewById(R.id.supplierDescription)
    }
}