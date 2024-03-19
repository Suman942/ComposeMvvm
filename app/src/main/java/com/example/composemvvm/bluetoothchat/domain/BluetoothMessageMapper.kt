package com.example.composemvvm.bluetoothchat.domain

fun BluetoothMessage.toByteArray(): ByteArray{
    return "$senderName#$message".encodeToByteArray()
}

fun String.toBluetoothMessage(isFromLocal:Boolean):BluetoothMessage{
    val name = substringBeforeLast("#")
    val message= substringAfterLast("#")
    return BluetoothMessage(
        message = message,
        senderName =  name,
        isFromLocal = isFromLocal
    )
}