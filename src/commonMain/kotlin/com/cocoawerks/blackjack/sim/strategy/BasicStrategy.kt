package com.cocoawerks.blackjack.sim.strategy

import com.cocoawerks.blackjack.sim.BlackjackRules
import com.cocoawerks.blackjack.sim.strategy.tables.BasicStrategy4PlusDecksTable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

    fun toJson() : String {
        return Json.encodeToString(playActions)
    }
}
