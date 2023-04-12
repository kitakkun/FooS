package com.example.foos.ui.view.component

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.foos.R
import com.example.foos.ui.PreviewContainer

/**
 * Reactionを行うためのドロップダウン
 * @param expanded ドロップダウンを開くかどうか
 * @param onDismissRequest ドロップダウンメニューの範囲外をタップしたときの挙動
 * @param onReactionClicked リアクションが行われた際の挙動
 */
@Composable
fun ReactionDropdown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onReactionClicked: (String) -> Unit,
) {
    val reactions = listOf(
        stringResource(id = R.string.emoji_like),
        stringResource(id = R.string.emoji_yummy),
        stringResource(id = R.string.emoji_fire),
    )

    val dropdownWidth = with(LocalDensity.current) {
        60.sp.toDp()
    }

    MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(45))) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = Modifier.width(dropdownWidth)
        ) {
            reactions.forEach {
                DropdownMenuItem(onClick = { onReactionClicked.invoke(it) }) {
                    Text(
                        text = it,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ReactionDropdownPreview() = PreviewContainer {
    ReactionDropdown(expanded = true, onDismissRequest = { }, onReactionClicked = {})
}
