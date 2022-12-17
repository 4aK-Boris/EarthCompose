package ru.mpei.ui.screens.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import ru.mpei.ui.Green300
import ru.mpei.ui.Green800
import ru.mpei.ui.Purple700
import ru.mpei.ui.screens.charts.chart.EquationSystem
import ru.mpei.ui.screens.charts.chart.Parameters


@Composable
fun ChartsScreen(paddingValues: PaddingValues, viewModel: ChartsViewModel) {

    val speed by viewModel.speed.collectAsState(initial = EquationSystem.SPEED)
    val angle by viewModel.angle.collectAsState(initial = EquationSystem.ANGLE)
    val height by viewModel.height.collectAsState(initial = EquationSystem.HEIGHT)
    val windSpeed by viewModel.windSpeed.collectAsState(initial = EquationSystem.WIND_SPEED)
    val c by viewModel.c.collectAsState(initial = EquationSystem.C)
    val m by viewModel.m.collectAsState(initial = EquationSystem.M)
    val s by viewModel.s.collectAsState(initial = EquationSystem.S)

    val errorState by viewModel.errorState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    ChartAlertDialog(
        errorState = errorState,
        errorMessage = errorMessage,
        close = viewModel::closeError
    )

    Row(modifier = Modifier.padding(paddingValues = paddingValues).fillMaxSize(), verticalAlignment = Alignment.Top) {

        val (size, onSizeChanged) = remember { mutableStateOf(value = IntSize(width = 0, height = 0)) }

        Box(modifier = Modifier.padding(all = 16.dp).fillMaxWidth(fraction = 0.5f).fillMaxHeight().onSizeChanged {
            onSizeChanged(it)
        }) {
            TestChart()
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(fraction = 1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            item {
                Text(
                    text = "Параметры модели",
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.W800,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            item {
                Parameter(
                    value = speed,
                    onValueChanged = viewModel::onSpeedChanged,
                    parameter = Parameters.SPEED,
                    onChangeErrorMessage = viewModel::showError,
                    solve = viewModel::solve
                )
            }

            item {
                Parameter(
                    value = angle,
                    onValueChanged = viewModel::onAngleChanged,
                    parameter = Parameters.ANGLE,
                    onChangeErrorMessage = viewModel::showError,
                    solve = viewModel::solve
                )
            }

            item {
                Parameter(
                    value = height,
                    onValueChanged = viewModel::onHeightChanged,
                    parameter = Parameters.HEIGHT,
                    onChangeErrorMessage = viewModel::showError,
                    solve = viewModel::solve
                )
            }

            item {
                Parameter(
                    value = windSpeed,
                    onValueChanged = viewModel::onWindSpeedChanged,
                    parameter = Parameters.WIND_SPEED,
                    onChangeErrorMessage = viewModel::showError,
                    solve = viewModel::solve
                )
            }

            item {
                Parameter(
                    value = c,
                    onValueChanged = viewModel::onCChanged,
                    parameter = Parameters.C,
                    onChangeErrorMessage = viewModel::showError,
                    solve = viewModel::solve
                )
            }

            item {
                Parameter(
                    value = m,
                    onValueChanged = viewModel::onMChanged,
                    parameter = Parameters.M,
                    onChangeErrorMessage = viewModel::showError,
                    solve = viewModel::solve
                )
            }

            item {
                Parameter(
                    value = s,
                    onValueChanged = viewModel::onSChanged,
                    parameter = Parameters.S,
                    onChangeErrorMessage = viewModel::showError,
                    solve = viewModel::solve
                )
            }
        }
    }

}

@Composable
fun ListChart(viewModel: ChartsViewModel) {

    val scrollState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    val result by viewModel.result.collectAsState()

    LaunchedEffect(true) {
        viewModel.solve()
    }

    LazyRow(
        state = scrollState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {

//        if (result.isNotEmpty()) {
//
//            item {
//
//                LineChart(
//                    chartName = "Зависимость x от y",
//                    points = result.map { it.x to it.y },
//                    xSteps = viewModel.steps.stepsX,
//                    ySteps = viewModel.steps.stepsY
//                )
//
//                SnapHelper(scrollState = scrollState, coroutineScope = coroutineScope)
//            }
//
//            item {
//
//                LineChart(
//                    chartName = "Зависимость x от скорости",
//                    points = result.map { it.x to it.v },
//                    xSteps = viewModel.steps.stepsX,
//                    ySteps = viewModel.steps.stepsV
//                )
//
//                SnapHelper(scrollState = scrollState, coroutineScope = coroutineScope)
//            }
//
//            item {
//
//                LineChart(
//                    chartName = "Зависимость y от скорости",
//                    points = result.map { it.y to it.v },
//                    xSteps = viewModel.steps.stepsY,
//                    ySteps = viewModel.steps.stepsV
//                )
//
//                SnapHelper(scrollState = scrollState, coroutineScope = coroutineScope)
//            }
//
//            item {
//
//                LineChart(
//                    chartName = "Зависимость скорости от угла",
//                    points = result.map { it.v to it.u },
//                    xSteps = viewModel.steps.stepsV,
//                    ySteps = viewModel.steps.stepsU
//                )
//
//                SnapHelper(scrollState = scrollState, coroutineScope = coroutineScope)
//            }
    }
}

@Composable
private fun SnapHelper(scrollState: LazyListState, coroutineScope: CoroutineScope) {
    if (!scrollState.isScrollInProgress) {
        if (scrollState.isHalfPastItemLeft())
            coroutineScope.scrollBasic(scrollState, left = true)
        else
            coroutineScope.scrollBasic(scrollState)

        if (scrollState.isHalfPastItemRight())
            coroutineScope.scrollBasic(scrollState)
        else
            coroutineScope.scrollBasic(scrollState, left = true)
    }
}

private fun CoroutineScope.scrollBasic(listState: LazyListState, left: Boolean = false) {
    launch {
        val pos =
            if (left) listState.firstVisibleItemIndex else listState.firstVisibleItemIndex + 1
        listState.animateScrollToItem(pos)
    }
}

private const val HALF = 500

@Composable
private fun LazyListState.isHalfPastItemRight(): Boolean {
    return remember { derivedStateOf { firstVisibleItemScrollOffset } }.value > HALF
}

@Composable
private fun LazyListState.isHalfPastItemLeft(): Boolean {
    return remember { derivedStateOf { firstVisibleItemScrollOffset } }.value <= HALF
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ChartAlertDialog(
    errorState: Boolean,
    errorMessage: String,
    close: () -> Unit
) {
    if (errorState) {
        AlertDialog(
            modifier = Modifier.width(width = 256.dp).height(height = 196.dp),
            onDismissRequest = close,
            title = {
                Text(
                    text = "Ошибка!",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            text = { Text(text = errorMessage) },
            buttons = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                    Button(
                        onClick = close,
                        modifier = Modifier.padding(all = 16.dp)
                    ) {
                        Text("OK", fontSize = 22.sp)
                    }
                }
            },
            shape = RoundedCornerShape(size = 16.dp)
        )
    }
}

@Composable
private fun Parameter(
    parameter: Parameters,
    value: Float,
    onValueChanged: (Float) -> Unit,
    onChangeErrorMessage: (String) -> Unit,
    solve: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {

        ParameterTextField(value = value.toString(), onValueChanged = {
            try {
                onValueChanged(it.toFloat())
            } catch (e: NumberFormatException) {
                onChangeErrorMessage("Число введено в неверном формате")
            }
        }, inc = {
            onValueChanged(
                if (value + parameter.step > parameter.maxValue) {
                    parameter.maxValue
                } else {
                    value + parameter.step
                }
            )
        }, dec = {
            onValueChanged(
                if (value - parameter.step < parameter.minValue) {
                    parameter.minValue
                } else {
                    value - parameter.step
                }
            )
        })

        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(all = 16.dp)
        ) {
            Text(
                text = parameter.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Normal,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Slider(
                value = value,
                onValueChange = {
                    onValueChanged((it * 1000).toInt().toFloat() / 1000)
                },
                valueRange = parameter.minValue..parameter.maxValue,
                onValueChangeFinished = solve
            )
        }
    }
}

@Composable
private fun ParameterTextField(value: String, onValueChanged: (String) -> Unit, inc: () -> Unit, dec: () -> Unit) {

    Row(
        modifier = Modifier.border(
            width = 2.dp, color = Purple700, shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
        ), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChanged,
            modifier = Modifier.width(width = 128.dp),
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.W600,
                fontSize = 24.sp
            ),
            singleLine = true
        )
        Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.background(color = Green300)) {
            IconButton(onClick = inc, modifier = Modifier.size(size = 24.dp)) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Увеличить зачение",
                    tint = Green800,
                    modifier = Modifier.size(size = 24.dp)
                )
            }
            IconButton(onClick = dec, modifier = Modifier.size(size = 24.dp)) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Уменьшить зачение",
                    tint = Green800,
                    modifier = Modifier.size(size = 24.dp)
                )
            }
        }
    }
}