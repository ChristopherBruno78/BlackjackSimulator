package com.cocoawerks.blackjack.sim.strategy

import com.cocoawerks.blackjack.sim.BlackjackRules
import com.cocoawerks.blackjack.sim.cards.Rank
import kotlin.math.min

class AceFiveCountingStrategy(val minBet: Double, val maxBet: Double, rules: BlackjackRules) : CountingStrategy(
    BetSpread(mapOf(0 to minBet)), rules
) {

    private var lastBet: Double = minBet

    init {
        cardValues = mapOf(
            Rank.Five to 1,
            Rank.Ace to -1
        )
    }

    override fun getBet(): Double {
        if (trueCount > 1) {
            lastBet = min(maxBet, 2 * lastBet)
            return lastBet
        } else {
            return minBet
        }
    }

    override val trueCount: Int
        get() = _runningCount
}