package com.cocoawerks.blackjack.calc.strategy

import com.cocoawerks.blackjack.calc.BlackjackRules
import com.cocoawerks.blackjack.calc.cards.Card
import com.cocoawerks.blackjack.calc.stats.CountStats

abstract class CountingStrategy(private val betSpread: BetSpread, rules: BlackjackRules) : BasicStrategy(rules) {

    protected var _runningCount: Int = 0
    protected var numberOfCardsObserved = 0

    protected val deviationIndexes: MutableMap<Int, MutableMap<HandState, Action>> = HashMap()

    protected val stats: CountStats = CountStats()

    abstract fun updateRunningCount(observedCard: Card)

    val runningCount: Int
        get() = _runningCount

    open val trueCount: Int
        get() {
            val decksRemaining = (rules.numberOfDecks * 52.0 - numberOfCardsObserved) / 52.0
            return Math.floor(_runningCount / decksRemaining).toInt()
        }

    override fun reset() {
        _runningCount = 0
        numberOfCardsObserved = 0
    }

    override fun getBet(): Double {
        //record the true count in stats
        val count = this.trueCount
        stats.recordIndex(count)
        return betSpread.getBet(count)
    }

    override fun getPlayAction(state: HandState): Action {
        val count = trueCount

        val indexes = deviationIndexes.keys
        for (index in indexes) {
            if (index >= 0 && count >= index) {
                val deviations = deviationIndexes[index]!!
                if (deviations.containsKey(state)) {
//                    println("deviation used: $count")
//                    println(state)
//                    println(deviations[state]!!)
                    return deviations[state]!!
                }
            } else if (index < 0 && count < index) {
                val deviations = deviationIndexes[index]!!
                if (deviations.containsKey(state)) {
//                    println("deviation used: $count")
//                    println(state)
//                    println(deviations[state]!!)
                    return deviations[state]!!
                }
            }
        }
        return super.getPlayAction(state)
    }

    fun setDeviation(action: Action, forState: HandState, atIndex: Int) {
        if (!deviationIndexes.containsKey(atIndex)) {
            deviationIndexes[atIndex] = HashMap()
        }
        val deviation = deviationIndexes[atIndex]!!
        deviation[forState] = action
    }

}