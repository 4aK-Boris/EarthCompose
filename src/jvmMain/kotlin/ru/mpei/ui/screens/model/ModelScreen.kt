package ru.mpei.ui.screens.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ModelScreen(paddingValues: PaddingValues) {

    val superscript = SpanStyle(
        baselineShift = BaselineShift.Superscript,
        fontSize = 14.sp,
        color = Color.Black
    )

    val subscript = SpanStyle(
        baselineShift = BaselineShift.Subscript,
        fontSize = 14.sp,
        color = Color.Black
    )

    val modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
    val enumerationModifier = Modifier.padding(start = 64.dp, end = 32.dp, bottom = 12.dp)

    LazyColumn(
        modifier = Modifier.padding(paddingValues = paddingValues),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        userScrollEnabled = true
    ) {
        item {
            Spacer(modifier = Modifier.height(height = 16.dp))
        }
        item {
            TitleText(text = "Математическая модель движения тела, брошенного под углом у горизонту")
        }
        item {
            OrdinaryText(
                text = "       Движение тела, брошенного (запущенного) под углом к горизонту," +
                        " может быть описано системой дифференциальных уравнений:",
                modifier = modifier
            )
        }
        item {
            Image(
                painter = painterResource(resourcePath = "equations.png"),
                contentDescription = "Система дифференциальных уравнений",
                modifier = modifier.height(height = 196.dp)
            )
        }
        item {
            OrdinaryText(text = "В этой системе уравнений:", modifier = modifier)
        }
        item {
            EnumerationText(text = "x(t), y(t) – текущие координаты тела", modifier = enumerationModifier)
        }
        item {
            EnumerationText(
                text = "θ(t) – угол между вектором скорости и линией горизонта",
                modifier = enumerationModifier
            )
        }
        item {
            EnumerationText(text = "m – масса тела", modifier = enumerationModifier)
        }
        item {
            EnumerationText(text = "w – скорость ветра", modifier = enumerationModifier)
        }
        item {
            EnumerationText(text = buildAnnotatedString {
                append("g = 9,81 м / с")
                withStyle(style = superscript) {
                    append("2")
                }
                append(" – ускорение свободного падения")
            }, modifier = enumerationModifier)
        }
        item {
            EnumerationText(text = buildAnnotatedString {
                append("D = cp * (sv")
                withStyle(style = superscript) {
                    append("2")
                }
                append(") / 2 – аэродинамическое сопротивление, в котором\n")
                append("c ≤ 1 – коэффициент сопротивления,\n")
                append("s – площадь поперечного сопротивления тела,\n")
                append("p = 1,29 кг / м")
                withStyle(style = superscript) {
                    append("3")
                }
                append(" – плотность воздуха.")
            }, modifier = enumerationModifier)
        }
        item {
            OrdinaryText(
                text = "        Задать массу m и начальные значения x, y, θ, v." +
                        " Решить численно полученную задачу Коши," +
                        " построить графики зависимости решения от времени," +
                        " а также фазовый портрет решения (в плоскости (x, y))." +
                        " Вычисления следует прекращать при достижении телом поверхности земли.",
                modifier = modifier
            )
        }
        item {
            Spacer(modifier = Modifier.height(height = 32.dp))
        }
        item {
            TitleText(text = "Описание компьютерной модели")
        }
        item {
            OrdinaryText(text = "       Компьютерная модель позволяет визуализировать дижение тела," +
                    " брошенного под углом к горизонту," +
                    " а так же позволяет наблюдать за его поведением эпидемии при изменении параметров," +
                    " описанных в математической модели.", modifier = modifier)
        }
        item {
            Spacer(modifier = Modifier.height(height = 16.dp))
        }
    }
}

@Composable
private fun EnumerationText(text: String, modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .offset(y = 3.dp)
                .clip(shape = CircleShape)
                .size(size = 8.dp)
                .background(color = Color.Black)
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        OrdinaryText(text = text)
    }
}

@Composable
private fun EnumerationText(text: AnnotatedString, modifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .offset(y = 12.dp)
                .clip(shape = CircleShape)
                .size(size = 8.dp)
                .background(color = Color.Black)
        )
        Spacer(modifier = Modifier.width(width = 8.dp))
        OrdinaryText(text = text)
    }
}

@Composable
private fun OrdinaryText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 20.sp,
        fontFamily = FontFamily.Serif,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.W400,
        lineHeight = 27.sp
    )
}

@Composable
private fun OrdinaryText(text: AnnotatedString, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 20.sp,
        fontFamily = FontFamily.Serif,
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.W400,
        lineHeight = 27.sp
    )
}

@Composable
private fun TitleText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp).fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 24.sp,
        fontFamily = FontFamily.Serif,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.W600
    )
}

