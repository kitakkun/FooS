package com.github.kitakkun.foos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.kitakkun.foos.common.navigation.MainScreen
import com.github.kitakkun.foos.common.navigation.SubScreen
import com.github.kitakkun.foos.ui.screen.AppScreen
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startDestination = when (firebaseAuth.currentUser) {
            null -> SubScreen.Auth.route
            else -> MainScreen.Home.route
        }

        setContent {
            AppScreen(
                startDestination = startDestination,
            )
        }
    }
}
