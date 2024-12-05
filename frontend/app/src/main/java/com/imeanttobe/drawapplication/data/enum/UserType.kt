package com.imeanttobe.drawapplication.data.enum

enum class UserType {
    ADMIN, WEBTOON_ARTIST, ASSIST_ARTIST, UNDEFINED;

    companion object {
        fun fromString(value: String): UserType {
            return when (value) {
                "ADMIN" -> ADMIN
                "WEBTOON_ARTIST" -> WEBTOON_ARTIST
                "ASSIST_ARTIST" -> ASSIST_ARTIST
                else -> UNDEFINED
            }
        }
    }
}