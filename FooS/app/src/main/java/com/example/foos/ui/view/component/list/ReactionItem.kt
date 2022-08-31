package com.example.foos.ui.view.component.list

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
import com.example.foos.R
import com.example.foos.ui.constants.paddingMedium
import com.example.foos.ui.constants.paddingSmall
import com.example.foos.ui.state.screen.reaction.ReactionItemUiState
import com.example.foos.ui.view.component.UserIcon

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

@Preview(showBackground = true)
@Composable
private fun ReactionItemPreview() {
    val uiState = ReactionItemUiState(
        username = "username",
        userIcon = "",
        reaction = "'emoji'",
        postContent = "some interesting post content..."
    )
    ReactionItem(uiState = uiState)
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
