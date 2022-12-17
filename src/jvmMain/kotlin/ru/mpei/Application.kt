package ru.mpei

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.mpei.ui.screens.charts.ChartsViewModel
import ru.mpei.ui.screens.main.MainScreen

class Application: KoinComponent {

    val chartsViewModel by inject<ChartsViewModel>()

    @Composable
    fun App() {
        MaterialTheme {
            MainScreen(chartsViewModel = chartsViewModel)
        }
    }

}