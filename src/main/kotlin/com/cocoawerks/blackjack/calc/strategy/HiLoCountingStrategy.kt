package com.cocoawerks.blackjack.calc.strategy

import com.cocoawerks.blackjack.calc.BlackjackRules
import com.cocoawerks.blackjack.calc.cards.Card

class HiLoCountingStrategy(betSpread: BetSpread, rules: BlackjackRules) : CountingStrategy(betSpread, rules) {

    override fun updateRunningCount(observedCard: Card) {
        val value = observedCard.rank.value()
        if(value >= 2 && value < 7){
            _runningCount += 1
        }
        else if (value == 1 || value == 10) {
            _runningCount -= 1
        }
        numberOfCardsObserved += 1
    }

}