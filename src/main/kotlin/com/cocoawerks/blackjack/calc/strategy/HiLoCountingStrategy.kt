package com.cocoawerks.blackjack.calc.strategy

import com.cocoawerks.blackjack.calc.BlackjackRules
import com.cocoawerks.blackjack.calc.cards.Card
import com.cocoawerks.blackjack.calc.strategy.tables.IllustriousEighteen

class HiLoCountingStrategy(betSpread: BetSpread, rules: BlackjackRules, private val illustriousEighteen:Boolean = false) : CountingStrategy(betSpread, rules) {

    init {
        if(illustriousEighteen) {
            IllustriousEighteen(this)
        }

    }

    override fun updateRunningCount(observedCard: Card) {
        val value = observedCard.rank.value()
        if (value >= 2 && value < 7) {
            _runningCount += 1
        } else if (value == 1 || value == 10) {
            _runningCount -= 1
        }
        numberOfCardsObserved += 1
    }

    override fun willTakeInsurance(): Boolean {
        return if(illustriousEighteen) trueCount > 2 else false
    }

}