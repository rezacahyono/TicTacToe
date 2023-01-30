package com.rchyn.tictactoe.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rchyn.tictactoe.R
import com.rchyn.tictactoe.data.TicTacToe
import com.rchyn.tictactoe.ui.theme.TicTacToeTheme
import com.rchyn.tictactoe.viewmodel.TicTacToeViewModel

@Composable
fun GameScreen(
    viewModel: TicTacToeViewModel
) {
    val state by viewModel.state.collectAsState()

    if (state.isOver) {
        GameOverScreen(
            playerWon = state.playerWon,
            onNewGameClick = { viewModel.newGame() }
        )
    } else {
        OngoingGameScreen(
            localPlayer = state.localPlayer,
            playerTurn = state.playerTurn,
            board = state.board,
            onBucketClick = { position ->
                viewModel.play(position)
            }
        )
    }
}

@Composable
fun GameOverScreen(
    playerWon: Int,
    onNewGameClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.game_over),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = if (playerWon > 0) stringResource(
                id = R.string.player_won,
                playerWon
            ) else stringResource(id = R.string.player_lose),
            style = MaterialTheme.typography.displayMedium
        )
        FilledTonalButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 16.dp),
            onClick = onNewGameClick
        ) {
            Text(text = stringResource(id = R.string.new_game))
        }
    }
}

@Composable
fun OngoingGameScreen(
    localPlayer: Int,
    playerTurn: Int,
    board: Array<Array<Int>>,
    onBucketClick: (position: Pair<Int, Int>) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.height(64.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.local_player, localPlayer),
                style = MaterialTheme.typography.titleLarge
            )
            Box(
                modifier = Modifier
                    .padding(4.dp, 0.dp)
                    .size(20.dp)
            ) {
                Icon(
                    painter = painterResource(id = getPlayerIcon(localPlayer)),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
            }
        }
        Text(
            text = if (localPlayer == playerTurn)
                stringResource(id = R.string.your_turn)
            else stringResource(id = R.string.waiting_turn, playerTurn),
            style = MaterialTheme.typography.labelLarge
        )
        BoardGame(
            modifier = Modifier.weight(9f),
            board = board,
            onBucketButtonClicked = { position ->
                onBucketClick(position)
            }
        )
    }
}

@Composable
fun BoardGame(
    modifier: Modifier = Modifier,
    board: Array<Array<Int>>,
    onBucketButtonClicked: (position: Pair<Int, Int>) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        for (i in board.indices) {
            Column(
                modifier = modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (j in board.indices) {
                    Bucket(modifier = modifier
                        .fillMaxSize()
                        .weight(1f),
                        player = board[i][j],
                        onClick = { onBucketButtonClicked(i to j) })
                }
            }
        }
    }

}

@Composable
fun Bucket(
    modifier: Modifier = Modifier, player: Int, onClick: () -> Unit
) {
    IconButton(
        modifier = modifier.border(
            width = 1.dp, color = MaterialTheme.colorScheme.secondary, RoundedCornerShape(8.dp)
        ),
        onClick = onClick,
    ) {
        if (player != 0) {
            Icon(
                modifier = modifier,
                painter = painterResource(id = getPlayerIcon(player)),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        }
    }
}

private fun getPlayerIcon(player: Int): Int {
    return when (player) {
        1 -> R.drawable.ic_close
        2 -> R.drawable.ic_circle
        else -> throw IllegalArgumentException("Missing icon for player $player")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBucket() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Bucket(modifier = Modifier
                .fillMaxSize()
                .weight(1f), player = 2, onClick = {})
            Bucket(modifier = Modifier
                .fillMaxSize()
                .weight(1f), player = 1, onClick = {})
            Bucket(modifier = Modifier
                .fillMaxSize()
                .weight(1f), player = 0, onClick = {})
        }
        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Bucket(modifier = Modifier
                .fillMaxSize()
                .weight(1f), player = 1, onClick = {})
            Bucket(modifier = Modifier
                .fillMaxSize()
                .weight(1f), player = 2, onClick = {})
            Bucket(modifier = Modifier
                .fillMaxSize()
                .weight(1f), player = 2, onClick = {})
        }
        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Bucket(modifier = Modifier
                .fillMaxSize()
                .weight(1f), player = 1, onClick = {})
            Bucket(modifier = Modifier
                .fillMaxSize()
                .weight(1f), player = 2, onClick = {})
            Bucket(modifier = Modifier
                .fillMaxSize()
                .weight(1f), player = 2, onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOnGoingGame() {
    TicTacToeTheme {
        OngoingGameScreen(
            localPlayer = 1,
            playerTurn = 1,
            board = TicTacToe().board,
            onBucketClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameOverScreen() {
    TicTacToeTheme {
        GameOverScreen(
            playerWon = 1,
            onNewGameClick = {}
        )
    }
}