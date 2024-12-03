package com.imeanttobe.drawapplication.data.model

data class ChatSession(
    val id: String,
    val user1Id: String,
    val user2Id: String,
    val lastMessage: String
)