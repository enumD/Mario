package com.example.mario

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.KeyEvent
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoiceService : Service() {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionCallback: MediaSessionCallback
    private val serviceScope = CoroutineScope(Dispatchers.Main)
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()

        //media session
        mediaSessionCallback = MediaSessionCallback(this)
        mediaSession = MediaSessionCompat(this, "VoiceService").apply {
            setCallback(mediaSessionCallback)
            isActive = true
        }
        // Create notification channel (required for Android Oreo and above)
        createNotificationChannel()

        // Start foreground service
        val notification = createNotification()

        startForeground(1, notification.build())
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startVoiceRecognition()
    {
//        val recIntent = Intent(this, VoiceRecognitionActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        }
//        startActivity(recIntent)
    }

    private fun logToUI(message: String)
    {
        serviceScope.launch {
            LogChannel.sendLog(message)
        }
    }

    private fun playBeep()
    {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, R.raw.beep)
        mediaPlayer?.start()
    }

    private fun vibrate(duration: Long)
    {
        val vibrator = getSystemService(Vibrator::class.java) as Vibrator

        vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))

    }

    private fun createNotificationChannel()
    {

            val name = "Voice Service Channel"
            val descriptionText = "Channel for voice service notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("voice_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

    }

    private fun createNotification(): NotificationCompat.Builder
    {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, "voice_channel")
            .setContentTitle("Assistente vocale attivo")
            .setContentText("In ascolto per il comando")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }


    class MediaSessionCallback(private val voiceService: VoiceService) : MediaSessionCompat.Callback() {
        override fun onMediaButtonEvent(mediaButtonIntent: Intent?): Boolean {
            val keyEvent = mediaButtonIntent?.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
            if (keyEvent?.keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && keyEvent.action == KeyEvent.ACTION_DOWN) {
                Log.d("VoiceService", "Volume Down premuto — avvio riconoscimento vocale")
                voiceService.logToUI("Volume Down premuto — avvio riconoscimento vocale")
                voiceService.vibrate(500)
                voiceService.playBeep()
                voiceService.startVoiceRecognition()
            }
            return super.onMediaButtonEvent(mediaButtonIntent)
        }
    }
}