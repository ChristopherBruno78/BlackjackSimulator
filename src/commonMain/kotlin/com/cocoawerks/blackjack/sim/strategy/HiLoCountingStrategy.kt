package com.cocoawerks.blackjack.sim.strategy

import com.cocoawerks.blackjack.sim.BlackjackRules
import com.cocoawerks.blackjack.sim.cards.Rank
import com.cocoawerks.blackjack.sim.strategy.tables.IllustriousEighteenTable

class HiLoCountingStrategy(betSpread: BetSpread, rules: BlackjackRules, private val illustriousEighteen:Boolean = false) : CountingStrategy(betSpread, rules) {

    init {
        if(illustriousEighteen) {
            IllustriousEighteenTable(this)
        }

        cardValues = mapOf(
            Rank.Ace to -1,
            Rank.Two to 1,
            Rank.Three to 1,
            Rank.Four to 1,
            Rank.Five to 1,
            Rank.Six to 1,
            Rank.Seven to 0,
            Rank.Eight to 0,
            Rank.Nine to 0,
            Rank.Ten to -1,
            Rank.Jack to -1,
            Rank.Queen to -1,
            Rank.King to -1
        )

    }


    override fun willTakeInsurance(): Boolean {
        return if(illustriousEighteen) trueCount > 2 else false
    }

    override fun getNumberOfBettingSpots(): Int {
        return if(trueCount > 3) 2 else 1
    }

}