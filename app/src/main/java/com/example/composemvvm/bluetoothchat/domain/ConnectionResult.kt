package com.example.composemvvm.bluetoothchat.domain

sealed interface ConnectionResult {
    object ConnectionEstablished :ConnectionResult
    data class Error(val message:String) :ConnectionResult
}