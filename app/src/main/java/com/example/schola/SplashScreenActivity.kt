package com.example.schola

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.schola.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load the animation
        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.animanton)

        // Start the animation on the ImageView
        binding.schola.startAnimation(bounceAnimation)

        // Set a listener to transition after the animation completes
        bounceAnimation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}

            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                // Transition to the next activity after animation ends
                val intent = Intent(this@SplashScreenActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })
    }
}
