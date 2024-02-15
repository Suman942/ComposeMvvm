package com.example.composemvvm.screens.dating

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.composemvvm.models.dating.BottomNavigationItem
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import com.example.composemvvm.utils.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatingLandingScreen(navController: NavController, context: Context) {

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedItem = Icons.Filled.Home,
            unselectedItem = Icons.Outlined.Home,
            hasNews = false
        ), BottomNavigationItem(
            title = "Email",
            selectedItem = Icons.Filled.Email,
            unselectedItem = Icons.Outlined.Email,
            hasNews = true
        ), BottomNavigationItem(
            title = "Chat",
            selectedItem = Icons.Filled.Chat,
            unselectedItem = Icons.Outlined.Chat,
            hasNews = false,
            badgeCount = 10
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection), bottomBar = {
        NavigationBar {
            items.forEachIndexed { index, bottomNavigationItem ->
                NavigationBarItem(selected = selectedItemIndex == index,
                    onClick = { selectedItemIndex = index },
                    label = { Text(text = bottomNavigationItem.title) },
                    icon = {
                        BadgedBox(badge = {
                            if (bottomNavigationItem.badgeCount != null) {
                                Badge {
                                    Text(text = bottomNavigationItem.badgeCount.toString())
                                }
                            } else if (bottomNavigationItem.hasNews) {
                                Badge()
                            }
                        }) {
                            Icon(
                                imageVector = if (selectedItemIndex == index) bottomNavigationItem.selectedItem
                                else bottomNavigationItem.unselectedItem,
                                contentDescription = bottomNavigationItem.title
                            )
                        }
                    })
            }
        }
    }, topBar = {
        TopAppBar(title = { Text(text = "Dating") }, navigationIcon = {
            IconButton(onClick = {
                if (context is Activity)
                    context.finish()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }, actions = {
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favourite")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }, scrollBehavior = scrollBehaviour)
    }) { it ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            items(100) {
                Text(text = "Item $it", modifier = Modifier.padding(Dimens.Padding16))
            }
        }
    }
}