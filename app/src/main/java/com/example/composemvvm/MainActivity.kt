package com.example.composemvvm

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composemvvm.screens.dating.DatingLandingScreen
import com.example.composemvvm.screens.dating.DatingOnBoardingScreen
import com.example.composemvvm.screens.newsApp.OnBoardingScreen
import com.example.composemvvm.screens.practice.DetailScreen
import com.example.composemvvm.screens.practice.HomeScreen
import com.example.composemvvm.ui.theme.ComposeMVVMTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            ComposeMVVMTheme {
                App(this)
            }
        }
    }
}

@Composable
fun App(baseContext: Context) {
//  DemoPractice()
    DatingNavGraphComposable(baseContext)
}
@Composable
fun DatingNavGraphComposable(baseContext: Context) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "onBoarding") {
        composable("onBoarding") {
            DatingOnBoardingScreen("0",1f, onProgressUpdated = {},modifier = Modifier, navigateHome = {navController.navigate("home")}, popBackStack = {navController.popBackStack()})
        }
        composable("home"){
            DatingLandingScreen(navController = navController,baseContext)
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


