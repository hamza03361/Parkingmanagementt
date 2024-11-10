package com.example.parkingmanagement

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
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
import androidx.core.widget.NestedScrollView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Signupactivity : ComponentActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var apartmentSpinner: Spinner
    private lateinit var eyeIcon: ImageView
    private lateinit var nextButton: Button
    private lateinit var smallImageView: ImageView
    private lateinit var dropdownIcon: ImageView
    private lateinit var nestedScrollView: NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signupactivity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = getColor(R.color.colorPrimaryDark) // Change to your color resource
        }

        // Initialize views
        usernameEditText = findViewById(R.id.usernameedittext)
        passwordEditText = findViewById(R.id.passwordEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneedittext)
        apartmentSpinner = findViewById(R.id.apartmentspinner)
        eyeIcon = findViewById(R.id.eyeIcon)
        nextButton = findViewById(R.id.nextbutton)
        smallImageView = findViewById(R.id.smallImageView)
        dropdownIcon = findViewById(R.id.dropdownicon)

        // Toggle password visibility
        var isPasswordVisible = false
        eyeIcon.setOnClickListener {
            if (isPasswordVisible) {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeIcon.setImageResource(R.drawable.eye) // Update with your icon resource for hidden password
            } else {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                eyeIcon.setImageResource(R.drawable.eye) // Update with your icon resource for visible password
            }
            isPasswordVisible = !isPasswordVisible
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        // Setup Spinner
        val apartmentOptions = arrayOf("Select Apartment", "A001", "A002", "A003")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, apartmentOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        apartmentSpinner.adapter = adapter

        // Navigate to VehicleRegistrationActivity
        nextButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val apartment = apartmentSpinner.selectedItem.toString().trim()

            if (validateInput(username, password, email, phone, apartment)) {
                // Move to the next activity with the user details
                val intent = Intent(this, Vehicleregistration::class.java).apply {
                    putExtra("USER_EMAIL", email)
                    putExtra("USER_PHONE", phone)
                    putExtra("USERNAME", username)
                    putExtra("PASSWORD", password)
                    putExtra("APARTMENT", apartment)
                }
                startActivity(intent)
                finish() // Optional: close Signupactivity if you don't want to return to it
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
                nestedScrollView.scrollTo(0, nextButton.bottom)
            } else {
                // Keyboard is closed, handle any other actions if necessary
            }
        }

        smallImageView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInput(username: String, password: String, email: String, phone: String, apartment: String): Boolean {
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || apartment.isEmpty()) {
            showMessage("All fields are required")
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMessage("Please enter a valid email address")
            return false
        }

        if (!phone.matches(Regex("^\\+\\d{12,15}$"))) {
            showMessage("Please enter a valid phone number in the format +923009834465")
            return false
        }

        return true
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
