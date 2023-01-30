package com.rchyn.tictactoe

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.gms.nearby.Nearby
import com.rchyn.tictactoe.ui.screens.TicTacToeApp
import com.rchyn.tictactoe.ui.theme.TicTacToeTheme
import com.rchyn.tictactoe.viewmodel.TicTacToeViewModel
import com.rchyn.tictactoe.viewmodel.TicTacToeViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: TicTacToeViewModel by viewModels {
        TicTacToeViewModelFactory(Nearby.getConnectionsClient(applicationContext))
    }

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.entries.any { !it.value }) {
            Toast.makeText(this, "Required permissions needed", Toast.LENGTH_LONG).show()
            finish()
        } else {
            recreate()
        }
    }

    override fun onStart() {
        super.onStart()
        if (!hasPermissions(this, REQUIRED_PERMISSIONS)) {
            requestMultiplePermissions.launch(
                REQUIRED_PERMISSIONS
            )
        }
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.isEmpty() || permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                TicTacToeApp(viewModel = viewModel)
            }
        }
    }

    private companion object {
        val REQUIRED_PERMISSIONS =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                arrayOf(
                    android.Manifest.permission.BLUETOOTH,
                    android.Manifest.permission.BLUETOOTH_ADVERTISE,
                    android.Manifest.permission.BLUETOOTH_CONNECT,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.NEARBY_WIFI_DEVICES,
                    android.Manifest.permission.BLUETOOTH_SCAN
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            }
    }
}
