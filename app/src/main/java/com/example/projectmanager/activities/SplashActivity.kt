package com.example.projectmanager.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.example.projectmanager.databinding.ActivitySplashBinding
import com.example.projectmanager.firebase.FireStoreClass

class SplashActivity : AppCompatActivity() {
    private var binding: ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        // This is used to get the file from the assets folder and set it to the title textView.
        /* val typeface: Typeface =
             Typeface.createFromAsset(assets, "carbon bl.ttf")
             binding?.tvAppName?.typeface = typeface
             TODO add a font to project
             */

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUserID = FireStoreClass().getCurrentUserId()

            if (currentUserID.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish() // Call this when your activity is done and should be closed.
            // Start the Intro or Main Activity after 5 milliseconds.
        }, 500)
    }
}