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
import androidx.compose.ui.unit.dp
import com.example.foos.R
import com.example.foos.ui.state.screen.reaction.ReactionItemUiState
import com.example.foos.ui.view.component.UserIcon

@Preview
@Composable
fun ReactionItem(
    uiState: ReactionItemUiState = ReactionItemUiState.Default
) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        UserIcon(url = uiState.userIcon)
        Spacer(modifier = Modifier.width(16.dp))
        Column() {
            val text = stringResource(R.string.reaction_message, uiState.username, uiState.reaction)
            WordEmText(text = text, emIndices = listOf(0))
            Text(
                text = uiState.postContent
            )
        }
    }
}

@Composable
fun WordEmText(
    text: String,
    emIndices: List<Int>,
) {
    val texts = text.split(" ")
    var first = true
    Text(
        buildAnnotatedString {
            for (i in texts.indices) {
                if (!first) {
                    append(" ")
                }
                if (i in emIndices) {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(texts[i])
                    }
                } else {
                    append(texts[i])
                }
                first = false
            }
        }
    )
}
