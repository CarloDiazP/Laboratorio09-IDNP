package com.example.reproductorservice
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log

class MusicService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val TAG = "MusicService"
    private var isPaused = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START" -> startMusic()
            "STOP" -> stopMusic()
            "RESUME" -> resumeMusic()
            "PAUSE" -> pauseMusic()
        }
        return START_STICKY
    }

    private fun startMusic() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.himno_aqp)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
            Log.d(TAG, "Reproducción iniciada")
        }
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        Log.d(TAG, "Reproducción detenida")
    }

    private fun pauseMusic() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isPaused = true
            Log.d(TAG, "Reproducción pausada")
        }
    }

    private fun resumeMusic() {
        if (isPaused) {
            mediaPlayer?.start()
            isPaused = false
            Log.d(TAG, "Reproducción reanudada")
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        stopMusic()
        super.onDestroy()
    }
}
