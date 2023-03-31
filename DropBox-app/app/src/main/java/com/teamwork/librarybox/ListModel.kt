package com.teamwork.librarybox

import com.google.gson.annotations.SerializedName


data class ListModel (
    @SerializedName("fileURI") val fileName: String? = null
        )