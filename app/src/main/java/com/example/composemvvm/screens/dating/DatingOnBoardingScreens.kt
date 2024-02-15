package com.example.composemvvm.screens.dating

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composemvvm.R
import com.example.composemvvm.screens.newsApp.ButtonComposable
import com.example.composemvvm.utils.Dimens

@Composable
fun DatingOnBoardingScreen(
    page: String,
    progress: Float,
    onProgressUpdated: (Float) -> Unit,
    modifier: Modifier,
    navigateHome: () -> Unit,
    popBackStack: () -> Unit
) {
    val _progress = remember { mutableStateOf(progress) }
    val navController = rememberNavController()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.dating_onboard_bg),
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentScale = ContentScale.Crop
        )
        Column(modifier.fillMaxWidth()) {
            ProgressBar(progress = _progress.value)
            NavHost(navController = navController, startDestination = page) {
                composable("0") {
                    DatingOnBoardingWelcomeScreen(onClick = {
                        _progress.value = _progress.value + 1
                        onProgressUpdated(_progress.value)
                        Log.d("MainScreenProgress", "60 Compose: $progress  $_progress.value")
                        navController.popBackStack()
                        navController.navigate("1")
                    })
                }
                composable("1") {
                    DatingOnBoardingPreference(onClick = { it ->
                        _progress.value = _progress.value + 1
                        onProgressUpdated(_progress.value)
                        popBackStack()
                        navigateHome()
                    })
                }

            }

        }
    }
}


@Composable
fun DatingOnBoardingPreference(modifier: Modifier = Modifier, onClick: (String) -> Unit) {
    Column {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
        )

        Text(
            text = "What are your dating preferences?",
            color = Color.White,
            style = TextStyle(fontSize = 28.sp),
            fontWeight = FontWeight.Medium,
            modifier = modifier.padding(horizontal = Dimens.Padding16)
        )
        Text(
            text = "This will helps you to find an better match for you",
            color = colorResource(id = R.color.onBoardingTextColor),
            style = TextStyle(
                fontSize = 14.sp,
                color = colorResource(id = R.color.onBoardingTextColor)
            ),
            fontWeight = FontWeight.Normal,
            modifier = modifier.padding(Dimens.Padding16)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(16.dp)
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick("Casual") }
                    .border(2.dp, colorResource(id = R.color.progressBarTrackColor))

            ) {
                Text(
                    text = "Casual",
                    modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.Center),
                    style = TextStyle(fontSize = 16.sp, color = Color.White)
                )
            }
            Box(
                modifier = modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick("Hobby") }
                    .border(2.dp, colorResource(id = R.color.progressBarTrackColor))
            ) {
                Text(
                    text = "Hobby",
                    modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.Center),
                    style = TextStyle(fontSize = 16.sp, color = Color.White)
                )
            }
            Box(
                modifier = modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick("Short Term") }
                    .border(2.dp, colorResource(id = R.color.progressBarTrackColor))
            ) {
                Text(
                    text = "Short Term",
                    modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.Center),
                    style = TextStyle(fontSize = 16.sp, color = Color.White)
                )
            }
            Box(
                modifier = modifier
                    .fillMaxWidth(0.8f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onClick("Long Term") }
                    .border(2.dp, colorResource(id = R.color.progressBarTrackColor))
            ) {
                Text(
                    text = "Long Term",
                    modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.Center),
                    style = TextStyle(fontSize = 16.sp, color = Color.White)
                )
            }

        }
    }
}

@Composable
fun DatingOnBoardingWelcomeScreen(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
        )
        Box(
            modifier = modifier
                .padding(Dimens.Padding16)
                .width(54.dp)
                .height(54.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.dating_onboard_heart_drawable),
                contentDescription = null
            )
        }
        Text(
            text = "Let’s begin\nwith Dating journey",
            color = Color.White,
            style = TextStyle(fontSize = 28.sp),
            fontWeight = FontWeight.Medium,
            modifier = modifier.padding(horizontal = Dimens.Padding16)
        )
        Text(
            text = "All information shared in dating section, will not be shared anywhere else in Netclan.",
            color = colorResource(id = R.color.onBoardingTextColor),
            style = TextStyle(
                fontSize = 14.sp,
                color = colorResource(id = R.color.onBoardingTextColor)
            ),
            fontWeight = FontWeight.Normal,
            modifier = modifier.padding(Dimens.Padding16)
        )
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.8f)
        )
        ButtonComposable(text = "Let's Started", onClick = onClick)
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.1f)
        )
    }

}

@Composable
fun ProgressBar(progress: Float, modifier: Modifier = Modifier) {
    Log.d("MainScreenProgress", "58 Compose: $progress")
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.02f)
    )
    val maxProgress = 2
    val progressValue = progress.toFloat() / maxProgress.toFloat()
    Log.d("Progess", "value: $progress  $progressValue")
    LinearProgressIndicator(
        progress = progressValue,
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp)
            .padding(horizontal = Dimens.Padding8)
            .clip(RoundedCornerShape(4.dp)),
        trackColor = colorResource(id = R.color.progressBarTrackColor),
        color = Color.White
    )


}
