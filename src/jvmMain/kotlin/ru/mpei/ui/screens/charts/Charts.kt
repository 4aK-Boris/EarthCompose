package ru.mpei.ui.screens.charts

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.launch
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import ru.mpei.ui.Green300
import ru.mpei.ui.Green800
import ru.mpei.ui.screens.charts.chart.Parameter

@Composable
fun Charts(viewModel: ChartsViewModel) {

    val scrollState = rememberLazyListState()

    var itemIndex by remember { mutableStateOf(value = 0) }

    val points by viewModel.result.collectAsState()
    val shadowPoints by viewModel.shadowPoints.collectAsState()
    val steps by viewModel.steps.collectAsState()

    val scope = rememberCoroutineScope()

    val parameter by viewModel.parameter.collectAsState()

    val (size, onSizeChanged) = remember { mutableStateOf(value = IntSize(width = 0, height = 0)) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyRow(
            state = scrollState,
            modifier = Modifier.padding(all = 16.dp).fillMaxWidth().fillMaxHeight(0.9f).onSizeChanged {
                onSizeChanged(it)
            },
            verticalAlignment = Alignment.CenterVertically,
            userScrollEnabled = false
        ) {

            if (points.isNotEmpty()) {

                item {

                    LineChart(size = size,
                        chartName = "Зависимость x от y",
                        points = points.map { it.x to it.y },
                        stepsX = steps.stepsX,
                        stepsY = steps.stepsY,
                        xAxisName = "x",
                        yAxisName = "y",
                        shadowPoints = shadowPoints.map { shadowPoint -> shadowPoint.points.map { it.x to it.y } to shadowPoint.color })
                }

                item {

                    LineChart(size = size,
                        chartName = "Зависимость x от скорости",
                        points = points.map { it.x to it.v },
                        stepsX = steps.stepsX,
                        stepsY = steps.stepsV,
                        xAxisName = "x",
                        yAxisName = "v",
                        shadowPoints = shadowPoints.map { shadowPoint -> shadowPoint.points.map { it.x to it.v } to shadowPoint.color })
                }

                item {

                    LineChart(size = size,
                        chartName = "Зависимость y от скорости",
                        points = points.map { it.y to it.v },
                        stepsX = steps.stepsY,
                        stepsY = steps.stepsV,
                        xAxisName = "y",
                        yAxisName = "v",
                        shadowPoints = shadowPoints.map { shadowPoint -> shadowPoint.points.map { it.y to it.v } to shadowPoint.color })
                }
            }
        }

        Legends(parameters = shadowPoints.map { it.color to it.name })

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Row(
                modifier = Modifier.height(height = 64.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                val leftShape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)
                val rightShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)

                val borderColorLeft by animateColorAsState(
                    targetValue = if (itemIndex > 0) Green800 else Color.Black,
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                )
                val borderColorRight by animateColorAsState(
                    targetValue = if (itemIndex < 2) Green800 else Color.Black,
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                )
                val colorLeft by animateColorAsState(
                    targetValue = if (itemIndex > 0) Green300 else Color.LightGray,
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                )
                val colorRight by animateColorAsState(
                    targetValue = if (itemIndex < 2) Green300 else Color.LightGray,
                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                )

                IconButton(
                    enabled = itemIndex > 0,
                    onClick = {
                        scope.launch {
                            scrollState.scrollToItem(index = --itemIndex)
                        }
                    },
                    modifier = Modifier.width(width = 128.dp).height(height = 48.dp).clip(shape = leftShape)
                        .background(color = colorLeft).border(
                            width = 2.dp, shape = leftShape, brush = Brush.linearGradient(
                                colors = listOf(borderColorLeft, colorLeft),
                                start = Offset(x = 0f, y = 24f),
                                end = Offset(x = 128f, y = 24f)
                            )
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Переключить на левый график"
                    )
                }

                Box(
                    modifier = Modifier.requiredHeight(height = 48.dp).width(width = 2.dp)
                        .background(color = Color.Black)
                )

                IconButton(
                    enabled = itemIndex < 2,
                    onClick = {
                        scope.launch {
                            scrollState.scrollToItem(index = ++itemIndex)
                        }
                    },
                    modifier = Modifier.width(width = 128.dp).height(height = 48.dp).clip(shape = rightShape)
                        .background(color = colorRight).border(
                            width = 2.dp, shape = rightShape, brush = Brush.linearGradient(
                                colors = listOf(borderColorRight, colorRight),
                                start = Offset(x = 128f, y = 24f),
                                end = Offset(x = 0f, y = 24f)
                            )
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Переключить на правый график"
                    )
                }
            }

            ListParameters(parameter = parameter, onParameterChanged = viewModel::onParameterChanged)
        }
    }
}

@Composable
private fun Legends(parameters: List<Pair<Color, String>>) {

    Row(
        modifier = Modifier.fillMaxWidth().padding(all = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        parameters.forEach {
            Legend(color = it.first, name = it.second)
        }
    }
}

@Composable
private fun Legend(color: Color, name: String) {

    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
        Box(modifier = Modifier.height(height = 5.dp).width(width = 32.dp).background(color = color))
        Spacer(modifier = Modifier.height(height = 8.dp))
        Text(text = name)
    }
}

