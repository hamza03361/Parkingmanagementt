package com.example.parkingmanagement

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.core.widget.NestedScrollView

class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var nestedScrollView: NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = getColor(R.color.colorPrimaryDark) // Change to your color resource
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val eyeIcon: ImageView = findViewById(R.id.eyeIcon)
        val forgotPasswordTextView: TextView = findViewById(R.id.forgotPasswordTextview)
        val loginButton: Button = findViewById(R.id.loginbutton)
        val registerButton: Button = findViewById(R.id.registerbutton)

        var isPasswordVisible = false
        eyeIcon.setOnClickListener {
            if (isPasswordVisible) {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeIcon.setImageResource(R.drawable.eye) // Replace with the correct drawable for the closed eye
            } else {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT
                eyeIcon.setImageResource(R.drawable.eye) // Replace with the correct drawable for the open eye
            }
            isPasswordVisible = !isPasswordVisible
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, Forgotpassword::class.java)
            startActivity(intent)
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
                nestedScrollView.scrollTo(0, registerButton.bottom)
            } else {
                // Keyboard is closed, handle any other actions if necessary
            }
        }


        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(email, password)
            } else {
                showMessage("Email and password must not be empty")
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, Signupactivity::class.java)
            startActivity(intent)
        }
    }

    private fun authenticateUser(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, navigate to HomeActivity
                    navigateToHomeActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    showMessage("Authentication failed: ${task.exception?.message}")
                }
            }
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, Homeactivity::class.java)
        startActivity(intent)
        finish() // Optional: close LoginActivity
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
