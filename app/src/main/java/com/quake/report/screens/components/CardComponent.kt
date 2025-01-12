package com.quake.report.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardComponent(title: String, subtitle: String, textShort: String, textLong: String) {

    Card(
        modifier = Modifier.padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7F2F9),
        )
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(10.dp)
                .background(color = Color(0xFFF7F2F9))
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = subtitle, fontSize = 16.sp,
                modifier = Modifier.padding(top = 8.dp),
                fontWeight = FontWeight.Bold
            )

            Text(
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp),
                text = if (expanded) textLong else textShort
            )

            ClickableText(
                modifier = Modifier.align(Alignment.End),
                text = if (expanded) AnnotatedString("Show less") else AnnotatedString("Show more"),
                onClick = {
                    expanded = !expanded
                })
        }
    }
}