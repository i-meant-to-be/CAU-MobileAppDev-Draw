package com.imeanttobe.drawapplication.data.model

data class Message(
    val id: String,
    val senderId: String,
    val chatSessionId: String,
    val body: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    constructor() : this(
        id = "",
        senderId = "",
        chatSessionId = "",
        body = ""
    )

}