package com.teamwork.librarybox.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class File (
    @SerializedName("fileURI") val fileName: String,
    //@SerializedName("fileImagePath") val fileImagePath: String
) : Parcelable {
    constructor() : this("")
}