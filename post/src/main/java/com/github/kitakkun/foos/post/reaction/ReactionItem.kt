package com.github.kitakkun.foos.post.reaction

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.github.kitakkun.foos.common.const.paddingMedium
import com.github.kitakkun.foos.common.const.paddingSmall
import com.github.kitakkun.foos.customview.composable.user.UserIcon
import com.github.kitakkun.foos.customview.preview.PreviewContainer
import com.github.kitakkun.foos.customview.theme.FooSTheme
import com.github.kitakkun.foos.post.R

/**
 * リアクションリストのアイテム
 */
@Composable
fun ReactionItem(
    uiState: ReactionItemUiState,
    modifier: Modifier = Modifier,
    onContentClick: (String) -> Unit = {},
    onUserIconClick: (String) -> Unit = {},
) {
    Row(
        modifier = modifier.padding(paddingMedium)
    ) {
        UserIcon(url = uiState.userIcon)
        Spacer(modifier = Modifier.width(paddingMedium))
        Column {
            WordEmText(
                text = stringResource(
                    R.string.reaction_message,
                    uiState.username,
                    uiState.reaction
                ),
                emIndices = listOf(0)
            )
            Spacer(modifier = Modifier.height(paddingSmall))
            Text(text = uiState.postContent)
        }
    }
}

@Preview
@Composable
private fun ReactionItemPreview() = PreviewContainer {
    val uiState = ReactionItemUiState(
        username = "username",
        userIcon = "",
        reaction = "'emoji'",
        postContent = "some interesting post content..."
    )
    FooSTheme {
        ReactionItem(uiState = uiState)
    }
}

@Composable
fun WordEmText(
    text: String,
    emIndices: List<Int>,
) {
    val words = text.split(" ")
    Text(
        buildAnnotatedString {
            words.forEachIndexed { i, word ->
                if (i != 0) {
                    append(" ")
                }
                if (i in emIndices) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(word)
                    }
                } else {
                    append(word)
                }
            }
        }
    )
}
