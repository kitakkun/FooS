package com.example.foos.ui.view.screen.editprofile

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.foos.R
import com.example.foos.ui.view.component.UserIcon

@Composable
fun EditProfileScreen(viewModel: EditProfileViewModel) {

    val uiState = viewModel.uiState.collectAsState()
    val editItemHeaders = mapOf(
        R.string.name to uiState.value.username,
        R.string.bio to uiState.value.bio,
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            UserIcon(url = uiState.value.profileImage)
        }
        items(editItemHeaders.toList()) {
            EditItem(
                header = it.first,
                value = it.second,
                onValueChange = { value -> viewModel.update(key = it.first, value = value) }
            )
        }
    }
}

@Composable
fun EditItem(
    @StringRes header: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(stringResource(id = header))
        },
        modifier = modifier.fillMaxWidth()
    )
}