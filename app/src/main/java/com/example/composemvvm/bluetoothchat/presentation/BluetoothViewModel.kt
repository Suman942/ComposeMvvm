package com.example.composemvvm.bluetoothchat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composemvvm.bluetoothchat.domain.BluetoothController
import com.example.composemvvm.bluetoothchat.domain.BluetoothDeviceDomain
import com.example.composemvvm.bluetoothchat.domain.BluetoothUIState
import com.example.composemvvm.bluetoothchat.domain.ConnectionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(private val bluetoothController: BluetoothController) :
    ViewModel() {



    private val _state = MutableStateFlow(BluetoothUIState())
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state
    ) { scannedDevices, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
            messages = if (state.isConnected) state.messages else emptyList()
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)


    init {
        bluetoothController.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)
        bluetoothController.error.onEach { error ->
            _state.update { it.copy(errorMessage = error) }
        }.launchIn(viewModelScope)
    }
    private var deviceConnectionJob: Job? = null
    fun connectToDevice(device: BluetoothDeviceDomain) {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController.connectionToDevice(device).listen()
    }

    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()
        _state.update {
            it.copy(
                isConnecting = false,
                isConnected = false
            )
        }
    }

    fun waitForIncomingConnection(){
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController.startBluetoothServer().listen()
    }

    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController
            .stopDiscovery()
    }

    fun sendMessage(message:String){
        viewModelScope.launch {
            val bluetoothMessage = bluetoothController.trySendMessage(message)
            if (bluetoothMessage != null){
                _state.update { it.copy(
                    messages = it.messages + bluetoothMessage
                ) }
            }
        }
    }

    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when (result) {
                ConnectionResult.ConnectionEstablished -> {
                    _state.update {
                        it.copy(
                            isConnected = true,
                            isConnecting = false,
                            errorMessage = null
                        )
                    }
                }
                is ConnectionResult.TransferSucceeded ->{
                    _state.update { it.copy(
                        messages = it.messages + result.message
                    ) }
                }

                is ConnectionResult.Error -> {
                    _state.update {
                        it.copy(
                            isConnected = false,
                            isConnecting = false,
                            errorMessage = result.message
                        )
                    }
                }
                else ->{

                }
            }
        }.catch { throwable ->
            bluetoothController.closeConnection()
            _state.update {
                it.copy(
                    isConnected = false,
                    isConnecting = false,
                )
            }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothController.release()
    }
}