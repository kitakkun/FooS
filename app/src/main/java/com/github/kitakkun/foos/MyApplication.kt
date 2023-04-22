package com.github.kitakkun.foos

import android.app.Application
import android.util.Log
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = BundledEmojiCompatConfig(applicationContext)
        EmojiCompat.init(config)

        startKoin {
            modules(appModule)
            androidContext(this@MyApplication)
            androidLogger()
        }

        val firestore: FirebaseFirestore by inject()
        val firebaseAuth: FirebaseAuth by inject()

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
