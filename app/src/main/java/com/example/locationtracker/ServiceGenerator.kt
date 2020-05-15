package com.example.locationtracker


import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


//Upload File generator
class ServiceGenerator {
    private val BASE_URL = "https://www.onlinegdb.com/"
    fun uploadFile(context: Context, filename: String, desc:String ) {

        var file = File(context.filesDir,filename)

        //creating request body for file
        val requestFile: RequestBody = RequestBody.create(MediaType.parse("text/plain"), file)
        val descBody = MultipartBody.Part.createFormData("text/plain", file.name, requestFile)

        //File(file.toURI()).forEachLine { Log.d("location data",it) }
        //The gson builder
        val gson = GsonBuilder()
            .setLenient()
            .create()

        //creating retrofit object
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        //creating our api
        val api: FileUploadApi = retrofit.create(FileUploadApi::class.java)

        //creating a call and calling the upload image method
        val call = api.upload(requestFile,descBody)

        //finally performing the call
        call.enqueue(object : Callback<MyResponse?> {
            override fun onResponse(call: retrofit2.Call<MyResponse?>, response: retrofit2.Response<MyResponse?>) {
                if (!response.body()!!.error) {
                    Log.d("SUCCESS","File Uploaded Successfully...")
                } else {
                    Log.d( "Error","Some error occurred...")
                }
            }

            override fun onFailure(call: retrofit2.Call<MyResponse?>?, t: Throwable) {
                Log.d( "Error", t.message.toString())
            }
        })
    }
}