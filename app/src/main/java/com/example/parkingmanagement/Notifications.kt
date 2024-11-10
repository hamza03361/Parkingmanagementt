package com.example.parkingmanagement

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Notifications : ComponentActivity(){

    private lateinit var backarrow : ImageView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var gestureDetector: GestureDetector
    private lateinit var notificationsAdapter: NotificationsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notifications)
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

        val notifications = mutableListOf(
            Notification(R.drawable.ic_notification, "New message from John", "10:30 AM"),
            Notification(R.drawable.ic_notification, "Meeting reminder", "9:00 AM"),
            Notification(R.drawable.ic_notification, "Update available", "Yesterday")
        )

        backarrow = findViewById(R.id.crossImageView)
        backarrow.setOnClickListener{
            val intent = Intent(this, Homeactivity::class.java)
            startActivity(intent)
        }

        // Set up RecyclerView and Adapter
        val recyclerView: RecyclerView = findViewById(R.id.notificationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        notificationsAdapter = NotificationsAdapter(notifications)
        recyclerView.adapter = notificationsAdapter

        // Set up ItemTouchHelper for swipe-to-dismiss functionality
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Remove the swiped notification from the list
                val position = viewHolder.adapterPosition
                notifications.removeAt(position)
                notificationsAdapter.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.selectedItemId = R.id.navigation_notifications

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
                    navigateToActivity(Parkme::class.java)
                    true
                }
                R.id.navigation_notifications -> {
                    // Live menu item clicked
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
        val intent = Intent(this, Parkme::class.java) // Adjust as needed
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun onSwipeLeft() {
        val intent = Intent(this, Profile::class.java) // Adjust as needed
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


}