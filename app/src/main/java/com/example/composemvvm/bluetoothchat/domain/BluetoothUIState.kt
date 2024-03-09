package com.example.composemvvm.bluetoothchat.domain

data class BluetoothUIState(
    val scannedDevices : List<BluetoothDevice> = emptyList(),
    val pairedDevices : List<BluetoothDevice> = emptyList(),

    )
