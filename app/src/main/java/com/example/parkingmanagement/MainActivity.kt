package com.example.parkingmanagement

import android.os.Bundle
import android.content.Intent
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.LeadingMarginSpan
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        FirebaseApp.initializeApp(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = getColor(R.color.colorPrimaryDark) // Change to your color resource
        }

        val centerTextView: TextView = findViewById(R.id.centerTextView)
        val text = "Find The Best Parking\nDestination For Your Car"
        val spannableString = SpannableString(text)

        // Color definition
        val color = ContextCompat.getColor(this, R.color.coloraccent) // Ensure this color is defined in your colors.xml

        // Apply color spans
        spannableString.setSpan(
            ForegroundColorSpan(color),
            0, 4, // Apply color to "Find"
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            ForegroundColorSpan(color),
            14, 21, // Apply color to "The Best Parking"
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        // Apply leading margin to the first line only
        spannableString.setSpan(
            LeadingMarginSpan.Standard(40, 0), // Adjust the first value to set the margin
            0,
            text.indexOf('\n'), // End of the first line
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        centerTextView.text = spannableString

        // Set up the button click listener
        val getStartedButton: Button = findViewById(R.id.getstartedbutton)
        getStartedButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
