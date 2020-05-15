package com.example.locationtracker

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat


class LocationService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //create the LocationUpdate object and call startUpdater to read the Location data
        var locationprovider = LocationUpdate(this)
        locationprovider.startUpdater()

        //Create Notification as its a Foreground Service
        val text = "Location Tracker"
        val notification: Notification = NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
            .setSmallIcon(R.drawable.location)
            .setContentTitle("Location Tracker Service")
            .setContentText(text)
            .build()
        notification.flags = 16 or notification.flags
        startForeground(1, notification)
        return START_NOT_STICKY
    }

}
