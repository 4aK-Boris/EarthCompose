package ru.mpei.ui.screens.charts

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import ru.mpei.ui.Green300
import ru.mpei.ui.Green800
import ru.mpei.ui.Purple700
import ru.mpei.ui.screens.charts.chart.Parameters


@Composable
fun ChartsScreen(paddingValues: PaddingValues, viewModel: ChartsViewModel) {

    val speed by viewModel.speed.collectAsState()
    val angle by viewModel.angle.collectAsState()
    val height by viewModel.height.collectAsState()
    val windSpeed by viewModel.windSpeed.collectAsState()
    val c by viewModel.c.collectAsState()
    val m by viewModel.m.collectAsState()
    val s by viewModel.s.collectAsState()

    val errorState by viewModel.errorState.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(speed, angle, height, windSpeed, c, m, s) {
        viewModel.solve()
    }

    ChartAlertDialog(
        errorState = errorState,
        errorMessage = errorMessage,
        close = viewModel::closeError
    )

    Row(modifier = Modifier.padding(paddingValues = paddingValues).fillMaxSize(), verticalAlignment = Alignment.Top) {

        Box(
            modifier = Modifier.padding(all = 16.dp).fillMaxWidth(fraction = 0.5f).fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Charts(viewModel = viewModel)
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
                    onChangeErrorMessage = viewModel::showError
                )
            }

            item {
                Parameter(
                    value = angle,
                    onValueChanged = viewModel::onAngleChanged,
                    parameter = Parameters.ANGLE,
                    onChangeErrorMessage = viewModel::showError
                )
            }

            item {
                Parameter(
                    value = height,
                    onValueChanged = viewModel::onHeightChanged,
                    parameter = Parameters.HEIGHT,
                    onChangeErrorMessage = viewModel::showError
                )
            }

            item {
                Parameter(
                    value = windSpeed,
                    onValueChanged = viewModel::onWindSpeedChanged,
                    parameter = Parameters.WIND_SPEED,
                    onChangeErrorMessage = viewModel::showError
                )
            }

            item {
                Parameter(
                    value = c,
                    onValueChanged = viewModel::onCChanged,
                    parameter = Parameters.C,
                    onChangeErrorMessage = viewModel::showError
                )
            }

            item {
                Parameter(
                    value = m,
                    onValueChanged = viewModel::onMChanged,
                    parameter = Parameters.M,
                    onChangeErrorMessage = viewModel::showError
                )
            }

            item {
                Parameter(
                    value = s,
                    onValueChanged = viewModel::onSChanged,
                    parameter = Parameters.S,
                    onChangeErrorMessage = viewModel::showError
                )
            }
        }
    }

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
    onChangeErrorMessage: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {

        val (sliderValue, onSliderValueChanged) = remember { mutableStateOf(value = value) }

        LaunchedEffect(key1 = value) {
            onSliderValueChanged(value)
        }

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
                value = sliderValue,
                onValueChange = onSliderValueChanged,
                valueRange = parameter.minValue..parameter.maxValue,
                onValueChangeFinished = {
                    onValueChanged((sliderValue * 1000).toInt().toFloat() / 1000)
                }
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
