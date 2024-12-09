package com.imeanttobe.drawapplication.data.model

data class ChatSession(
    val id: String,
    val user1Id: String,
    val user2Id: String,
    var lastMessage: String,
    val closed: Boolean = false
) {
    constructor() : this(
        id = "",
        user1Id = "",
        user2Id = "",
        lastMessage = ""
    )
}