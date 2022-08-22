package com.example.foos.ui.navargs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post(
    val imageUris: List<String>
) : Parcelable
