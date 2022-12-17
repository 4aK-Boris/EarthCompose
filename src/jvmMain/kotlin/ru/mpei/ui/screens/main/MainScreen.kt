package ru.mpei.ui.screens.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ru.mpei.ui.screens.about.AboutScreen
import ru.mpei.ui.screens.charts.ChartsScreen
import ru.mpei.ui.screens.charts.ChartsViewModel
import ru.mpei.ui.screens.model.ModelScreen

@Composable
fun MainScreen(chartsViewModel: ChartsViewModel) {

    val (tabPage, onTabPageChanged) = remember { mutableStateOf(value = TabPage.CHARTS) }

    Scaffold(
        bottomBar = {
            BottomBar(tabPage = tabPage, onTabPageChanged = onTabPageChanged)
        }
    ) { paddingValues ->
        Crossfade(targetState = tabPage) { screen ->
            when (screen) {
                TabPage.ABOUT -> AboutScreen(paddingValues = paddingValues)
                TabPage.MODEL -> ModelScreen(paddingValues = paddingValues)
                TabPage.CHARTS -> ChartsScreen(paddingValues = paddingValues, viewModel = chartsViewModel)
            }
        }
    }
}