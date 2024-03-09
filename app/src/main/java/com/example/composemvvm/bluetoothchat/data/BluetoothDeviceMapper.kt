package com.example.composemvvm.bluetoothchat.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.composemvvm.bluetoothchat.domain.BluetoothDeviceDomain

@SuppressLint("MissingPermission")
fun BluetoothDevice.toBluetoothDeviceDomain(): BluetoothDeviceDomain{
    return BluetoothDeviceDomain(
        name = name,
        address = address
    )

}