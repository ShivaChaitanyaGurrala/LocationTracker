package com.example.locationtracker

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlin.concurrent.thread

class AudioService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //create Recorder class instance and call the audio recorder service
        /*val audioRecorder = AudioRecorder(this)
        audioRecorder.startRecorder()*/

        //set the notification
        val text = "Location Tracker"
        val notification: Notification = NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
            .setSmallIcon(R.drawable.location)
            .setContentTitle("Location Tracker Service")
            .setContentText(text)
            .build()
        notification.flags = 16 or notification.flags
        startForeground(1, notification)
        //Create recorder class and store the data to file
        val recording = true
        val recorder = Recorder(this)
        recorder.startRecord(recording)

        return START_NOT_STICKY
    }
}