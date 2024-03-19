package com.example.composemvvm.bluetoothchat.domain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ChatScreen(paddingValues: PaddingValues,
               state: BluetoothUIState, onDisConnect: () -> Unit, sendMessages: (String) -> Unit) {
    val message = rememberSaveable {
        mutableStateOf("")
    }
    val keyboardControlled = LocalSoftwareKeyboardController.current
    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Messages", modifier = Modifier.weight(1f))
            IconButton(onClick = onDisConnect) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "disconnect")
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            items(state.messages) { message ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    ChatMessage(
                        message = message,
                        modifier = Modifier
                            .align(if (message.isFromLocal) Alignment.End else Alignment.Start))
                }

            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically){
            TextField(value = message.value, onValueChange = {message.value = it},
                modifier = Modifier.weight(1f), placeholder = {Text(text = "message")})

            IconButton(onClick = {sendMessages(message.value)
                message.value = ""
            keyboardControlled?.hide()}) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "send message")
            }
        }

    }
}

@Composable
fun ChatMessage(message: BluetoothMessage, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = if (message.isFromLocal) 15.dp else 0.dp,
                    topEnd = 15.dp,
                    bottomStart = 15.dp,
                    bottomEnd = if (message.isFromLocal) 0.dp else 15.dp
                )
            )
            .background(if (message.isFromLocal) Color.DarkGray else Color.Gray)
            .padding(8.dp)
    ) {
        Text(text = message.senderName, fontSize = 10.sp, color = Color.White)
        Text(text = message.message, color = Color.White, modifier = modifier.widthIn(250.dp))
    }
}

@Preview
@Composable
fun ChatMessagePreview() {
    ChatMessage(
        message = BluetoothMessage(
            message = "Hello",
            senderName = "Pixel",
            isFromLocal = false
        )
    )
}