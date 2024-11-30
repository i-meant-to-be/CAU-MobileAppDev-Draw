package com.imeanttobe.drawapplication.data.model

data class ChatSession(
    val id: Int,
    val userIdList: List<String>,
    val lastMessage: String
)