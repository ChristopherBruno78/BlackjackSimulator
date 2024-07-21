package com.cocoawerks.blackjack.calc.cards

data class HandHash(val isSoft: Boolean, val isPair: Boolean, val total: Int)

fun hard(total: Int): HandHash {
    return HandHash(isSoft = false, isPair = false, total = total)
}

fun soft(total: Int): HandHash {
    return HandHash(isSoft = true, isPair = false, total = total)
}

fun pair(total: Int): HandHash {
    if (total == 2) {
        return HandHash(isSoft = true, isPair = true, total = total)
    }
    return HandHash(isSoft = false, isPair = true, total = total)
}
