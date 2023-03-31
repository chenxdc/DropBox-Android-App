package com.teamwork.librarybox

import java.io.Serializable

data class FileData(
    val fileName: String,
    val fileImagePath: String
) : Serializable
