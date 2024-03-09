package com.example.composemvvm.bluetoothchat.domain

typealias BluetoothDeviceDomain = BluetoothDevice
data class BluetoothDevice(
    var name: String?,
    var address: String
)