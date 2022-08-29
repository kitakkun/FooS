package com.example.foos.ui.view.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.foos.R

@Composable
fun FollowButton(
    onClick: () -> Unit,
    following: Boolean,
) {
    val text =
        if (following) stringResource(id = R.string.following)
        else stringResource(id = R.string.follow)
    RoundButton(onClick = onClick) {
        Text(text = text)
    }

}