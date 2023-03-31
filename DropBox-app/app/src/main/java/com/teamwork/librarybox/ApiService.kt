package com.teamwork.librarybox

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("search/searchAll?userId=448fb6c3-7ec8-478d-97fc-7e879e6b8827")
    fun getFiles() : Call<MutableList<ListModel>>


}