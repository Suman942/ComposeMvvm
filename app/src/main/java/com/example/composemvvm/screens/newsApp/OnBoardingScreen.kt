package com.example.composemvvm.screens.newsApp

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.composemvvm.utils.Dimens
import com.example.composemvvm.models.newsApp.pages
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen() {
    Column(modifier = Modifier.fillMaxWidth()) {
        val pagerState = rememberPagerState(initialPage = 0) {
            pages.size
        }

        val buttonState = remember {
            derivedStateOf {
                when (pagerState.currentPage) {
                    0 -> listOf("", "Next")
                    1 -> listOf("Back", "Next")
                    2 -> listOf("Back", "Get started")
                    else -> listOf("", "")
                }
            }
        }

        HorizontalPager(state = pagerState) { index ->
            OnboardingPage(page = pages[index])
        }
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier

                .fillMaxWidth()
                .padding(horizontal = Dimens.Padding16)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PageIndicator(
                modifier = Modifier.width(Dimens.PageIndicatorWidth),
                pageSize = pages.size,
                selectedPage = pagerState.currentPage
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                val scope = rememberCoroutineScope()
                if (buttonState.value[0].isNotEmpty()) {
                    NewsBackButtons(text = buttonState.value[0],
                        onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } }
                    )
                }
                ButtonComposable(text = buttonState.value[1],
                    onClick = {scope.launch {
                        if (pagerState.currentPage == 3){
                        // next screen
                        }else{
                            pagerState.animateScrollToPage(pagerState.currentPage +1)
                        }
                    }})
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))


    }
}