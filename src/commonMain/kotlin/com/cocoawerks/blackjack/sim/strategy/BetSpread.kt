package com.cocoawerks.blackjack.sim.strategy

class BetSpread(private val spread:Map<Int, Double>) {

    fun getBet(index: Int): Double {
        if (spread.containsKey(index)) {
            return spread[index]!!
        } else {
            var i = index
            while (!spread.containsKey(i)) {
                if (i > 0) {
                    i -= 1
                } else {
                    i += 1
                }
                if (i == 0) {
                    break
                }
            }
            return spread[i] ?: 1.0
        }
    }
}