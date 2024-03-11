package com.example.composemvvm.screens.dating

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.Coil
import coil.compose.AsyncImage
import coil.util.CoilUtils
import com.example.composemvvm.R
import com.example.composemvvm.bluetoothchat.domain.BluetoothDevice
import com.example.composemvvm.bluetoothchat.domain.BluetoothUIState
import com.example.composemvvm.models.dating.ListItem
import com.example.composemvvm.utils.Dimens
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DatingHomeFirst(paddingValues: PaddingValues) {

    val images = listOf(
        R.drawable.slider_one,
        R.drawable.slider_two,
        R.drawable.slider_three,
        R.drawable.slider_one,
        R.drawable.slider_two,
        R.drawable.slider_three
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        images.size
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        HorizontalPager(state = pagerState,
            key = { images[it] }) { index ->
            Image(
                painter = painterResource(id = images[index]), contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }

}

@Composable
fun DatingHomeSecond(paddingValues: PaddingValues) {
    val items: List<ListItem> = listOf(
        ListItem.TextItem(1, "Text 1"),
        ListItem.VideoItem(3, "video-url"),
        ListItem.ImageItem(
            2,
            "https://images.pexels.com/photos/371589/pexels-photo-371589.jpeg?cs=srgb&dl=clouds-conifer-daylight-371589.jpg&fm=jpg"
        ),
        ListItem.TextItem(4, "Text 2"),
        ListItem.ImageItem(
            5,
            "https://th.bing.com/th/id/OIP.-15_9rp1nRdFBCqLgpdtTgHaEK?rs=1&pid=ImgDetMain"
        ),

        )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        items(items) { item ->
            when (item) {
                is ListItem.TextItem -> TextItem(item)
                is ListItem.ImageItem -> ImageItem(item)
                is ListItem.VideoItem -> VideoItem(item)
            }
        }
    }

}


@Composable
fun TextItem(item: ListItem.TextItem) {
    // Composable for text item
    Text(text = item.text, modifier = Modifier.padding(16.dp))
}

@Composable
fun ImageItem(item: ListItem.ImageItem) {
    // Composable for image item
    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(text = "Image: ${item.imageUrl}", modifier = Modifier.padding(16.dp))
            AsyncImage(
                model = item.imageUrl, contentDescription = null, modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1280f / 840f)
            )
        }
    }
}

@Composable
fun VideoItem(item: ListItem.VideoItem) {
    // Composable for video item
    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(text = "Video: ${item.videoUrl}", modifier = Modifier.padding(16.dp))
            Image(
                painter = painterResource(id = R.drawable.slider_two), contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Composable
fun DatingHomeThird(
    paddingValues: PaddingValues,
    state: BluetoothUIState,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    onDeviceClick: (BluetoothDevice) -> Unit,
    onStartServer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

    ) {
        val context = LocalContext.current

        BluetoothDeviceList(pairedDeviceList = state.pairedDevices, scannedDeviceList =state.scannedDevices ,
            onDeviceClick,
            modifier = Modifier.fillMaxWidth().weight(1f))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {onStartScan()}) {
                Text(text = "Scan", color = Color.White)
            }
            Button(onClick = {onStopScan()}) {
                Text(text = "Stop", color = Color.White)
            }
            Button(onClick = onStartServer) {
                Text(text = "Start Server", color = Color.White)
            }
        }

    }
}

@Composable
fun BluetoothDeviceList(
    pairedDeviceList: List<BluetoothDevice>,
    scannedDeviceList: List<BluetoothDevice>,
    onDeviceClick:  (BluetoothDevice) -> Unit, modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LazyColumn(modifier = modifier) {
        item{
            Text(text = "Paired device", fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = modifier.padding(16.dp))
        }

        items(pairedDeviceList){ device ->
            Text(text = device.name ?: "No name", fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { onDeviceClick(device)  }
                    .padding(16.dp))
        }

        item{
            Text(text = "Scanned device", fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = modifier.padding(16.dp))
        }

        items(scannedDeviceList){ device ->
            Text(text = device.name ?: "No name", fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = modifier
                    .fillMaxWidth()
                    .clickable { Toast.makeText(context,device.name,Toast.LENGTH_SHORT).show()
                    onDeviceClick(device)}
                    .padding(16.dp))
        }
    }
}

