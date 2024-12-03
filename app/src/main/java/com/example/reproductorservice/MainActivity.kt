package com.example.reproductorservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.reproductorservice.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private var isPaused = false
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRestart.setOnClickListener {
            val intent = Intent(this, MusicService::class.java)
            intent.action = "RESTART"
            startService(intent)
        }

        binding.btnPlayPause.setOnClickListener {
            val intent = Intent(this, MusicService::class.java)
            when {
                !isPlaying -> {
                    intent.action = "START"
                    binding.btnPlayPause.setImageResource(R.drawable.pause_icon)
                    isPlaying = true
                    isPaused = false
                }
                isPaused -> {
                    intent.action = "RESUME"
                    binding.btnPlayPause.setImageResource(R.drawable.pause_icon)
                    isPaused = false
                }
                else -> {
                    intent.action = "PAUSE"
                    binding.btnPlayPause.setImageResource(R.drawable.play_icon)
                    isPaused = true
                }
            }
            startService(intent)
        }

        binding.btnStop.setOnClickListener {
            val intent = Intent(this, MusicService::class.java)
            intent.action = "STOP"
            startService(intent)
            isPlaying = false
            isPaused = false
            binding.btnPlayPause.setImageResource(R.drawable.play_icon)
        }
    }
}