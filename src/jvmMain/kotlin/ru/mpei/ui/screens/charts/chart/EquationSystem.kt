package ru.mpei.ui.screens.charts.chart

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class EquationSystem {

    fun solve(
        speed: Float,
        angle: Float,
        height: Float,
        windSpeed: Float,
        c: Float,
        m: Float,
        s: Float
    ): List<Variables> {
        var variables = Variables(x = 0f, y = height, u = angle.angleToPi(), v = speed)
        val list = mutableListOf(variables)
        do {
            val buffer = Variables(
                x = variables.x + h * fx(v = variables.v, u = variables.u, windSpeed = windSpeed),
                y = variables.y + h * fy(v = variables.v, u = variables.u),
                u = variables.u + h * fu(v = variables.v, u = variables.u),
                v = variables.v + h * fv(v = variables.v, u = variables.u, m = m, c = c, s = s)
            )
            list.add(buffer)
            variables = buffer
        } while (variables.y > 0)
        return list.map {
            it.copy(u = it.u.angleToDegree())
        }
    }

    private fun fx(v: Float, u: Float, windSpeed: Float): Float = v * cos(u) + windSpeed

    private fun fy(v: Float, u: Float): Float = v * sin(u)

    private fun fu(v: Float, u: Float): Float = -(g / v) * cos(u)

    private fun fv(v: Float, u: Float, m: Float, c: Float, s: Float): Float =
        (-d(v = v, c = c, s = s) - m * g * sin(u)) / m

    private fun d(v: Float, c: Float, s: Float): Float = c * p * s * v.pow(2) / 2

    private fun Float.angleToPi() = this * PI.toFloat() / PI180

    private fun Float.angleToDegree() = this * PI180 / PI.toFloat()

    companion object {
        private const val h = 0.0001f
        private const val g = 9.80665f
        private const val p = 1.29f

        private const val PI180 = 180f
    }
}
