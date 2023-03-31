package com.example.homework_5

import com.google.gson.Gson

class MovieList {
    var movieList: List<MovieData> = Gson().fromJson(movies, Array<MovieData>::class.java).asList()

    /*
    var posterTable:MutableMap<String, Int> = mutableMapOf()
    init{
        posterTable[movieList[0].title]=R.drawable.m5
        posterTable[movieList[1].title]=R.drawable.m1
        posterTable[movieList[2].title]=R.drawable.m2
        posterTable[movieList[3].title]=R.drawable.m3
        posterTable[movieList[4].title]=R.drawable.m4
        posterTable[movieList[5].title]=R.drawable.m0
        posterTable[movieList[6].title]=R.drawable.m6
        posterTable[movieList[7].title]=R.drawable.m7
    }
     */

    var genreTable:MutableMap<Int, String> = mutableMapOf()
    init{
        genreTable[0]="Action"
        genreTable[1]="Adventure"
        genreTable[2]="Animated"
        genreTable[3]="Comedy"
        genreTable[4]="Drama"
        genreTable[5]="Fantasy"

    }
}