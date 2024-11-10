package com.example.parkingmanagement

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.core.widget.NestedScrollView

class Guestpersonaldetails : ComponentActivity(){

    private lateinit var backarrow : ImageView
    lateinit var nextbutton : Button
    private lateinit var nestedScrollView: NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guestpersonaldetails)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = getColor(R.color.colorPrimaryDark) // Change to your color resource
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        nestedScrollView=findViewById(R.id.nestedscrollview)
        nestedScrollView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            nestedScrollView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = nestedScrollView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard is open, scroll to the Next button
                nestedScrollView.scrollTo(0, nextbutton.bottom)
            } else {
                // Keyboard is closed, handle any other actions if necessary
            }
        }

        backarrow = findViewById(R.id.smallImageView)
        backarrow.setOnClickListener{
            val intent = Intent(this, Homeactivity::class.java)
            startActivity(intent)
        }
        nextbutton = findViewById(R.id.nextbutton)
        nextbutton.setOnClickListener{
            val intent = Intent(this, Guesttimedetails::class.java)
            startActivity(intent)
        }
    }
}