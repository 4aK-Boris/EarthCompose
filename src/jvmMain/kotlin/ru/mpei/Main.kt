package ru.mpei

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin
import ru.mpei.di.appModule


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Движение у поверхности земли",
        state = WindowState(placement = WindowPlacement.Maximized),
        icon = painterResource("title.png")
    ) {

        startKoin {
            modules(appModule)
        }

        Application().App()
    }
}
