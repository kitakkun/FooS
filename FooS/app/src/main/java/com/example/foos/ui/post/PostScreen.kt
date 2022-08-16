package com.example.foos.ui.post

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.foos.R

@Composable
fun PostScreen(viewModel: PostViewModel, navController: NavController) {
    val uiState by viewModel.postUiState.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents())
    {
        viewModel.setImages(it)
    }

    PostUI(
        onCanceled = { navController.navigateUp() },
        onSent = { viewModel.post() },
        onAddImagesBtnClicked = {launcher.launch("image/*")},
        uiState = uiState,
        onTextUpdate = { viewModel.onTextFieldUpdated(it) },
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PostUI(
    uiState: PostUiState = PostUiState("", listOf()),
    onCanceled: () -> Unit = {},
    onSent: () -> Unit = {},
    onAddImagesBtnClicked: () -> Unit = {},
    onTextUpdate: (String) -> Unit = {},
) {
    Column {
        PostUITopRow(
            confirmText = "Send",
            onCanceled = onCanceled,
            onConfirmed = onSent
        )
        OutlinedTextField(
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            value = uiState.content,
            onValueChange = onTextUpdate,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            singleLine = false,
        )
        AttachedImagesRow(attachedImages = uiState.attachedImages)
        ToolBar(
            onAddImagesBtnClicked = onAddImagesBtnClicked
        )
    }
}

@Composable
fun AttachedImagesRow(
    attachedImages: List<String>
) {
    LazyRow() {
        items(attachedImages) {
            AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                .data(it).build(), contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .clipToBounds()
            )
        }
    }
}

@Preview
@Composable
fun PostUITopRow(
    confirmText: String = "Confirm",
    onCanceled: () -> Unit = {},
    onConfirmed: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onCanceled) {
            Icon(Icons.Default.Close, contentDescription = "cancel")
        }
        Button(
            onClick = onConfirmed,
            shape = RoundedCornerShape(50)
        ) {
            Text(confirmText)
        }
    }
}

@Preview
@Composable
fun ToolBar(
    onAddImagesBtnClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onAddImagesBtnClicked
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add images",
            )
        }
        IconButton(
            onClick = { /*TODO*/ }
        ) {
            Icon(
                painterResource(R.drawable.ic_pin_drop),
                contentDescription = "Add location",
            )
        }
    }
}