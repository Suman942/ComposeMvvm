package com.example.composemvvm.bluetoothchat.domain

import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val scannedDevices:StateFlow<List<BluetoothDeviceDomain>>
    val pairedDevices:StateFlow<List<BluetoothDeviceDomain>>

    fun startDiscovery()
    fun stopDiscovery()
    fun release()
}