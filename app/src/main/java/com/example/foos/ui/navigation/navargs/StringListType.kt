package com.example.foos.ui.navigation.navargs

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

/**
 * NavArgumentでStringのリストを渡すための型
 */
@Parcelize
data class StringList(
    val value: List<String>
) : Parcelable

val StringListType: NavType<StringList> =
    object : NavType<StringList>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): StringList? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): StringList {
            return Gson().fromJson(value, StringList::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: StringList) {
            bundle.putParcelable(key, value)
        }
    }
