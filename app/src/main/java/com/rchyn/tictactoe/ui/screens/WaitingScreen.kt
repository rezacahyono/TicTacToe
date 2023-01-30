package com.rchyn.tictactoe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rchyn.tictactoe.ui.theme.TicTacToeTheme
import com.rchyn.tictactoe.R

@Composable
fun WaitingScreen(
    modifier: Modifier = Modifier,
    title: String,
    onStopButtonClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        CircularProgressIndicator(
            modifier = modifier
                .padding(16.dp)
                .size(64.dp)
        )
        FilledTonalButton(
            modifier = modifier.fillMaxWidth(),
            onClick = onStopButtonClicked
        ) {
            Text(text = stringResource(id = R.string.stop))
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewWaitingScreen() {
    TicTacToeTheme {
        WaitingScreen(
            title = stringResource(id = R.string.waiting_discover),
            onStopButtonClicked = {}
        )
    }
}