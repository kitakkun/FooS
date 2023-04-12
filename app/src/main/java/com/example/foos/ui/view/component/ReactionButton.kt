package com.example.foos.ui.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foos.R
import com.example.foos.ui.PreviewContainer

/**
 * リアクション追加用ボタン
 * @param onReactionClicked リアクション追加時の挙動
 */
@Composable
fun ReactionButton(
    onReactionClicked: (String) -> Unit,
    onReactionRemoved: () -> Unit,
    myReaction: String?,
) {
    var expanded by remember { mutableStateOf(false) }
    if (myReaction == null) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(painterResource(R.drawable.ic_add_reaction), null)
            ReactionDropdown(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                onReactionClicked = { onReactionClicked(it) }
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colors.surface, shape = CircleShape)
                .background(MaterialTheme.colors.surface)
                .clickable { onReactionRemoved() },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = myReaction,
                fontSize = 25.sp,
            )
        }
    }
}

@Preview
@Composable
private fun ReactionButtonPreview() = PreviewContainer {
    ReactionButton(onReactionClicked = {}, onReactionRemoved = { }, myReaction = null)
}
