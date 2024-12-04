package com.example.reproductorservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        // Verificar si la versión de Android es 13 o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Verificar si el permiso ya está otorgado
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                // Solicitar el permiso
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notificaciones activadas", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se mostrarán notificaciones", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, MusicService::class.java).apply {
            action = "HIDE_NOTIFICATION"
        }
        Log.d("Main", "Start")
        startService(intent)
    }

    override fun onStop() {
        super.onStop()
        val intent = Intent(this, MusicService::class.java).apply {
            action = "SHOW_NOTIFICATION"
        }
        Log.d("Main", "Stop")
        startService(intent)
    }


}