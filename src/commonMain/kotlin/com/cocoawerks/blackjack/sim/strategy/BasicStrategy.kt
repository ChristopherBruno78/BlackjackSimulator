package com.cocoawerks.blackjack.sim.strategy

import com.cocoawerks.blackjack.sim.BlackjackRules
import com.cocoawerks.blackjack.sim.strategy.tables.BasicStrategy4PlusDecksTable
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
open class BasicStrategy(val rules: BlackjackRules) : Strategy {

    private val playActions: MutableMap<HandState, Action> = HashMap()
    internal val deviationIndexes: MutableMap<Int, MutableMap<HandState, Action>> = HashMap()

    init {
        initializeFromTable()
    }

    private fun initializeFromTable() {
        BasicStrategy4PlusDecksTable(this)
    }

    fun setPlayAction(action: Action, forState: HandState) {
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

    fun setDeviation(action: Action, forState: HandState, atIndex: Int) {
        if (!deviationIndexes.containsKey(atIndex)) {
            deviationIndexes[atIndex] = HashMap()
        }
        val deviation = deviationIndexes[atIndex]!!
        deviation[forState] = action
    }

    fun toJson(): String {
        val json = Json {
            allowStructuredMapKeys = true
        }
        return json.encodeToString(this)
    }
}

fun strategyFromJsonString(jsonStr: String): BasicStrategy {
    val json = Json {
        allowStructuredMapKeys = true
    }
    val strategy = json.decodeFromString<BasicStrategy>(jsonStr)
    return strategy
}
