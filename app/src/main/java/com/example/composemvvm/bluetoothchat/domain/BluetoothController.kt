package com.example.composemvvm.bluetoothchat.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {
    val isConnected:StateFlow<Boolean>
    val scannedDevices:StateFlow<List<BluetoothDeviceDomain>>
    val pairedDevices:StateFlow<List<BluetoothDeviceDomain>>
    val error:SharedFlow<String>

    fun startDiscovery()
    fun stopDiscovery()
    fun startBluetoothServer() : Flow<ConnectionResult>
    fun connectionToDevice(device: BluetoothDevice) : Flow<ConnectionResult>
    fun closeConnection()
    fun release()
}