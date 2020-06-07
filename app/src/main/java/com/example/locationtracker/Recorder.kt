package com.example.locationtracker

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class Recorder(mcontext: Context) {
    private var timer = 0L
    private var restarttime = 1 * 60 * 1000L
    private val RECORDER_SAMPLERATE = 44100
    private val RECORDER_CHANNELS: Int = AudioFormat.CHANNEL_IN_MONO
    private val RECORDER_AUDIO_ENCODING: Int = AudioFormat.ENCODING_PCM_16BIT
    private var recorder: AudioRecord? = null
    private var context = mcontext


    fun startRecord(record: Boolean) {

        var recording = record
        Log.d("Audio Service", "Starting Audio service")
        thread(start = true){
        try {

            val minBufferSize = AudioRecord.getMinBufferSize(
                RECORDER_SAMPLERATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING
            )
            val audioData = ShortArray(minBufferSize)
            val audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLERATE,
                RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING,
                minBufferSize
            )
            //int sessionId=audioRecord.getAudioSessionId();
            //  NoiseSuppressor.create(sessionId);
            audioRecord.startRecording()
            val file = createAudioFile()
            val outputStream: OutputStream = FileOutputStream(file)
            val bufferedOutputStream = BufferedOutputStream(outputStream)
            val dataOutputStream = DataOutputStream(bufferedOutputStream)
            timer = System.currentTimeMillis()
            Log.d("Start", "Starting Recording")
            while (recording) {
                val numberOfShort = audioRecord.read(audioData, 0, minBufferSize)
                for (i in 0 until numberOfShort) {
                    dataOutputStream.writeShort(audioData[i].toInt())
                }
                if (System.currentTimeMillis() - timer >= restarttime) {
                    recording = false
                    Log.d("End", "End recording")
                }
            }
            audioRecord.stop()
            dataOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
    private fun createAudioFile(): File {
        val audioCapturesDirectory = File(context.getExternalFilesDir(null), "/AudioCaptures")
        if (!audioCapturesDirectory.exists()) {
            audioCapturesDirectory.mkdirs()
        }
        val timestamp = SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", Locale.US).format(Date())
        val fileName = "Capture-$timestamp.pcm"
        return File(audioCapturesDirectory.absolutePath + "/" + fileName)
    }
    //call the save method to store the Audio recording
}