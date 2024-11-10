package com.imeanttobe.drawapplication.data.enum

enum class UserType {
    ADMIN, WEBTOON_ARTIST, ASSIST_ARTIST;

    override fun toString(): String {
        return when (this) {
            ADMIN -> "Admin"
            WEBTOON_ARTIST -> "Webtoon Artist"
            ASSIST_ARTIST -> "Assist Artist"
        }
    }
}