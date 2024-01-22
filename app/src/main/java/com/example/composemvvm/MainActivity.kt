package com.example.composemvvm

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composemvvm.screens.DetailScreen
import com.example.composemvvm.screens.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "poets") {
        composable("poets") {
            HomeScreen {
                navController.navigate("detail/$it/dada")
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
