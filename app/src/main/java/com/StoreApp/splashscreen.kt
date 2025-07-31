package com.StoreApp

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.StoreApp.databinding.ActivitySplashscreenBinding
import com.StoreApp.ui.LoginActivity
import com.StoreApp.ui.OnboardingActivity

class splashscreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashscreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)



        // hide actionbar
        supportActionBar?.hide()
        val videoView = binding.videoSpalash

        val videoPath = "android.resource://" + packageName + "/" + R.raw.splash_video
        videoView.setVideoURI(Uri.parse(videoPath))

        //run uto video
        videoView.start()

       // listener to end video
        videoView.setOnCompletionListener(MediaPlayer.OnCompletionListener {

            val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
            if (prefs.getBoolean("isOnboardingCompleted", false)) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, OnboardingActivity::class.java))
            }
            finish()
        })



    }

    }
