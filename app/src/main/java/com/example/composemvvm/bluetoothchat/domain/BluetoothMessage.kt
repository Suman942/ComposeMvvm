package com.example.composemvvm.bluetoothchat.domain

data class BluetoothMessage(
    var message:String,
    var senderName:String,
    var isFromLocal:Boolean
)
