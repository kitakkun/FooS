package com.example.foos

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import com.example.foos.data.model.database.DatabaseUser
import com.example.foos.data.repository.UsersRepository
import com.example.foos.ui.view.screen.AppScreen
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class MainActivity : ComponentActivity(), CoroutineScope {

    @Inject
    private lateinit var auth: FirebaseAuth

    @Inject
    lateinit var usersRepository: UsersRepository

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    companion object {
        private const val TAG = "MainActivity"
    }


    private val signIn: ActivityResultLauncher<Intent> = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(
                this,
                getString(R.string.message_successfully_signed_in),
                Toast.LENGTH_SHORT
            ).show()
            auth.currentUser?.let {
                launch {
                    if (usersRepository.fetchByUserId(userId = it.uid) == null) {
                        usersRepository.create(
                            DatabaseUser(
                                userId = it.uid,
                                username = it.displayName ?: "user",
                                profileImage = ""
                            )
                        )
                    }
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.message_sign_in_error), Toast.LENGTH_LONG)
                .show()
            val response = result.idpResponse
            if (response == null) {
                Log.w(TAG, "Sign in canceled")
            } else {
                Log.w(TAG, "Sign in error", response.error)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setLogo(R.mipmap.ic_launcher)
                .setTheme(R.style.Theme_FooS)
                .setAvailableProviders(
                    listOf(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build(),
                    )
                )
                .build()
            signIn.launch(signInIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppScreen()
        }
    }
}

