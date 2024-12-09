package com.imeanttobe.drawapplication.data.etc

import android.net.Uri
import com.imeanttobe.drawapplication.data.model.Post

data class PostWrapper(
    val id: String,
    val userId: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUri: String
) {
    constructor() : this(
        id = "",
        userId = "",
        description = "",
        timestamp = 0,
        imageUri = ""
    )

    constructor(post: Post) : this(
        id = post.id,
        userId = post.userId,
        description = post.description,
        timestamp = post.timestamp,
        imageUri = post.imageUri.toString()
    )
}