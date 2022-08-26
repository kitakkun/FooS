package com.example.foos.ui.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RoundButtonRow(
    titles: List<String>,
    defaultColor: Color,
    selectedColor: Color,
    onClick: (String) -> Unit = {},
) {
    var selectedIndex by remember { mutableStateOf(0) }
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(titles) { index, title ->
            Button(
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = if (index == selectedIndex) selectedColor else defaultColor
                ),
                onClick = {
                    selectedIndex = index
                    onClick(title)
                },
                shape = RoundedCornerShape(50)
            ) {
                Text(text = title)
            }
        }
    }
}

@Preview
@Composable
fun FlexButtonsPreview() {
    RoundButtonRow(
        titles = listOf("title1", "title2", "title3"),
        defaultColor = Color.DarkGray,
        selectedColor = Color.LightGray,
    )
}