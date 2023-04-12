package com.example.foos

import android.app.Application
import android.util.Log
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {

    @Inject lateinit var firestore: FirebaseFirestore
    @Inject lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate() {
        super.onCreate()
        val config = BundledEmojiCompatConfig(applicationContext)
        EmojiCompat.init(config)

        // RUNS ONLY ON DEBUG MODE
        // Firebase Emulator Settings For Testing.
        // 10.0.2.2 is the host machine's IP address in the Android emulator.
        // See https://developer.android.com/studio/run/emulator-networking.html
        // port numbers are defined in firebase.json
        if (BuildConfig.DEBUG) {
            Log.d("MainActivity", "Firebase Emulator Settings For Testing.")
            firestore.useEmulator("10.0.2.2", 8080)
            firebaseAuth.useEmulator("10.0.2.2", 9099)
        }
    }
    
}
