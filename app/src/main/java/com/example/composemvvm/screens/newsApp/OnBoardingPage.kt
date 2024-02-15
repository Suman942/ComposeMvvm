package com.example.composemvvm.screens.newsApp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.composemvvm.utils.Dimens
import com.example.composemvvm.models.newsApp.Page

@Composable
fun OnboardingPage(page: Page, modifier: Modifier = Modifier) {

    Column(modifier = modifier) {
        Image(
            painter = painterResource(id = page.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(Dimens.Padding16))
        Text(
            text = page.title,
            modifier = Modifier.padding(horizontal = Dimens.Padding16, vertical = Dimens.Padding16),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )

        Text(
            text = page.description,
            modifier = Modifier.padding(horizontal = Dimens.Padding16),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}

