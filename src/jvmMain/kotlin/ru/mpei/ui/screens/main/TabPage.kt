package ru.mpei.ui.screens.main

import androidx.compose.ui.graphics.Color
import ru.mpei.ui.Cacao
import ru.mpei.ui.Green300
import ru.mpei.ui.Green800
import ru.mpei.ui.LightCacao
import ru.mpei.ui.Purple100
import ru.mpei.ui.Purple500

enum class TabPage(val backgroundColor: Color, val borderColor: Color) {
    CHARTS(backgroundColor = Green300, borderColor = Green800),
    MODEL(backgroundColor = Purple100, borderColor = Purple500),
    ABOUT(backgroundColor = LightCacao, borderColor = Cacao)
}