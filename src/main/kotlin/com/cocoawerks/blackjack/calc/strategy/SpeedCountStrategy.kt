package com.cocoawerks.blackjack.calc.strategy

import com.cocoawerks.blackjack.calc.BlackjackRules
import com.cocoawerks.blackjack.calc.cards.Card

class SpeedCountStrategy(betSpread: BetSpread, rules: BlackjackRules) : CountingStrategy(betSpread, rules) {

    override fun updateRunningCount(observedCard: Card) {
        val value = observedCard.value()
        if (value >= 2 && value < 7) {
            _runningCount += 1
        }
        numberOfCardsObserved += 1
    }

    fun subtractNumberOfHands(numHands: Int) {
        _runningCount -= numHands
    }

    override fun reset() {
        _runningCount = 6
        numberOfCardsObserved = 0
    }

    /** Speed Count is an unbalanced count **/
    override val trueCount: Int
        get() {
            return _runningCount
        }

}