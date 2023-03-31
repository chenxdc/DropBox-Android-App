package com.teamwork.librarybox.models.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FileApiService {

    companion object{
        private const val BASE_URL = "http://10.0.2.2:8080/"
        private var retrofit : Retrofit? = null

        fun getInstance() : Retrofit{
            if(retrofit ==null){
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
        return retrofit!!
        }
    }
}