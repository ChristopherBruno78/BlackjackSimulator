package com.cocoawerks.blackjack.calc.strategy

import com.cocoawerks.blackjack.calc.BlackjackRules
import com.cocoawerks.blackjack.calc.strategy.tables.BasicStrategy4PlusDecksTable

open class BasicStrategy(val rules: BlackjackRules) : Strategy {

    private val playActions: MutableMap<HandState, Action> = HashMap()

    init {
        BasicStrategy4PlusDecksTable(this)
    }

    override fun setPlayAction(action: Action, forState: HandState) {
        playActions[forState] = action
    }

    override fun getPlayAction(state: HandState): Action {
        return playActions[state]!!
    }

    override fun willTakeInsurance(): Boolean {
        return false
    }

    override fun getBet(): Double {
        return 1.0
    }

    override fun getNumberOfBettingSpots(): Int {
        return 1
    }

    override fun reset() {}
}
