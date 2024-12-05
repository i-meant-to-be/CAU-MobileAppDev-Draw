package com.imeanttobe.drawapplication.data.model

import android.net.Uri
import com.imeanttobe.drawapplication.data.etc.PostWrapper
import java.time.format.DateTimeFormatter

private val localDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

data class Post(
    val id: String,
    val userId: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUri: Uri
) {
    constructor() : this(
        id = "",
        userId = "",
        description = "",
        imageUri = Uri.EMPTY
    )

    constructor(postWrapper: PostWrapper) : this(
        id = postWrapper.id,
        userId = postWrapper.userId,
        description = postWrapper.description,
        timestamp = postWrapper.timestamp,
        imageUri = Uri.parse(postWrapper.imageUri)
    )
}