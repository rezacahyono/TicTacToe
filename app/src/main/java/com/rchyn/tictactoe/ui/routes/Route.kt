package com.rchyn.tictactoe.ui.routes

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rchyn.tictactoe.R

enum class Route(@StringRes val title: Int?) {
    Home(R.string.home),
    Host(null),
    Discover(null),
    Game(R.string.game)
}

object TicTacToeRouter {
    var currentScreen: Route by mutableStateOf(Route.Home)

    fun navigateTo(destination: Route) {
        currentScreen = destination
    }
}