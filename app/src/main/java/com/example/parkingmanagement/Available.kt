package com.example.parkingmanagement

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Available : ComponentActivity() {

    private lateinit var backarrow : ImageView
    lateinit var confirm : Button
    lateinit var booked : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.available)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = getColor(R.color.colorPrimaryDark) // Change to your color resource
        }
        backarrow = findViewById(R.id.crossImageView)
        backarrow.setOnClickListener{
            val intent = Intent(this, Homeactivity::class.java)
            startActivity(intent)
        }
        confirm = findViewById(R.id.confirmbutton)
        confirm.setOnClickListener{
            val intent = Intent(this, Bookingdone::class.java)
            startActivity(intent)
        }

        booked = findViewById(R.id.bookedButton)
        booked.setOnClickListener{
            val intent = Intent(this, Parkme::class.java)
            startActivity(intent)
        }

        // Define text data for the TextViews
        val parkingSpaces = listOf(
            Pair("01", "02"),
            Pair("03", "04"),
            Pair("05", "06")
        )

        // Find RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.availableRecyclerView)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AvailableAdapter(parkingSpaces)
    }
}
