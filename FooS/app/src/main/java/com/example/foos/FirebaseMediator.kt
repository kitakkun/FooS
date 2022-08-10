package com.example.foos

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

object FirebaseMediator {
    private var auth: FirebaseAuth = Firebase.auth
    private var db: FirebaseDatabase = Firebase.database("https://foos-bdebd-default-rtdb.asia-southeast1.firebasedatabase.app")
    private var storage: FirebaseStorage = Firebase.storage

    fun checkSignInState(context: Context) {
        if (auth.currentUser == null) {
            context.startActivity(
                Intent(context, SignInActivity::class.java)
            )
            val activity = context as? Activity
            activity?.finish()
        }
    }

    fun logOut() {
        FirebaseAuth.getInstance().signOut()
    }
}