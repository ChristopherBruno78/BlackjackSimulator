package com.cocoawerks.blackjack.sim.applications.speedcount

import com.cocoawerks.blackjack.sim.BlackjackRules
import com.cocoawerks.blackjack.sim.cards.Rank
import com.cocoawerks.blackjack.sim.strategy.BasicStrategy
import com.cocoawerks.blackjack.sim.strategy.BetSpread
import com.cocoawerks.blackjack.sim.strategy.CountingIndex
import com.cocoawerks.blackjack.sim.strategy.CountingStrategy

class SpeedCountIndex : CountingIndex() {

    init {
        setValueForRank(Rank.Two, 1)
        setValueForRank(Rank.Three, 1)
        setValueForRank(Rank.Four, 1)
        setValueForRank(Rank.Five, 1)
        setValueForRank(Rank.Six, 1)

        insurancePivot = 18
    }


    override fun getTrueCount(runningCount: Int, numberOfCardsObserved: Int, rules: BlackjackRules): Int {
        return runningCount
    }
}

class SpeedCountStrategy(betSpread: BetSpread, rules: BlackjackRules) : CountingStrategy(betSpread, SpeedCountIndex(), BasicStrategy(rules)) {


    fun subtractNumberOfHands(numHands: Int) {
        runningCount -= numHands
    }

    override fun reset() {
        runningCount = 6
        numberOfCardsObserved = 0
    }
}