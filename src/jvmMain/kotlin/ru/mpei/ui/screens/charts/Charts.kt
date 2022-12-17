package ru.mpei.ui.screens.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint

@Composable
fun Charts(viewModel: ChartsViewModel) {

    val scrollState = rememberLazyListState()

    LazyRow(
        state = scrollState,
        modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        userScrollEnabled = false
    ) {

        if (result.isNotEmpty()) {

            item {

                LineChart(
                    chartName = "Зависимость x от y",
                    points = result.map { it.x to it.y },
                    xSteps = viewModel.steps.stepsX,
                    ySteps = viewModel.steps.stepsY
                )

                SnapHelper(scrollState = scrollState, coroutineScope = coroutineScope)
            }

            item {

                LineChart(
                    chartName = "Зависимость x от скорости",
                    points = result.map { it.x to it.v },
                    xSteps = viewModel.steps.stepsX,
                    ySteps = viewModel.steps.stepsV
                )

                SnapHelper(scrollState = scrollState, coroutineScope = coroutineScope)
            }

            item {

                LineChart(
                    chartName = "Зависимость y от скорости",
                    points = result.map { it.y to it.v },
                    xSteps = viewModel.steps.stepsY,
                    ySteps = viewModel.steps.stepsV
                )

                SnapHelper(scrollState = scrollState, coroutineScope = coroutineScope)
            }

            item {

                LineChart(
                    chartName = "Зависимость скорости от угла",
                    points = result.map { it.v to it.u },
                    xSteps = viewModel.steps.stepsV,
                    ySteps = viewModel.steps.stepsU
                )

                SnapHelper(scrollState = scrollState, coroutineScope = coroutineScope)
            }
        }
    }
}

@Composable
fun LineChart(
    stepsX: List<Int>,
    stepsY: List<Int>,
    points: List<Pair<Float, Float>>
) {

    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {

        val width = drawContext.size.width
        val height = drawContext.size.height

        println("width = $width")
        println("height = $height")
        println()

        val count = 10

        val xAxisSpace = width / (count + 1)
        val yAxisSpace = height / (count + 1)

        val stepsX = listOf(999, 1, 8, 400, 1242, 9898, 70, 80, 90, 100)

        val paint = Paint().apply {
            color = Color.Black.toArgb()
        }

        val font = Font().apply {
            size = xAxisSpace / 4
        }

        drawLine(
            Color.LightGray,
            Offset(x = xAxisSpace, y = 0f),
            Offset(x = xAxisSpace, y = height - yAxisSpace),
            strokeWidth = 1f
        )

        drawLine(
            color = Color.LightGray,
            start = Offset(x = xAxisSpace, y = height - yAxisSpace),
            end = Offset(x = width, y = height - yAxisSpace),
            strokeWidth = 1f
        )

        for (i in 1 until count) {
            drawLine(
                color = Color.LightGray,
                strokeWidth = 1f,
                start = Offset(x = 0.9f * xAxisSpace, y = yAxisSpace * i),
                end = Offset(x = 1.1f * xAxisSpace, yAxisSpace * i)
            )
        }

        for (i in 1 until count) {
            drawContext.canvas.nativeCanvas.drawString(
                s = stepsX[i - 1].toString(),
                x = xAxisSpace / 3 + getTextOffsetVertical(step = xAxisSpace, value = stepsX[i - 1]),
                y = height - yAxisSpace * (i + 1) + xAxisSpace / 12,
                font = font,
                paint = paint
            )
        }

        for (i in 1 until count) {
            drawLine(
                color = Color.LightGray,
                strokeWidth = 1f,
                start = Offset(x = width - xAxisSpace * i, y = height - 1.1f * yAxisSpace),
                end = Offset(x = width - xAxisSpace * i, height - 0.9f * yAxisSpace)
            )
        }

        for (i in 1 until count) {
            drawContext.canvas.nativeCanvas.drawString(
                s = stepsX[i - 1].toString(),
                x = xAxisSpace + xAxisSpace * i - getTextOffsetHorizontal(step = xAxisSpace, value = stepsX[i - 1]),
                y = height - yAxisSpace / 2,
                font = font,
                paint = paint
            )
        }

        drawLine(
            color = Color.LightGray,
            strokeWidth = 1f,
            start = Offset(x = xAxisSpace, y = 0f),
            end = Offset(x = 1.1f * xAxisSpace, yAxisSpace / 5)
        )

        drawLine(
            color = Color.LightGray,
            strokeWidth = 1f,
            start = Offset(x = xAxisSpace, y = 0f),
            end = Offset(x = 0.9f * xAxisSpace, yAxisSpace / 5)
        )

        drawLine(
            color = Color.LightGray,
            strokeWidth = 1f,
            start = Offset(x = width, y = height - yAxisSpace),
            end = Offset(x = width - 0.2f * xAxisSpace, height - 1.1f * yAxisSpace)
        )

        drawLine(
            color = Color.LightGray,
            strokeWidth = 1f,
            start = Offset(x = width, y = height - yAxisSpace),
            end = Offset(x = width - 0.2f * xAxisSpace, height - 0.9f * yAxisSpace)
        )

        drawContext.canvas.nativeCanvas.drawString(
            s = "0",
            x = xAxisSpace / 4,
            y = height - yAxisSpace / 4,
            font = font,
            paint = paint
        )

        val maxX = points.maxOf { it.first }
        val maxY = points.maxOf { it.second }

        val chartPoints = points.map {
            val pointX = xAxisSpace + (it.first / maxX) * (width - xAxisSpace)
            val pointY = height - (it.second / maxY * (height - yAxisSpace)) - yAxisSpace
            Offset(x = pointX, y = pointY)
        }

        drawPoints(points = chartPoints, pointMode = PointMode.Points, color = Color.Blue, strokeWidth = 1f)
    }
}

private fun getTextOffsetHorizontal(step: Float, value: Int): Float {
    return if (value < 10) step / 14
    else if (value < 100) step / 8
    else if (value < 1000) step / 5
    else step / 3.7f
}

private fun getTextOffsetVertical(step: Float, value: Int): Float {
    return if (value < 10) step / 2.5f
    else if (value < 100) step / 4
    else if (value < 1000) step / 8
    else 0f
}
