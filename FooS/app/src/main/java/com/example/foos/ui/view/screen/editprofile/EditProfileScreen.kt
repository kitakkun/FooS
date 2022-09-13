package com.example.foos.ui.view.screen.editprofile

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foos.R
import com.example.foos.ui.state.editprofile.EditProfileScreenUiState
import com.example.foos.ui.theme.FooSTheme
import com.example.foos.ui.view.component.UserIcon

@Composable
fun EditProfileScreen(viewModel: EditProfileViewModel) {

    val uiState = viewModel.uiState.value

    EditProfileUI(uiState = uiState, onValueChange = { key, value -> viewModel.update(key, value) })
}

@Composable
private fun EditProfileUI(
    uiState: EditProfileScreenUiState,
    onCanceled: () -> Unit = {},
    onCompleted: () -> Unit = {},
    onValueChange: (Int, String) -> Unit = { _, _ -> },
) {
    val editItemHeaders = mapOf(
        R.string.name to uiState.username,
        R.string.bio to uiState.bio,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.edit_profile)) },
                navigationIcon = {
                    IconButton(onClick = onCanceled) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = onCompleted) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                UserIcon(url = uiState.profileImage)
            }
            items(editItemHeaders.toList()) {
                EditItem(
                    header = it.first,
                    value = it.second,
                    onValueChange = { value -> onValueChange(it.first, value) }
                )
            }
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

@Preview
@Composable
private fun EditProfileUIPreview() {
    FooSTheme {
        val uiState = EditProfileScreenUiState.Default
        EditProfileUI(uiState = uiState)
    }
}
