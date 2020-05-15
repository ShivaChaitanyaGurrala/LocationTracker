package com.example.locationtracker

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val SHARED_PREFERENCES = "Location_TRACKER_PREFERENCES"
    var locationStatus = false
    private val REQUEST_PERMISSION_LOCATION = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
        createNotificationChannel()

        imageButton.setOnClickListener(){
            // get & update the shared preferences
            var sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
            if(sharedPreferences.contains("ACTIVITY_IDLENESS")) {
                locationStatus = sharedPreferences.getBoolean("ACTIVITY_IDLENESS", false)
            }
            val editor = sharedPreferences.edit()
            locationStatus = !locationStatus
            editor.putBoolean("ACTIVITY_IDLENESS", locationStatus)
            editor.commit()

            if(locationStatus) {
                if (checkPermissionForLocation(this)) {
                    imageButton.setImageResource(R.drawable.stop)
                    textView.text = "Stop Location Tracker"
                    Toast.makeText(this, "Idleness tracking activated!", Toast.LENGTH_SHORT).show()
                    val serviceIntent = Intent(this, LocationService::class.java)
                    serviceIntent.putExtra("TRACKING_LOCATION_ACTIVITY", "Location")
                    serviceIntent.putExtra("TRACKING_LOCATION_STATUS", locationStatus)
                    ContextCompat.startForegroundService (this, serviceIntent)
                }
            }
        else {
                imageButton.setImageResource(R.drawable.play)
                textView.text = "Start Location Tracker"
                Toast.makeText(this, "Idleness tracking deactivated!", Toast.LENGTH_SHORT).show()
                val serviceIntent = Intent(this,LocationService::class.java)
                stopService(serviceIntent)
        }
    }
    }
    private fun buildAlertMessageNoGps() {

        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    , 11)
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
                finish()
            }
        val alert: AlertDialog = builder.create()
        alert.show()


    }
    private fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // Show the permission request
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(getString(R.string.CHANNEL_ID),getString(R.string.CHANNEL_NEWS), NotificationManager.IMPORTANCE_DEFAULT )
            val notificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
