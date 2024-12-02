package com.imeanttobe.drawapplication.data.etc

sealed class Resource(
    val message: String? = null
) {
    class Success() : Resource()
    class Loading() : Resource()
    class Error(message: String) : Resource(message)
}