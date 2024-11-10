package com.example.parkingmanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AvailableAdapter(private val parkingSpaces: List<Pair<String, String>>) :
    RecyclerView.Adapter<AvailableAdapter.ParkingSpaceViewHolder>() {

    class ParkingSpaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val parkingTextView: TextView = view.findViewById(R.id.parkingtextView)
        val secondParkingTextView: TextView = view.findViewById(R.id.secondparkingtextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingSpaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_available, parent, false) // Make sure this matches your layout file
        return ParkingSpaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParkingSpaceViewHolder, position: Int) {
        val (firstText, secondText) = parkingSpaces[position]
        holder.parkingTextView.text = firstText
        holder.secondParkingTextView.text = secondText
    }

    override fun getItemCount(): Int = parkingSpaces.size
}
