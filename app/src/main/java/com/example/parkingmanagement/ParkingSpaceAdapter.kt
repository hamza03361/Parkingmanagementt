package com.example.parkingmanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ParkingSpaceAdapter(private val parkingSpaces: List<Pair<Int, Int>>) :
    RecyclerView.Adapter<ParkingSpaceAdapter.ParkingSpaceViewHolder>() {

    class ParkingSpaceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val parkingImageView: ImageView = view.findViewById(R.id.parkingImageView)
        val secondParkingImageView: ImageView = view.findViewById(R.id.secondparkingImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingSpaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booked, parent, false) // Make sure this matches your layout file
        return ParkingSpaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParkingSpaceViewHolder, position: Int) {
        val (firstImageRes, secondImageRes) = parkingSpaces[position]
        holder.parkingImageView.setImageResource(firstImageRes)
        holder.secondParkingImageView.setImageResource(secondImageRes)
    }

    override fun getItemCount(): Int = parkingSpaces.size
}
