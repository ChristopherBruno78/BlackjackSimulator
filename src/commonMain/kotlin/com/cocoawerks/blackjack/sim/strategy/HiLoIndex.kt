package com.cocoawerks.blackjack.sim.strategy

import com.cocoawerks.blackjack.sim.BlackjackRules
import com.cocoawerks.blackjack.sim.cards.Rank
import kotlin.math.roundToInt

class HiLoIndex : CountingIndex() {
    init {
        setValueForRank(Rank.Two, 1)
        setValueForRank(Rank.Three, 1)
        setValueForRank(Rank.Four, 1)
        setValueForRank(Rank.Five, 1)
        setValueForRank(Rank.Six, 1)

        setValueForRank(Rank.Ten, -1)
        setValueForRank(Rank.Jack, -1)
        setValueForRank(Rank.Queen, -1)
        setValueForRank(Rank.King, -1)
        setValueForRank(Rank.Ace, -1)

        insurancePivot = 2
    }

    override fun getTrueCount(runningCount: Int, numberOfCardsObserved: Int, rules: BlackjackRules): Int {
        val totalNumberOfCards = rules.numberOfDecks*52
        val cardsRemaining = totalNumberOfCards - numberOfCardsObserved
        val decksRemaining = (cardsRemaining / 52).toDouble()
        return (runningCount/ decksRemaining).roundToInt()
    }
}