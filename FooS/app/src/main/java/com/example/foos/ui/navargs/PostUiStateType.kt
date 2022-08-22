package com.example.foos.ui.navargs

import android.os.Bundle
import androidx.navigation.NavType
import com.example.foos.ui.state.screen.home.PostItemUiState
import com.google.gson.Gson

val PostType: NavType<PostItemUiState> = object : NavType<PostItemUiState>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): PostItemUiState? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): PostItemUiState {
        return Gson().fromJson(value, PostItemUiState::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: PostItemUiState) {
        bundle.putParcelable(key, value)
    }
}