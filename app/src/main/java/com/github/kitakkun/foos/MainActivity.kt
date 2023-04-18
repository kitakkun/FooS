package com.github.kitakkun.foos

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.github.kitakkun.foos.common.navigation.ScreenRouter
import com.github.kitakkun.foos.common.navigation.UserScreenRouter
import com.github.kitakkun.foos.ui.screen.AppScreen
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val mutableIsAuthorized: MutableState<Boolean> = mutableStateOf(false)
    private val isAuthorized: State<Boolean> = mutableIsAuthorized

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth.addAuthStateListener { authState ->
            mutableIsAuthorized.value = authState.currentUser != null
            Log.d("MainActivity", "sign-in state changed: $authState")
        }

        setContent {
            AppScreen(
                startDestination = when (isAuthorized.value) {
                    true -> ScreenRouter.Main.route
                    false -> UserScreenRouter.Auth.route
                }
            )
        }
    }
}
