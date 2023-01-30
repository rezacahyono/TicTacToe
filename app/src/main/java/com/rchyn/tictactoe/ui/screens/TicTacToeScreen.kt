package com.rchyn.tictactoe.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rchyn.tictactoe.R
import com.rchyn.tictactoe.ui.routes.Route
import com.rchyn.tictactoe.ui.routes.TicTacToeRouter
import com.rchyn.tictactoe.viewmodel.TicTacToeViewModel


@Composable
fun TicTacToeAppBar(
    currentScreen: Route
) {
    CenterAlignedTopAppBar(
        title = {
            currentScreen.title?.let {
                Text(text = stringResource(id = it))
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeApp(
    modifier: Modifier = Modifier,
    viewModel: TicTacToeViewModel
) {
    Scaffold(
        topBar = {
            TicTacToeAppBar(
                currentScreen = TicTacToeRouter.currentScreen
            )
        },
        content = { innerPadding ->
            Surface(modifier = modifier.padding(innerPadding)) {
                when (TicTacToeRouter.currentScreen) {
                    Route.Home -> {
                        HomeScreen(
                            onHostButtonClicked = {
                                viewModel.startHosting()
                            },
                            onDiscoverButtonClicked = {
                                viewModel.startDiscovering()
                            }
                        )
                    }
                    Route.Host -> {
                        WaitingScreen(title = stringResource(id = R.string.waiting_host)) {
                            viewModel.goToHome()
                        }
                    }
                    Route.Discover -> {
                        WaitingScreen(title = stringResource(id = R.string.waiting_discover)) {
                            viewModel.goToHome()
                        }
                    }
                    Route.Game -> GameScreen(viewModel)
                }
            }
        }
    )
}
