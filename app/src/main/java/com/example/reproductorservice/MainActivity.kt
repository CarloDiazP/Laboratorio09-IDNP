package com.example.reproductorservice

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reproductorservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private var isPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPlay.setOnClickListener {
            val intent = Intent(this, MusicService::class.java)
            intent.action = "START"
            startService(intent)
        }

        binding.btnPause.setOnClickListener {
            val intent = Intent(this, MusicService::class.java)
            if (isPaused) {
                intent.action = "RESUME"
                isPaused = false
                binding.btnPause.text = "Pausar"
            } else {
                intent.action = "PAUSE"
                isPaused = true
                binding.btnPause.text = "Reanudar"
            }
            startService(intent)
        }

        binding.btnStop.setOnClickListener {
            val intent = Intent(this, MusicService::class.java)
            intent.action = "STOP"
            startService(intent)
        }
    }
}