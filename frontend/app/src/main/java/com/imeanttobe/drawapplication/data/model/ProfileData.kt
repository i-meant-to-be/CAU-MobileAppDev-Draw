package com.imeanttobe.drawapplication.data.model

import android.net.Uri

data class ProfileData (

        val userId: String,
        val nickname: String,
        val role: String,
        val oneSentence: String,
        val imageUris: List<Uri>)