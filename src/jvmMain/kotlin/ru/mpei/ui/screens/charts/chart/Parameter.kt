package ru.mpei.ui.screens.charts.chart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp

enum class Parameter(
    val title: String,
    val minValue: Float,
    val maxValue: Float,
    val defaultValue: Float,
    val stepsCount: Int
) {
    SPEED(title = "Начальная скорость", minValue = 10f, maxValue = 25f, defaultValue = 10f, stepsCount = 3) {

        override val fullTitle: AnnotatedString
            get() = buildAnnotatedString {
                append("$title, м / с")
                withStyle(superscript) {
                    append("2")
                }
            }
    },
    ANGLE(
        title = "Угол между вектором скорости и осью X",
        minValue = 30f,
        maxValue = 60f,
        defaultValue = 45f,
        stepsCount = 2
    ) {

        override val fullTitle: AnnotatedString
            get() = buildAnnotatedString {
                append("$title, ")
                withStyle(superscript) {
                    append("°")
                }
            }
    },
    HEIGHT(title = "Начальная высота", minValue = 0f, maxValue = 100f, defaultValue = 0f, stepsCount = 2) {

        override val fullTitle: AnnotatedString
            get() = buildAnnotatedString {
                append("$title, м")
            }
    },
    WIND_SPEED(title = "Скорость ветра", minValue = 0f, maxValue = 20f, defaultValue = 0f, stepsCount = 2) {

        override val fullTitle: AnnotatedString
            get() = buildAnnotatedString {
                append("$title, м / с")
                withStyle(superscript) {
                    append("2")
                }
            }
    },
    C(
        title = "Коэффициент лобового сопротивления",
        minValue = 0.1f,
        maxValue = 0.3f,
        defaultValue = 0.1f,
        stepsCount = 2
    ) {

        override val fullTitle: AnnotatedString
            get() = buildAnnotatedString {
                append(title)
            }
    },
    M(title = "Масса тела", minValue = 1f, maxValue = 4f, defaultValue = 1f, stepsCount = 3) {

        override val fullTitle: AnnotatedString
            get() = buildAnnotatedString {
                append("$title, кг")
            }
    },
    S(title = "Площадь поперечного сечения", minValue = 0.1f, maxValue = 0.3f, defaultValue = 0.1f, stepsCount = 2) {

        override val fullTitle: AnnotatedString
            get() = buildAnnotatedString {
                append("$title, м")
                withStyle(superscript) {
                    append("2")
                }
            }
    };


    abstract val fullTitle: AnnotatedString

    val step: Float
        get() = (maxValue - minValue) / stepsCount

    fun nextValue(value: Float): Float {
        if (value >= maxValue) {
            return maxValue
        } else {
            for (i in 0..stepsCount) {
                val nextStep = minValue + step * i
                if (nextStep > value) {
                    return nextStep
                }
            }
        }
        return value
    }

    fun prevValue(value: Float): Float {
        if (value <= minValue) {
            return minValue
        } else {
            for (i in 0..stepsCount) {
                val prevStep = maxValue - step * i
                if (prevStep < value) {
                    return prevStep
                }
            }
        }
        return value
    }

    val steps: List<Float>
        get() = (0..stepsCount).map { minValue + step * it }

    companion object {
        private val superscript = SpanStyle(
            baselineShift = BaselineShift.Superscript,
            fontSize = 14.sp,
            color = Color.Black
        )

        val colors = listOf(Color.Blue, Color.Red, Color.Green, Color.Magenta)
    }
}
