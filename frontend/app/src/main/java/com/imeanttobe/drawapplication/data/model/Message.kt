package com.imeanttobe.drawapplication.data.model

import java.time.LocalDateTime

data class Message(
    val id: Int = -1,
    val senderId: String,
    val chatSessionId: String,
    val body: String,
    val datetime: LocalDateTime = LocalDateTime.now()
)