@Composable
private fun ListParameters(parameter: Parameter, onParameterChanged: (Parameter) -> Unit) {
    val (expanded, onExpandedChanged) = remember { mutableStateOf(false) }

    var dropDownWidth by remember { mutableStateOf(0) }

    val icon = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Filled.ArrowDropDown

    Column {
        OutlinedTextField(
            value = parameter.title,
            onValueChange = {},
            modifier = Modifier.width(width = 400.dp).height(height = 64.dp).onSizeChanged {
                dropDownWidth = it.width
            },
            label = { Text("Параметр") },
            trailingIcon = {
                IconButton(onClick = { onExpandedChanged(true) }) {
                    Icon(imageVector = icon, contentDescription = "Открыть")
                }
            },
            singleLine = true,
            readOnly = true
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChanged(false) },
            modifier = Modifier.width(with(LocalDensity.current) { dropDownWidth.toDp() }),
            focusable = true
        ) {
            Parameter.values().forEach { param ->
                DropdownMenuItem(onClick = {
                    onParameterChanged(param)
                    onExpandedChanged(false)
                }) {
                    Text(text = param.title)
                }
            }
        }
    }
}

@Composable
fun LineChart(
    size: IntSize,
    chartName: String,
    xAxisName: String,
    yAxisName: String,
    stepsX: List<Float>,
    stepsY: List<Float>,
    points: List<Pair<Float, Float>>,
    shadowPoints: List<Pair<List<Pair<Float, Float>>, Color>>
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = chartName,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 24.sp,
            fontWeight = FontWeight.W600,
            fontFamily = FontFamily.Serif,
            fontStyle = FontStyle.Italic
        )

        Canvas(
            modifier = Modifier.fillMaxHeight().width(width = size.toSize().width.dp),
        ) {

            val width = drawContext.size.width
            val height = drawContext.size.height

            val xAxisSpace = width / (stepsX.size + 1)
            val yAxisSpace = height / (stepsY.size + 1)

            val paint = Paint().apply {
                color = Color.Black.toArgb()
            }

            val font = Font().apply {
                this.size = xAxisSpace / 4
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

            for (i in 1 until stepsY.size) {
                drawLine(
                    color = Color.LightGray,
                    strokeWidth = 1f,
                    start = Offset(x = 0.9f * xAxisSpace, y = yAxisSpace * i),
                    end = Offset(x = 1.1f * xAxisSpace, yAxisSpace * i)
                )
            }

            for (i in 1 until stepsY.size) {
                drawContext.canvas.nativeCanvas.drawString(
                    s = "%.2f".format(stepsY[i]),
                    x = getTextOffsetVertical(step = xAxisSpace, value = stepsY[i]),
                    y = height - yAxisSpace * (i + 1) + xAxisSpace / 12,
                    font = font,
                    paint = paint
                )
            }

            drawContext.canvas.nativeCanvas.drawString(
                s = yAxisName,
                x = xAxisSpace / 1.5f,
                y = height - yAxisSpace * (stepsY.size + 1) + xAxisSpace / 12,
                font = font,
                paint = paint
            )

            for (i in 1 until stepsX.size) {
                drawLine(
                    color = Color.LightGray,
                    strokeWidth = 1f,
                    start = Offset(x = width - xAxisSpace * i, y = height - 1.1f * yAxisSpace),
                    end = Offset(x = width - xAxisSpace * i, height - 0.9f * yAxisSpace)
                )
            }

            for (i in 1 until stepsX.size) {
                drawContext.canvas.nativeCanvas.drawString(
                    s = "%.2f".format(stepsX[i]),
                    x = xAxisSpace + xAxisSpace * i - getTextOffsetHorizontal(step = xAxisSpace, value = stepsX[i]),
                    y = height - yAxisSpace / 2,
                    font = font,
                    paint = paint
                )
            }

            drawContext.canvas.nativeCanvas.drawString(
                s = xAxisName,
                x = xAxisSpace + xAxisSpace * (stepsX.size) - xAxisSpace / 8,
                y = height - yAxisSpace / 1.5f,
                font = font,
                paint = paint
            )

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
                s = "0", x = xAxisSpace / 2, y = height - yAxisSpace / 2, font = font, paint = paint
            )

            val maxX = shadowPoints.flatMap { it.first }.maxOf { it.first }
            val maxY = shadowPoints.flatMap { it.first }.maxOf { it.second }

            shadowPoints.forEach {
                drawPoints(
                    points = convertToCanvasPoints(
                        points = it.first,
                        maxX = maxX,
                        maxY = maxY,
                        width = width,
                        height = height,
                        xAxisSpace = xAxisSpace,
                        yAxisSpace = yAxisSpace
                    ), pointMode = PointMode.Points, color = it.second, strokeWidth = 0.5f
                )
            }

            drawPoints(
                points = convertToCanvasPoints(
                    points = points,
                    maxX = maxX,
                    maxY = maxY,
                    width = width,
                    height = height,
                    xAxisSpace = xAxisSpace,
                    yAxisSpace = yAxisSpace
                ), pointMode = PointMode.Points, color = Color.Black, strokeWidth = 1f
            )
        }
    }
}

private fun convertToCanvasPoints(
    points: List<Pair<Float, Float>>,
    maxX: Float,
    maxY: Float,
    width: Float,
    height: Float,
    xAxisSpace: Float,
    yAxisSpace: Float
): List<Offset> {
    return points.map {
        Offset(
            x = xAxisSpace + (it.first / maxX) * (width - 2 * xAxisSpace),
            y = height - (it.second / maxY * (height - 2 * yAxisSpace)) - yAxisSpace
        )
    }
}

private fun getTextOffsetHorizontal(step: Float, value: Float): Float {
    return if (value < 10) step / 4
    else if (value < 100) step / 3.3f
    else if (value < 1000) step / 2.9f
    else step / 2
}

private fun getTextOffsetVertical(step: Float, value: Float): Float {
    return if (value < 10) step / 3
    else if (value < 100) step / 5
    else 0f
}
