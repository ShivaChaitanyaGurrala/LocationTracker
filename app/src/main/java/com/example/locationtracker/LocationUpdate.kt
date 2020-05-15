package com.example.locationtracker

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*


class LocationUpdate(mcontext: Context) {

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val INTERVAL: Long = 5000
    private val FASTEST_INTERVAL: Long = 1000
    lateinit var mLastLocation: Location
    private var mLocationRequest: LocationRequest = LocationRequest()
    private var locationManager:LocationManager? = null
    private var checktimer = false
    private var iscreatefile = true
    lateinit var outputStreamWriter : OutputStreamWriter
    private var tempname =  "location_data_"
    private var context = mcontext
    private var timer = 0L
    private var restarttime = 5*60*1000L

    //Initialize Location manager
    fun startUpdater(){
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if(isLocationEnabled()){
            startLocationUpdates()
        }
        timer = System.currentTimeMillis()
    }

    //Check if location is enabled before trying to receive data
    private fun isLocationEnabled(): Boolean {
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    protected fun startLocationUpdates() {

        // Create the location request to start receiving updates

        mLocationRequest!!.priority = PRIORITY_HIGH_ACCURACY
        mLocationRequest!!.interval = INTERVAL
        mLocationRequest!!.fastestInterval = FASTEST_INTERVAL

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)

        val locationSettingsRequest = builder.build()
        val settingsClient = LocationServices.getSettingsClient(context)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        // new Google API SDK v11 uses getFusedLocationProviderClient

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback,
            Looper.myLooper())
    }

    private val mLocationCallback = object : LocationCallback() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onLocationResult(locationResult: LocationResult) {
            // do work here
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    //On every location change capture Lat/Long data
    @RequiresApi(Build.VERSION_CODES.N)
    fun onLocationChanged(location: Location) {
        // New location has now been determined

        mLastLocation = location
        val date: Date = Calendar.getInstance().time
        val sdf = SimpleDateFormat("hh:mm:ss a")
        //Check if file already exists to write the data
        try {

            if ( iscreatefile ) {
                timer = System.currentTimeMillis()
                var locationrecord =
                    mLastLocation.latitude.toString() + " " + mLastLocation.longitude.toString() + " " + date.toString()
                tempname += timer.toString()
                tempname += ".txt"
                try {
                    outputStreamWriter =
                        OutputStreamWriter(context.openFileOutput(tempname, Context.MODE_PRIVATE))
                    outputStreamWriter.appendln(locationrecord)
                    //outputStreamWriter.close()
                } catch (e: IOException) {
                    Log.e("Exception", "File write failed: " + e.toString())
                }
                iscreatefile = false
            }
            else{
                var locationrecord = mLastLocation.latitude.toString() + " " + mLastLocation.longitude.toString() + " " + date.toString()
                try {
                    outputStreamWriter.appendln(locationrecord)
                    //outputStreamWriter.close()
                } catch (e: IOException) {
                    Log.e("Exception", "File write failed: " + e.toString())
                }
            }

            checktimer = true
            //check if the timer is more than 5 mins and send to server and clear the temp file
            if (System.currentTimeMillis() - timer >= restarttime) {
                outputStreamWriter.close()
                var file = File(context.filesDir,tempname)
                Log.d("New Location Data File",":  $tempname")
                File(file.toURI()).forEachLine { Log.d("location data",it) }

                // Uncomment the below code to send call sending the stored file to server

                /*val uploaddata = ServiceGenerator()
                uploaddata.uploadFile(context,tempname,"New Gps data")*/

                iscreatefile = true
                tempname =  "location_data_"
                timer =  System.currentTimeMillis()
            }

        }catch (error:Exception){
            Log.e("error message",error.toString())
        }
        /*Log.i("Updated at : " , date.toString())
        Log.i("LATITUDE :", mLastLocation.latitude.toString())
        Log.i("LONGITUDE : " , mLastLocation.longitude.toString())*/
        // You can now create a LatLng Object for use with maps
    }

}






