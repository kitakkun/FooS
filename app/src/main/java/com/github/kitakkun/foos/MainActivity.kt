package com.github.kitakkun.foos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.github.kitakkun.foos.common.navigation.ScreenRouter
import com.github.kitakkun.foos.common.navigation.UserScreenRouter
import com.github.kitakkun.foos.ui.screen.AppScreen
import com.google.firebase.auth.FirebaseAuth
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : ComponentActivity(), KoinComponent {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firebaseAuth: FirebaseAuth by inject()
        setContent {
            AppScreen(
                startDestination = when (firebaseAuth.currentUser == null) {
                    true -> UserScreenRouter.Auth.route
                    false -> ScreenRouter.Main.route
                }
            )
        }
    }
}
