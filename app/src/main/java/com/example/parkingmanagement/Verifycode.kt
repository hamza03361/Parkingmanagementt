package com.example.parkingmanagement

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.widget.NestedScrollView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthCredential

class Verifycode : ComponentActivity() {

    private lateinit var otpFieldOne: EditText
    private lateinit var otpFieldTwo: EditText
    private lateinit var otpFieldThree: EditText
    private lateinit var otpFieldFour: EditText
    private lateinit var verifyButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var resendTextView: TextView
    private lateinit var crossimage: ImageView
    private lateinit var nestedScrollView: NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verifycode)

        // Setup status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = getColor(R.color.colorPrimaryDark)
        }

        // Initialize views
        otpFieldOne = findViewById(R.id.otpFieldone)
        otpFieldTwo = findViewById(R.id.otpFieldtwo)
        otpFieldThree = findViewById(R.id.otpFieldthree)
        otpFieldFour = findViewById(R.id.otpFieldfour)
        verifyButton = findViewById(R.id.buttonverify)
        timerTextView = findViewById(R.id.timerTextview)
        resendTextView = findViewById(R.id.resendtextView)
        crossimage = findViewById(R.id.crossImageView)
        nestedScrollView = findViewById(R.id.nestedscrollview)

        // Get the email and OTP from the intent
        val userEmail = intent.getStringExtra("USER_EMAIL")
        val otp = intent.getStringExtra("OTP")

        // Send OTP to email
        sendOtpToEmail(userEmail ?: "", otp ?: "")

        // Start countdown timer
        startCountdownTimer()

        // Handle the resend OTP functionality
        resendTextView.setOnClickListener {
            resendTextView.isEnabled = false
            sendOtpToEmail(userEmail ?: "", otp ?: "")
            startCountdownTimer()
        }

        // Adjust layout for keyboard
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        nestedScrollView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            nestedScrollView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = nestedScrollView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                nestedScrollView.scrollTo(0, verifyButton.bottom)
            }
        }

        // Handle button clicks
        verifyButton.setOnClickListener {
            val intent = Intent(this, Entercode::class.java)
            startActivity(intent)
        }

        crossimage.setOnClickListener {
            val intent = Intent(this, Vehicleregistration::class.java)
            startActivity(intent)
        }
    }

    private fun sendOtpToEmail(email: String, otp: String) {
        // Implement your email sending functionality here
    }

    private fun startCountdownTimer() {
        val timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                timerTextView.text = "Resend OTP in: $secondsRemaining seconds"
            }

            override fun onFinish() {
                timerTextView.text = "Resend OTP"
                resendTextView.isEnabled = true
            }
        }
        timer.start()
    }
}
