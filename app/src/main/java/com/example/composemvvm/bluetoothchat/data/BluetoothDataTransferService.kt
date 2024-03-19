package com.example.composemvvm.bluetoothchat.data

import android.bluetooth.BluetoothSocket
import com.example.composemvvm.bluetoothchat.domain.BluetoothMessage
import com.example.composemvvm.bluetoothchat.domain.ConnectionResult
import com.example.composemvvm.bluetoothchat.domain.toBluetoothMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class BluetoothDataTransferService(private val socket: BluetoothSocket) {

    fun listenForIncomingMessages(): Flow<BluetoothMessage> {
        return flow {
            if (!socket.isConnected) {
                return@flow
            }
            val buffer = ByteArray(1024)
            while (true) {
                val byteCount = try {
                    socket.inputStream.read(buffer)
                } catch (e: IOException) {
                    throw TransferDataException()
                }
                emit(
                    buffer.decodeToString(endIndex = byteCount).toBluetoothMessage(
                        isFromLocal = false
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun sendMessage(byte: ByteArray): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                socket.outputStream.write(byte)
            } catch (e: IOException) {
                return@withContext false
            }
            true
        }
    }
}