package com.example.locationtracker

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import android.os.Handler
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class AudioRecorder(mcontext: Context) {
    private val LOG_TAG = "AudioRecordTest"
    private var timer = 0L
    private var restarttime = 5*60*1000L
    private var recorder: MediaRecorder? = null
    private var context = mcontext
    fun startRecorder() {
        val outputFile=createAudioFile()

        //create a Media recorder object, set the input attributes and start
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(outputFile.absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }

            start()
        }
        //we need to stop the recording after certain time and save data into a file
        //waits till the time mentioned and calls stop method
        Handler().postDelayed(
            {
                // This method will be executed once the timer is over
                recorder?.apply {
                    stop()
                    release()
                }
                recorder = null
            },
            1*60*1000 // value in milliseconds
        )
    }
    private fun createAudioFile(): File {
        val audioCapturesDirectory = File(context.getExternalFilesDir(null), "/AudioCaptures")
        if (!audioCapturesDirectory.exists()) {
            audioCapturesDirectory.mkdirs()
        }
        val timestamp = SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", Locale.US).format(Date())
        val fileName = "Capture-$timestamp.3gp"
        return File(audioCapturesDirectory.absolutePath + "/" + fileName)
    }
    //call the save method to store the Audio recording
}