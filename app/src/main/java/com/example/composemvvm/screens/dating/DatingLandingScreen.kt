package com.example.composemvvm.screens.dating

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
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
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composemvvm.bluetoothchat.domain.BluetoothDevice
import com.example.composemvvm.bluetoothchat.domain.BluetoothUIState
import com.example.composemvvm.bluetoothchat.presentation.BluetoothViewModel
import com.example.composemvvm.utils.Dimens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatingLandingScreen() {
    val navController = rememberNavController()


    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedItem = Icons.Filled.Home,
            unselectedItem = Icons.Outlined.Home,
            hasNews = false
        ), BottomNavigationItem(
            title = "Email",
            selectedItem = Icons.Filled.Favorite,
            unselectedItem = Icons.Outlined.Favorite,
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
    var selectedItemIndexDrawer by rememberSaveable {
        mutableStateOf(0)
    }
    val scrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(drawerContent = {
        ModalDrawerSheet {
            Spacer(modifier = Modifier.padding(Dimens.Padding16))
            items.forEachIndexed { index, bottomNavigationItem ->
                NavigationDrawerItem(
                    label = { Text(text = bottomNavigationItem.title) },
                    selected = index == selectedItemIndexDrawer,
                    onClick = {
                        selectedItemIndexDrawer = index
                        when(index){
                            0->navController.navigate("home1")
                            1->navController.navigate("home2")
                            2->navController.navigate("home3")
                        }
                        scope.launch { drawerState.close() }
                    },
                    icon = {
                        Icon(
                            imageVector = if (index == selectedItemIndexDrawer) bottomNavigationItem.selectedItem else bottomNavigationItem.unselectedItem,
                            contentDescription = bottomNavigationItem.title
                        )
                    },
                    badge = {
                        bottomNavigationItem.badgeCount?.let {
                            Text(text = bottomNavigationItem.badgeCount.toString())
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }, drawerState = drawerState) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, bottomNavigationItem ->
                        NavigationBarItem(selected = selectedItemIndex == index,
                            onClick = { selectedItemIndex = index
                                      when(index){
                                          0->navController.navigate("home1")
                                          1->navController.navigate("home2")
                                          2->navController.navigate("home3")

                                      }},
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
            },
            topBar = {
                TopAppBar(title = { Text(text = "Dating") }, navigationIcon = {
                    IconButton(onClick = {
//                    if (context is Activity)
//                        context.finish()
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Back")
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
                val paddingValues = it
            NavHost(navController = navController, startDestination = "home1") {
                composable("home1") {
                    DatingHomeFirst(paddingValues)
                }
                composable("home2"){
                    DatingHomeSecond(paddingValues)
                }
                composable("home3"){
                    val viewModel = hiltViewModel<BluetoothViewModel>()
                    val state by viewModel.state.collectAsState()
                    val context = LocalContext.current
                    LaunchedEffect(key1 = state.isConnected){
                        if (state.isConnected) {
                            Toast.makeText(context, "You are connected", Toast.LENGTH_SHORT).show()
                        }
                    }
                    LaunchedEffect(key1 = state.errorMessage){
                        state.errorMessage?.let {
                            Toast.makeText(context,"${state.errorMessage}",Toast.LENGTH_SHORT).show()
                        }
                    }
                    when{
                       state.isConnecting->{
                           Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                               CircularProgressIndicator()
                           }}
                        else ->{
                            DatingHomeThird(paddingValues = paddingValues, state = state, onStartScan = viewModel::startScan, onStopScan =  viewModel::stopScan, onDeviceClick = viewModel::connectToDevice, onStartServer = viewModel::waitForIncomingConnection)
                        }
                    }
                }

            }
        }
    }

}