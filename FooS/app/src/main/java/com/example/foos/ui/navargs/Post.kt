package com.example.foos.ui.navargs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Post(
    val imageUris: List<String>
) : Parcelable
