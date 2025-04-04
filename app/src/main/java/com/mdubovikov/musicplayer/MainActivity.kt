package com.mdubovikov.musicplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mdubovikov.musicplayer.databinding.ActivityMainBinding
import com.mdubovikov.player.service.MusicService

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw IllegalStateException("Activity $this binding cannot be accessed")

    private var isServiceRunning = false

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if (!isServiceRunning) {
            val intent = Intent(this, MusicService::class.java)
            startForegroundService(intent)
            isServiceRunning = true
        }
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)
    }

    @SuppressLint("ImplicitSamInstance")
    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MusicService::class.java))
        isServiceRunning = false
        _binding = null
    }
}