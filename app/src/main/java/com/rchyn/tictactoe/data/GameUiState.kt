package com.rchyn.tictactoe.data

data class GameUiState(
    val localPlayer: Int = -1,
    val playerTurn: Int = -1,
    val playerWon: Int = -1,
    val isOver: Boolean = false,
    val board: Array<Array<Int>> = emptyArray()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameUiState

        if (localPlayer != other.localPlayer) return false
        if (playerTurn != other.playerTurn) return false
        if (playerWon != other.playerWon) return false
        if (isOver != other.isOver) return false
        if (!board.contentDeepEquals(other.board)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = localPlayer
        result = 31 * result + playerTurn
        result = 31 * result + playerWon
        result = 31 * result + isOver.hashCode()
        result = 31 * result + board.contentDeepHashCode()
        return result
    }
}
