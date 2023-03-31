package com.example.homework_5

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class MovieData(
    val vote_count: Int?,
    val id: Int?, // movie_id
    val vote_average: Double?,
    var title: String, //?
    val popularity: Double?,
    val poster_path: String?,
    val original_language: String?,
    val original_title: String?,
    var genre_ids: List<Int?>?,
    val backdrop_path: String?,
    val overview: String?,
    val release_date: String?,
    var db_id: Int = -1, // db primary key
    var genres:String = "",
    var checked: Boolean = false

    ) : Serializable















