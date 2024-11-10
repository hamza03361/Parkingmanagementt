package com.example.parkingmanagement

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity

class Bookingdone : ComponentActivity(){

    lateinit var cross : ImageView
    lateinit var done : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookingdone)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = getColor(R.color.colorPrimaryDark) // Change to your color resource
        }

        cross = findViewById(R.id.crossImageView)
        cross.setOnClickListener{
            val intent = Intent(this, Homeactivity::class.java)
            startActivity(intent)
        }

        done = findViewById(R.id.belowButton)
        done.setOnClickListener{
            val intent = Intent(this, Homeactivity::class.java)
            startActivity(intent)
        }

    }
}