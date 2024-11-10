package com.example.parkingmanagement

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthCredential
import java.util.concurrent.TimeUnit


class Vehicleregistration : ComponentActivity() {

    private lateinit var vehicleCompanyEditText: EditText
    private lateinit var vehicleYearEditText: EditText
    private lateinit var licensePlateEditText: EditText
    private lateinit var carColorSpinner: Spinner
    private lateinit var smallImageView: ImageView
    private lateinit var cancelButton: Button
    private lateinit var dropdownIcon: ImageView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var passwordTextView: TextView
    private lateinit var apartmentTextView: TextView

    private lateinit var nestedScrollView: NestedScrollView

    private lateinit var userEmail: String
    private lateinit var userPhone: String
    private lateinit var username: String
    private lateinit var password: String
    private lateinit var apartment: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vehicleregistration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = getColor(R.color.colorPrimaryDark) // Change to your color resource
        }

        // Initialize views
        vehicleCompanyEditText = findViewById(R.id.vehiclecompanyedittext)
        vehicleYearEditText = findViewById(R.id.yearEditText)
        licensePlateEditText = findViewById(R.id.licenseedittext)
        smallImageView = findViewById(R.id.smallImageView)
        cancelButton = findViewById(R.id.cancelbutton)
        dropdownIcon = findViewById(R.id.dropdownicon)
        carColorSpinner = findViewById(R.id.carcolorspinner)

        val carcolorOptions = arrayOf("Select Color", "White", "Silver", "Gun Metallic")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, carcolorOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        carColorSpinner.adapter = adapter
        // Get data from intent
        userEmail = intent.getStringExtra("USER_EMAIL") ?: ""
        userPhone = intent.getStringExtra("USER_PHONE") ?: ""
        username = intent.getStringExtra("USERNAME") ?: ""
        password = intent.getStringExtra("PASSWORD") ?: ""
        apartment = intent.getStringExtra("APARTMENT") ?: ""



        // Handle Next Button Click
        findViewById<Button>(R.id.nextbutton).setOnClickListener {
            val vehicleCompany = vehicleCompanyEditText.text.toString().trim()
            val vehicleYear = vehicleYearEditText.text.toString().trim()
            val licensePlate = licensePlateEditText.text.toString().trim()
            val carcolor = carColorSpinner.selectedItem.toString().trim()

            if (vehicleCompany.isNotEmpty() && vehicleYear.isNotEmpty() && licensePlate.isNotEmpty() && carcolor.isNotEmpty()) {
                registerUserToFirebase(vehicleCompany, vehicleYear, licensePlate, carcolor)
            } else {
                showMessage("All fields are required")
            }
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
                nestedScrollView.scrollTo(0, cancelButton.bottom)
            } else {
                // Keyboard is closed, handle any other actions if necessary
            }
        }

        // Handle Cancel Button Click
        smallImageView.setOnClickListener {
            val intent = Intent(this, Signupactivity::class.java)
            startActivity(intent)
        }

        cancelButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }

    private fun registerUserToFirebase(vehicleCompany: String, vehicleYear: String, licensePlate: String, carColor: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User registration success, now save additional user data to Firebase Realtime Database
                    uploadDataToFirebase(vehicleCompany, vehicleYear, licensePlate, carColor)
                } else {
                    // If sign up fails, display a message to the user.
                    showMessage("Registration failed: ${task.exception?.message}")
                }
            }
    }

    private fun uploadDataToFirebase(vehicleCompany: String, vehicleYear: String, licensePlate: String, carColor: String) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").push() // Generate a new unique key for each user

        val userData = mapOf(
            "email" to userEmail,
            "phone" to userPhone,
            "username" to username,
            "password" to password,
            "apartment" to apartment,
            "vehicleCompany" to vehicleCompany,
            "vehicleYear" to vehicleYear,
            "licensePlate" to licensePlate,
            "carColor" to carColor
        )

        userRef.setValue(userData)
            .addOnSuccessListener {
                showMessage("Data uploaded successfully")
                generateAndSendOTP()
            }
            .addOnFailureListener {
                showMessage("Failed to upload data")
            }
    }

    private fun generateAndSendOTP() {
        val otp = (1000..9999).random().toString() // Generate 4-digit OTP
        Log.d("OTP", "Generated OTP: $otp") // Log the OTP for debugging purposes
        sendOTPEmail(userEmail, otp)
        navigateToVerifyCodeActivity(otp)
    }

    private fun sendOTPEmail(email: String, otp: String) {
        val subject = "Your OTP Code"
        val message = "Your OTP code is $otp"

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        } catch (e: android.content.ActivityNotFoundException) {
            showMessage("No email clients installed.")
        }
    }


    private fun navigateToVerifyCodeActivity(otp: String) {
        val intent = Intent(this, Verifycode::class.java).apply {
            putExtra("USER_EMAIL", userEmail)
            putExtra("OTP", otp)
        }
        startActivity(intent)
    }


    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
