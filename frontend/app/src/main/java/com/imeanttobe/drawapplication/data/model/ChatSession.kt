package com.imeanttobe.drawapplication.data.model

data class ChatSession(
    val id: Int,
    val user1Id: String,
    val user2Id: String,
    val lastMessage: String
)