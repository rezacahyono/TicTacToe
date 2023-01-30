package com.rchyn.tictactoe.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rchyn.tictactoe.R
import com.rchyn.tictactoe.ui.theme.TicTacToeTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onHostButtonClicked: () -> Unit,
    onDiscoverButtonClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        FilledTonalButton(
            modifier = modifier.fillMaxWidth(),
            onClick = onHostButtonClicked
        ) {
            Text(text = stringResource(id = R.string.host))
        }
        FilledTonalButton(
            modifier = modifier.fillMaxWidth(),
            onClick = onDiscoverButtonClicked
        ) {
            Text(text = stringResource(id = R.string.discover))
        }
    }

}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    TicTacToeTheme {
        HomeScreen(
            onHostButtonClicked = {},
            onDiscoverButtonClicked = {}
        )
    }
}