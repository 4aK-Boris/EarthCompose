package ru.mpei.ui.screens.charts.chart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

enum class Parameters(val minValue: Float, val maxValue: Float, val defaultValue: Float, val step: Float) {
    SPEED(minValue = 0f, maxValue = 100f, defaultValue = 10f, step = 5f) {

        override val title: AnnotatedString
            get() = buildAnnotatedString {
                append("Начальная скорость, м / с")
                withStyle(superscript) {
                    append("2")
                }
            }
    },
    ANGLE(minValue = 0f, maxValue = 90f, defaultValue = 45f, step = 3f) {

        override val title: AnnotatedString
            get() = buildAnnotatedString {
                append("Угол между вектором скорости и осью X, ")
                withStyle(superscript) {
                    append("°")
                }
            }
    },
    HEIGHT(minValue = 0f, maxValue = 100f, defaultValue = 0f, step = 10f) {

        override val title: AnnotatedString
            get() = buildAnnotatedString {
                append("Начальная высота, м")
            }
    },
    WIND_SPEED(minValue = 0f, maxValue = 20f, defaultValue = 0f, step = 2f) {

        override val title: AnnotatedString
            get() = buildAnnotatedString {
                append("Скорость ветра, м / с")
                withStyle(superscript) {
                    append("2")
                }
            }
    },
    C(minValue = 0f, maxValue = 1f, defaultValue = 0.1f, step = 0.05f) {

        override val title: AnnotatedString
            get() = buildAnnotatedString {
                append("Коэффициент лобового сопротивления")
            }
    },
    M(minValue = 0f, maxValue = 100f, defaultValue = 1f, step = 5f) {

        override val title: AnnotatedString
            get() = buildAnnotatedString {
                append("Масса тела, кг")
            }
    },
    S(minValue = 0f, maxValue = 1f, defaultValue = 0.1f, step = 0.05f) {

        override val title: AnnotatedString
            get() = buildAnnotatedString {
                append("Площадь поперечного сечения, м")
                withStyle(superscript) {
                    append("2")
                }
            }
    };


    abstract val title: AnnotatedString

    companion object {
        private val superscript = SpanStyle(
            baselineShift = BaselineShift.Superscript,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}
