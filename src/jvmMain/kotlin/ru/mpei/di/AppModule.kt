package ru.mpei.di

import org.koin.dsl.module
import ru.mpei.ui.screens.charts.ChartsViewModel

val appModule = module {

    single {
        ChartsViewModel()
    }

}