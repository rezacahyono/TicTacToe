package com.rchyn.tictactoe.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.nearby.connection.*
import com.rchyn.tictactoe.BuildConfig
import com.rchyn.tictactoe.data.GameUiState
import com.rchyn.tictactoe.data.TicTacToe
import com.rchyn.tictactoe.ui.routes.Route
import com.rchyn.tictactoe.ui.routes.TicTacToeRouter
import com.rchyn.tictactoe.utils.toPayLoad
import com.rchyn.tictactoe.utils.toPosition
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class TicTacToeViewModel(
    private val connectionsClient: ConnectionsClient
) : ViewModel() {
    private val localUsername = UUID.randomUUID().toString()
    private var localPlayer: Int = 0
    private var opponentPlayer: Int = 0
    private var opponentEndpointId: String = ""

    private var game = TicTacToe()

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val payloadCallback: PayloadCallback = object : PayloadCallback() {
        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            Log.d(TAG, "onPayloadReceived")

            if (payload.type == Payload.Type.BYTES) {
                val position = payload.toPosition()
                Log.d(TAG, "Received [${position.first},${position.second}] from $endpointId")
                play(opponentPlayer, position)
            }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            Log.d(TAG, "onPayloadTransferUpdate")
        }
    }

    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            Log.d(TAG, "onEndpointFound")

            Log.d(TAG, "Requesting connection...")
            connectionsClient.requestConnection(
                localUsername,
                endpointId,
                connectionLifecycleCallback
            ).addOnSuccessListener {
                Log.d(TAG, "Successfully requested a connection")
            }.addOnFailureListener {
                Log.d(TAG, "Failed to request the connection")
            }
        }

        override fun onEndpointLost(endpointId: String) {
            Log.d(TAG, "onEndpointLost")
        }
    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            Log.d(TAG, "onConnectionInitiated")

            Log.d(TAG, "Accepting connection...")
            connectionsClient.acceptConnection(endpointId, payloadCallback)
        }

        override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {
            Log.d(TAG, "onConnectionResult")

            when (resolution.status.statusCode) {
                ConnectionsStatusCodes.STATUS_OK -> {
                    Log.d(TAG, "ConnectionsStatusCodes.STATUS_OK")

                    connectionsClient.stopAdvertising()
                    connectionsClient.stopDiscovery()
                    opponentEndpointId = endpointId
                    Log.d(TAG, "opponentEndpointId: $opponentEndpointId")
                    newGame()
                    TicTacToeRouter.navigateTo(Route.Game)
                }
                ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                    Log.d(TAG, "ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED")
                }
                ConnectionsStatusCodes.STATUS_ERROR -> {
                    Log.d(TAG, "ConnectionsStatusCodes.STATUS_ERROR")
                }
                else -> {
                    Log.d(TAG, "Unknown status code ${resolution.status.statusCode}")
                }
            }
        }

        override fun onDisconnected(endpointId: String) {
            Log.d(TAG, "onDisconnected")
            goToHome()
        }
    }

    fun startHosting() {
        Log.d(TAG, "Start advertising...")
        TicTacToeRouter.navigateTo(Route.Host)
        val advertisingOptions = AdvertisingOptions.Builder().setStrategy(STRATEGY).build()

        connectionsClient.startAdvertising(
            localUsername,
            BuildConfig.APPLICATION_ID,
            connectionLifecycleCallback,
            advertisingOptions
        ).addOnSuccessListener {
            Log.d(TAG, "Advertising...")
            localPlayer = 1
            opponentPlayer = 2
        }.addOnFailureListener {
            Log.d(TAG, "Unable to start advertising")
            TicTacToeRouter.navigateTo(Route.Home)
        }
    }

    fun startDiscovering() {
        Log.d(TAG, "Start discovering...")
        TicTacToeRouter.navigateTo(Route.Discover)
        val discoveryOptions = DiscoveryOptions.Builder().setStrategy(STRATEGY).build()

        connectionsClient.startDiscovery(
            BuildConfig.APPLICATION_ID,
            endpointDiscoveryCallback,
            discoveryOptions
        ).addOnSuccessListener {
            Log.d(TAG, "Discovering...")
            localPlayer = 2
            opponentPlayer = 1
        }.addOnFailureListener {
            Log.d(TAG, "Unable to start discovering")
            TicTacToeRouter.navigateTo(Route.Home)
        }
    }

    fun newGame() {
        Log.d(TAG, "Starting new game")
        game = TicTacToe()
        _state.value = GameUiState(localPlayer, game.playerTurn, game.playerWon, game.isOver, game.board)
    }

    fun play(position: Pair<Int, Int>) {
        if (game.playerTurn != localPlayer) return
        if (game.isPlayedBucket(position)) return

        play(localPlayer, position)
        sendPosition(position)
    }

    private fun play(player: Int, position: Pair<Int, Int>) {
        Log.d(TAG, "Player $player played [${position.first},${position.second}]")

        game.play(player, position)
        _state.value = GameUiState(localPlayer, game.playerTurn, game.playerWon, game.isOver, game.board)
    }

    private fun sendPosition(position: Pair<Int, Int>) {
        Log.d(TAG, "Sending [${position.first},${position.second}] to $opponentEndpointId")
        connectionsClient.sendPayload(
            opponentEndpointId,
            position.toPayLoad()
        )
    }

    override fun onCleared() {
        stopClient()
        super.onCleared()
    }

    fun goToHome() {
        stopClient()
        TicTacToeRouter.navigateTo(Route.Home)
    }

    private fun stopClient() {
        Log.d(TAG, "Stop advertising, discovering, all endpoints")
        connectionsClient.stopAdvertising()
        connectionsClient.stopDiscovery()
        connectionsClient.stopAllEndpoints()
        localPlayer = 0
        opponentPlayer = 0
        opponentEndpointId = ""
    }

    companion object {
        val TAG: String = TicTacToeViewModel::class.java.simpleName

        val STRATEGY = Strategy.P2P_POINT_TO_POINT
    }
}