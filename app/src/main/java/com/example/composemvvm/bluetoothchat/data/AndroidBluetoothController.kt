package com.example.composemvvm.bluetoothchat.data

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import com.example.composemvvm.bluetoothchat.domain.BluetoothController
import com.example.composemvvm.bluetoothchat.domain.BluetoothDeviceDomain
import com.example.composemvvm.bluetoothchat.domain.BluetoothMessage
import com.example.composemvvm.bluetoothchat.domain.ConnectionResult
import com.example.composemvvm.bluetoothchat.domain.toByteArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
class AndroidBluetoothController(
    private val context: Context
) : BluetoothController {
    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private var bluetoothDataService: BluetoothDataTransferService? = null

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean>
        get() = _isConnected.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    override val error: SharedFlow<String>
        get() = _error.asSharedFlow()

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val pairedDevices: StateFlow<List<BluetoothDeviceDomain>>
        get() = _pairedDevices.asStateFlow()

    private var currentServerSocket: BluetoothServerSocket? = null
    private var currentClientSocket: BluetoothSocket? = null


    private val foundDevicesReceiver = FoundDevicesReceiver { device ->
        _scannedDevices.update { devices ->
            val newDevice = device.toBluetoothDeviceDomain()
            if (newDevice in devices) devices else devices + newDevice
        }
    }

    private val bluetoothStateReceiver = BluetoothStateReceiver { isConnected, bluetoothDevice ->
        if (bluetoothAdapter?.bondedDevices?.contains(bluetoothDevice) == true) {
            _isConnected.update { isConnected }
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                _error.emit("Can't connect to unpaired device")
            }
        }
    }

    init {
        updatePairedDevices()
        context.registerReceiver(bluetoothStateReceiver,
            IntentFilter().apply
            {
                addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            })
    }

    override fun startDiscovery() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
                return
            }
        } else {
            if (!hasPermission(Manifest.permission.BLUETOOTH)) {
                return
            }
        }
        context.registerReceiver(
            foundDevicesReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )

        updatePairedDevices()
        bluetoothAdapter?.startDiscovery()
    }

    override fun startBluetoothServer(): Flow<ConnectionResult> {
        return flow {
            if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("No Bluetooth_Connect Permission")
            }
            currentServerSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                "chat_service", UUID.fromString(
                    SERVICE_UUID
                )
            )

            var shouldLoop = true
            while (shouldLoop) {
                currentClientSocket = try {
                    currentServerSocket?.accept()
                } catch (e: IOException) {
                    shouldLoop = false
                    null
                }
                emit(ConnectionResult.ConnectionEstablished)
                currentClientSocket?.let {
                    currentServerSocket?.close()
                    val service = BluetoothDataTransferService(it)
                    bluetoothDataService = service

                    emitAll(service.listenForIncomingMessages()
                        .map { ConnectionResult.TransferSucceeded(it) })
                }
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    override fun connectionToDevice(device: BluetoothDeviceDomain): Flow<ConnectionResult> {
        return flow {
            if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("No Bluetooth_Connect Permission")
            }
            val bluetoothDevice = bluetoothAdapter?.getRemoteDevice(device.address)

            currentClientSocket = bluetoothDevice
                ?.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_UUID))

            stopDiscovery()
            if (bluetoothAdapter?.bondedDevices?.contains(bluetoothDevice) == false) {

            }

            currentClientSocket?.let { socket ->
                try {
                    socket.connect()
                    emit(ConnectionResult.ConnectionEstablished)
                    BluetoothDataTransferService(socket).also {
                        bluetoothDataService = it
                       emitAll(it.listenForIncomingMessages().map { ConnectionResult.TransferSucceeded(it) })
                    }
                } catch (e: IOException) {
                    socket.close()
                    currentClientSocket = null
                    emit(ConnectionResult.Error("Connection Interrupted"))
                }
            }
        }.onCompletion {
            closeConnection()
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun trySendMessage(message: String): BluetoothMessage? {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)){
            return null
        }
        if (bluetoothDataService == null){
            return null
        }
        val bluetoothMessage = BluetoothMessage(
            message = message,
            senderName = bluetoothAdapter?.name ?:"Unknown",
            isFromLocal = true
        )
        bluetoothDataService?.sendMessage(bluetoothMessage.toByteArray())
        return bluetoothMessage
    }
    override fun closeConnection() {
        currentClientSocket?.close()
        currentServerSocket?.close()
        currentServerSocket = null
        currentClientSocket = null
    }

    override fun stopDiscovery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)) {
                return
            }
        } else {
            if (!hasPermission(Manifest.permission.BLUETOOTH)) {
                return
            }
        }
        bluetoothAdapter?.cancelDiscovery()
    }

    override fun release() {
        context.unregisterReceiver(foundDevicesReceiver)
        context.unregisterReceiver(bluetoothStateReceiver)

        closeConnection()
    }

    private fun updatePairedDevices() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                return
            }
        } else {
            if (!hasPermission(Manifest.permission.BLUETOOTH)) {
                return
            }
        }
        bluetoothAdapter?.bondedDevices
            ?.map { it.toBluetoothDeviceDomain() }
            ?.also { devices -> _pairedDevices.update { devices } }
    }

    private fun hasPermission(permission: String): Boolean {
        return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val SERVICE_UUID = "2957b9ea-f7ba-4c3f-adb1-389f837240df"
    }
}