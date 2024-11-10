package com.example.parkingmanagement

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Parkme : ComponentActivity(){

    private lateinit var backarrow : ImageView
    private lateinit var confirm : Button
    private lateinit var available : Button
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var gestureDetector: GestureDetector


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.parkme)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = getColor(R.color.colorPrimaryDark) // Change to your color resource
        }

        // Initialize GestureDetector and set it to the root view
        val rootView = findViewById<View>(R.id.parentview) // Ensure root_view ID is set in XML
        gestureDetector = GestureDetector(this, GestureListener())
        rootView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }

        confirm = findViewById(R.id.confirmbutton)
        confirm.setOnClickListener{
            val intent = Intent(this, Bookingdone::class.java)
            startActivity(intent)
        }

        backarrow = findViewById(R.id.crossImageView)
        backarrow.setOnClickListener{
            val intent = Intent(this, Homeactivity::class.java)
            startActivity(intent)
        }

        available = findViewById(R.id.availableButton)
        available.setOnClickListener{
            val intent = Intent(this, Available::class.java)
            startActivity(intent)
        }

        val parkingSpaces = listOf(
            Pair(R.drawable.firstcolumsbooked, R.drawable.secondcolombooked),
            Pair(R.drawable.firstcolumsbooked, R.drawable.secondcolombooked),
            Pair(R.drawable.firstcolumsbooked, R.drawable.secondcolombooked),
            Pair(R.drawable.firstcolumsbooked, R.drawable.secondcolombooked),
            Pair(R.drawable.firstcolumsbooked, R.drawable.secondcolombooked),
            Pair(R.drawable.firstcolumsbooked, R.drawable.secondcolombooked),
        )

        // Set up RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.parkingRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 1) // 2 columns for grid layout
        recyclerView.adapter = ParkingSpaceAdapter(parkingSpaces)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.selectedItemId = R.id.navigation_booked

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Home menu item clicked
                    navigateToActivity(Homeactivity::class.java)
                    true
                }
                R.id.navigation_booked -> {
                    // Matches menu item clicked, stay on the same activity
                    // No need to start Matches activity again
                    true
                }
                R.id.navigation_notifications -> {
                    // Live menu item clicked
                    navigateToActivity(Notifications::class.java)
                    true
                }
                R.id.navigation_profile -> {
                    // Statistics menu item clicked
                    navigateToActivity(Profile::class.java)
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToActivity(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = e2?.x?.minus(e1?.x ?: 0f) ?: 0f
            val diffY = e2?.y?.minus(e1?.y ?: 0f) ?: 0f
            return if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight()
                    } else {
                        onSwipeLeft()
                    }
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
    }

    private fun onSwipeRight() {
        val intent = Intent(this, Homeactivity::class.java) // Adjust as needed
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun onSwipeLeft() {
        val intent = Intent(this, Notification::class.java) // Adjust as needed
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


}