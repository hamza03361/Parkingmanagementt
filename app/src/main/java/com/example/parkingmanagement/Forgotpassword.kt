package com.example.parkingmanagement


import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.SecureRandom
import java.util.*
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class Forgotpassword : ComponentActivity() {

    private lateinit var crossImageView: ImageView
    private lateinit var emailEditText: EditText
    private lateinit var sendCodeButton: Button
    private lateinit var nestedScrollView: NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgotpassword)
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
                nestedScrollView.scrollTo(0, sendCodeButton.bottom)
            } else {
                // Keyboard is closed, handle any other actions if necessary
            }
        }

        crossImageView = findViewById(R.id.crossImageView)
        emailEditText = findViewById(R.id.usernameedittext)
        sendCodeButton = findViewById(R.id.nextbutton)

        crossImageView.setOnClickListener {
            finish()
        }

        sendCodeButton.setOnClickListener {
            val email = emailEditText.text.toString()
            if (email.isNotEmpty()) {
                val intent = Intent(this, Otponemail::class.java)
                startActivity(intent)
                val otp = generateOTP()
                sendEmail(email, otp)
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateOTP(): String {
        val random = SecureRandom()
        val otp = StringBuilder()
        for (i in 0 until 4) {
            otp.append(random.nextInt(10))
        }
        return otp.toString()
    }

    private fun sendEmail(recipientEmail: String, otp: String) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val properties = Properties().apply {
                        put("mail.smtp.auth", "true")
                        put("mail.smtp.starttls.enable", "true")
                        put("mail.smtp.host", "smtp.gmail.com")
                        put("mail.smtp.port", "587")
                        put("mail.smtp.ssl.trust", "smtp.gmail.com")
                    }

                    val session = Session.getInstance(properties, object : Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication("yourEmail@gmail.com", "yourEmailPassword")
                        }
                    })

                    val message = MimeMessage(session).apply {
                        setFrom(InternetAddress("yourEmail@gmail.com"))
                        setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail))
                        subject = "Your OTP Code"
                        setText("Your OTP code is $otp")
                    }

                    Transport.send(message)
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Forgotpassword, "OTP sent to your email", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("ForgotPasswordActivity", "Error sending email", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Forgotpassword, "Error sending email", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
