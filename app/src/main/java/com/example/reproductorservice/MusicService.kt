package com.example.reproductorservice
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class MusicService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val TAG = "MusicService"
    private var isPaused = false
    private val CHANNEL_ID = "MusicChannel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START" -> startMusic()
            "STOP" -> stopMusic()
            "RESUME" -> resumeMusic()
            "PAUSE" -> pauseMusic()
            "RESTART" -> restartMusic()
            "SHOW_NOTIFICATION" -> showNotification()
            "HIDE_NOTIFICATION" -> stopForeground(STOP_FOREGROUND_REMOVE)
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

    private fun restartMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer = MediaPlayer.create(this, R.raw.himno_aqp)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
        Log.d(TAG, "Reproducción reiniciada")
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

    private fun showNotification() {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Reproduciendo música")
            .setContentText("La música está en reproducción")
            .setSmallIcon(R.drawable.arequipa_bandera)
            .setAutoCancel(false)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        Log.d(TAG, "Show Notification")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.setSound(null, null)
            channel.enableVibration(false)

            val manager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
            Log.d(TAG, "Created channel")
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
