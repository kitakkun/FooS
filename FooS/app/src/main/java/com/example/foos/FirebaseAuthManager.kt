package com.example.foos

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object FirebaseAuthManager {
    private var auth: FirebaseAuth = Firebase.auth

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