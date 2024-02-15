package com.example.composemvvm.screens.newsApp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composemvvm.R
import com.example.composemvvm.utils.Dimens


@Composable
fun ButtonComposable(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick, colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.white),
            contentColor = Color.White
        ), shape = RoundedCornerShape(size = 6.dp), modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.Padding16)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold, color = colorResource(
                    id = R.color.themeColor
                ), fontSize = 16.sp
            ), modifier = Modifier.padding(vertical = Dimens.Padding8)
        )
    }
}

@Composable
fun NewsBackButtons(text: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            color = Color.White
        )
    }
}