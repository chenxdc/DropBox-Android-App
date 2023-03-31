package com.teamwork.librarybox.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FileResponse(
    val files: List<File>
) : Parcelable{
    constructor() : this(mutableListOf())
}