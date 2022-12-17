package ru.mpei.ui.screens.charts

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.mpei.ui.screens.charts.chart.EquationSystem
import ru.mpei.ui.screens.charts.chart.Parameters
import ru.mpei.ui.screens.charts.chart.Steps
import ru.mpei.ui.screens.charts.chart.Variables

class ChartsViewModel {

    private val equationSystem = EquationSystem()

    private val _errorState: MutableStateFlow<Boolean> = MutableStateFlow(value = false)
    private val _errorMessage: MutableStateFlow<String> = MutableStateFlow(value = "")

    val errorState: StateFlow<Boolean> = _errorState
    val errorMessage: StateFlow<String> = _errorMessage

    private val _speed: MutableStateFlow<Float> = MutableStateFlow(value = Parameters.SPEED.defaultValue)
    private val _angle: MutableStateFlow<Float> = MutableStateFlow(value = Parameters.ANGLE.defaultValue)
    private val _height: MutableStateFlow<Float> = MutableStateFlow(value = Parameters.HEIGHT.defaultValue)
    private val _windSpeed: MutableStateFlow<Float> =
        MutableStateFlow(value = Parameters.WIND_SPEED.defaultValue)
    private val _c: MutableStateFlow<Float> = MutableStateFlow(value = Parameters.C.defaultValue)
    private val _m: MutableStateFlow<Float> = MutableStateFlow(value = Parameters.M.defaultValue)
    private val _s: MutableStateFlow<Float> = MutableStateFlow(value = Parameters.S.defaultValue)
    private val _result: MutableStateFlow<List<Variables>> = MutableStateFlow(emptyList())

    val speed: StateFlow<Float> = _speed
    val angle: StateFlow<Float> = _angle
    val height: StateFlow<Float> = _height
    val windSpeed: StateFlow<Float> = _windSpeed
    val c: StateFlow<Float> = _c
    val m: StateFlow<Float> = _m
    val s: StateFlow<Float> = _s
    val result: StateFlow<List<Variables>> = _result

    fun showError(message: String) {
        _errorMessage.value = message
        _errorState.value = true
    }

    fun closeError() {
        _errorState.value = false
    }

    fun onSpeedChanged(value: Float) {
        _speed.value = value
    }

    fun onAngleChanged(value: Float) {
        _angle.value = value
    }

    fun onHeightChanged(value: Float) {
        _height.value = value
    }

    fun onWindSpeedChanged(value: Float) {
        _windSpeed.value = value
    }

    fun onCChanged(value: Float) {
        _c.value = value
    }

    fun onMChanged(value: Float) {
        _m.value = value
    }

    fun onSChanged(value: Float) {
        _s.value = value
    }

    fun solve() {
        _result.value = equationSystem.solve(
            speed = speed.value,
            angle = angle.value,
            height = height.value,
            windSpeed = windSpeed.value,
            c = c.value,
            m = m.value,
            s = s.value
        )
    }

    val steps: Steps
        get() {
            val max = maxVariables(variables = result.value)
            val min = minVariables(variables = result.value)
            val step = Variables(
                x = max.x step min.x,
                y = max.y step min.y,
                u = max.u step min.u,
                v = max.v step min.v
            )
            val stepCountX = stepsCount(max.x)
            val stepCountY = stepsCount(max.y)
            val stepCountU = stepsCount(max.u)
            val stepCountV = stepsCount(max.v)
            return Steps(
                stepsX = (0..stepCountX).map { min.x + it * step.x },
                stepsY = (0..stepCountY).map { min.y + it * step.y },
                stepsU = (0..stepCountU).map { min.u + it * step.u },
                stepsV = (0..stepCountV).map { min.v + it * step.v },
            )
        }

    private fun stepsCount(value: Float): Int {
        return if (value < 100f) 9
        else if (value < 1000f) 7
        else 5
    }

    private infix fun Float.step(value: Float): Float = (this - value) / stepsCount(value = this)

    private fun maxVariables(variables: List<Variables>): Variables {
        val maxX = variables.maxOf { it.x }
        val maxY = variables.maxOf { it.y }
        val maxU = variables.maxOf { it.u }
        val maxV = variables.maxOf { it.v }
        return Variables(x = maxX, y = maxY, u = maxU, v = maxV)
    }

    private fun minVariables(variables: List<Variables>): Variables {
        val minX = variables.minOf { it.x }
        val minY = variables.minOf { it.y }
        val minU = variables.minOf { it.u }
        val minV = variables.minOf { it.v }
        return Variables(x = minX, y = minY, u = minU, v = minV)
    }

    companion object {
        private const val STEPS_COUNT = 9
    }
}