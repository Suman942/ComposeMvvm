package com.example.composemvvm.models.newsApp

import androidx.annotation.DrawableRes
import com.example.composemvvm.R

data class Page(val title: String, val description: String, @DrawableRes val image: Int)

val pages = listOf<Page>(
    Page(
        "Loreum Picus",
        "The XML code you provided seems to be related to the configuration of a splash screen in an Android app. The issue you are facing with the logo",
        image = R.drawable.onboarding1
    ),
    Page(
        "Loreum Picus",
        "The XML code you provided seems to be related to the configuration of a splash screen in an Android app. The issue you are facing with the logo",
        image = R.drawable.onboarding2
    ),
    Page(
        "Loreum Picus",
        "The XML code you provided seems to be related to the configuration of a splash screen in an Android app. The issue you are facing with the logo",
        image = R.drawable.onboarding3
    )
)
