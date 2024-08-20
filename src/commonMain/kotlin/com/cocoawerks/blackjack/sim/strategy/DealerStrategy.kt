package com.cocoawerks.blackjack.sim.strategy

import com.cocoawerks.blackjack.sim.BlackjackRules
import com.cocoawerks.blackjack.sim.strategy.tables.DealerStrategyTable

class DealerStrategy(val rules: BlackjackRules) : Strategy {

    private val playActions: MutableMap<HandState, Action> = HashMap()

    init {
        DealerStrategyTable(this)
    }

    override fun getPlayAction(state: HandState): Action {
        return playActions[state]!!
    }

    fun setPlayAction(action: Action, forState: HandState) {
        playActions[forState] = action
    }

    override fun getBet(): Double {
        return 0.0
    }

    override fun getNumberOfBettingSpots(): Int {
        return 0
    }

    override fun reset() {}

    override fun willTakeInsurance(): Boolean {
        return false
    }
}
