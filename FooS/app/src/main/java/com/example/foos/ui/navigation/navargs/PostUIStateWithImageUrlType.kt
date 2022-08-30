package com.example.foos.ui.navigation.navargs

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson

val PostItemUiStateWithImageUrlType: NavType<PostItemUiStateWithImageUrl> =
    object : NavType<PostItemUiStateWithImageUrl>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): PostItemUiStateWithImageUrl? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): PostItemUiStateWithImageUrl {
            return Gson().fromJson(value, PostItemUiStateWithImageUrl::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: PostItemUiStateWithImageUrl) {
            bundle.putParcelable(key, value)
        }
    }
