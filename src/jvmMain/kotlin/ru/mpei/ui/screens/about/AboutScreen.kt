package ru.mpei.ui.screens.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AboutScreen(paddingValues: PaddingValues) {

    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(height = 32.dp))
        AboutText(name = "Группа", text = "А-13м-21")
        AboutText(name = "Студент", text = "Лосев Дмитрий")
        AboutText(name = "Тема", text = "Движение у поверхности земли")
        AboutText(name = "Среда разработки", text = "Intellij Idea Ultimate")
        AboutText(name = "Язык программирования", text = "Kotlin")
    }
}

@Composable
private fun AboutText(name: String, text: String) {

    val textValue = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Black)) {
            append("$name: ")
        }
        withStyle(style = SpanStyle(color = Color.Gray)) {
            append(text)
        }
    }

    Text(
        text = textValue,
        modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
        fontStyle = FontStyle.Normal,
        fontSize = 16.sp,
        fontWeight = FontWeight.W600,
        fontFamily = FontFamily.Serif
    )
}