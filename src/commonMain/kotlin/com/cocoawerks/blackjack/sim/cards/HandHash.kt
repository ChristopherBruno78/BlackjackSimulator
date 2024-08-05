package com.cocoawerks.blackjack.sim.cards

import kotlinx.serialization.Serializable

@Serializable
data class HandHash(val isSoft: Boolean, val isPair: Boolean, val total: Int)

fun hard(total: Int): HandHash {
    return HandHash(isSoft = false, isPair = false, total = total)
}

fun soft(total: Int): HandHash {
    return HandHash(isSoft = true, isPair = false, total = total)
}

fun pair(total: Int, aces: Boolean = false): HandHash {
    if (aces) {
        return HandHash(isSoft = true, isPair = true, total = 12)
    }
    return HandHash(isSoft = false, isPair = true, total = total)
}
