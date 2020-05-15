package com.example.locationtracker

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface FileUploadApi {
    @Multipart
    @POST("upload")
    fun upload(
        @Part("description") description: RequestBody?,
        @Part file: MultipartBody.Part
    ): Call<MyResponse>
}