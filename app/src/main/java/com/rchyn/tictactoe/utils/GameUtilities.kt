package com.rchyn.tictactoe.utils

import com.google.android.gms.nearby.connection.Payload
import kotlin.text.Charsets.UTF_8

fun Payload.toPosition(): Pair<Int, Int> {
    val positionStr = String(asBytes()!!, UTF_8)
    val positionArray = positionStr.split(",")
    return positionArray[0].toInt() to positionArray[1].toInt()
}

fun Pair<Int, Int>.toPayLoad() = Payload.fromBytes("$first,$second".toByteArray(UTF_8))