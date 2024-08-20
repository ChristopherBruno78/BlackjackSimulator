package com.cocoawerks.blackjack.sim.strategy

import com.cocoawerks.blackjack.sim.BlackjackRules
import com.cocoawerks.blackjack.sim.cards.Rank

class AceFiveIndex : CountingIndex() {

    init {
        setValueForRank(Rank.Five, 1)
        setValueForRank(Rank.Ace, -1)
    }

    override fun getTrueCount(runningCount: Int, numberOfCardsObserved: Int, rules: BlackjackRules): Int {
        return runningCount
    }
}