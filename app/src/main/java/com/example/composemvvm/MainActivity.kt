package com.example.composemvvm

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composemvvm.bluetoothchat.presentation.BluetoothViewModel
import com.example.composemvvm.screens.dating.DatingHomeFirst
import com.example.composemvvm.screens.dating.DatingLandingScreen
import com.example.composemvvm.screens.dating.DatingOnBoardingScreen
import com.example.composemvvm.screens.newsApp.OnBoardingScreen
import com.example.composemvvm.screens.practice.DetailScreen
import com.example.composemvvm.screens.practice.HomeScreen
import com.example.composemvvm.ui.theme.ComposeMVVMTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {

        }
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissions[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else {
                true
            }
            if (canEnableBluetooth && !isBluetoothEnabled){
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }

        }
        setContent {
            LaunchedEffect(true){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        permissionLauncher.launch(arrayOf(Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH_SCAN))
                    }else{
                        permissionLauncher.launch(arrayOf(Manifest.permission.BLUETOOTH,Manifest.permission.ACCESS_COARSE_LOCATION))
                    }

            }
            ComposeMVVMTheme {
                App(this)
            }
        }
    }
}

@Composable
fun App(baseContext: Context) {
    DatingNavGraphComposable(baseContext)
}

@Composable
fun DatingNavGraphComposable(baseContext: Context) {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<BluetoothViewModel>()
    val state by viewModel.state.collectAsState()
    NavHost(navController = navController, startDestination = "onBoarding") {
        composable("onBoarding") {
            DatingOnBoardingScreen(
                "0",
                1f,
                onProgressUpdated = {},
                modifier = Modifier,
                navigateHome = { navController.navigate("home") },
                popBackStack = { navController.popBackStack() })
        }
        composable("home") {
            DatingLandingScreen(state, viewModel::startScan, viewModel::stopScan)
        }

    }
}

@Composable
fun NewsAppOnBoardingComposable() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        OnBoardingScreen()
    }
}


@Composable
fun DemoPractice() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "poets") {
        composable("poets") {
            HomeScreen {
                navController.navigate("detail/$it/data")
            }
        }
        composable("detail/{poets}/{test}",
            arguments = listOf(navArgument("poets") {
                type = NavType.StringType
            },
                navArgument("test") {
                    type = NavType.StringType
                }
            )
        ) {
            val poetsName = it.arguments?.getString("poets") ?: ""
            val test_ = it.arguments?.getString("test") ?: ""
            Log.d("NavData", "data: $test_")
            DetailScreen(poetsName)
        }
    }
}


