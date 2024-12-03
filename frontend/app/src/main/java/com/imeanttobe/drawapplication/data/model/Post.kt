package com.imeanttobe.drawapplication.data.model

import java.time.format.DateTimeFormatter

private val localDateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

data class Post(
    val id: String,
    val userId: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUrl: String
) {
    constructor() : this(
        id = "",
        userId = "",
        description = "",
        imageUrl = ""
    )
}