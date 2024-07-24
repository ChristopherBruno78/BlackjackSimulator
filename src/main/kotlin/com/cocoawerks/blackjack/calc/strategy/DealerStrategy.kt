package com.cocoawerks.blackjack.calc.strategy

import com.cocoawerks.blackjack.calc.BlackjackRules
import com.cocoawerks.blackjack.calc.cards.hard
import com.cocoawerks.blackjack.calc.cards.pair
import com.cocoawerks.blackjack.calc.cards.soft
import com.cocoawerks.blackjack.calc.strategy.tables.DealerStrategyTable

class DealerStrategy(val rules: BlackjackRules) : Strategy {

    private val playActions: MutableMap<HandState, Action> = HashMap()

    init {
        DealerStrategyTable(this)
    }

    override fun getPlayAction(state: HandState): Action {
        return playActions[state]!!
    }

    override fun setPlayAction(action: Action, forState: HandState) {
        playActions[forState] = action
    }

    override fun getBet(): Double { return 0.0 }

    override fun getNumberOfBettingSpots():Int { return 0 }

    override fun reset() {  }

    override fun willTakeInsurance(): Boolean { return false }
}
