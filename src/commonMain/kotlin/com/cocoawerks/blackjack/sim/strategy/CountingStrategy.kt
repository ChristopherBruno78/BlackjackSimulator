package com.cocoawerks.blackjack.sim.strategy

import com.cocoawerks.blackjack.sim.cards.Card
import com.cocoawerks.blackjack.sim.stats.CountStats

open class CountingStrategy(
    private val betSpread: BetSpread,
    private val index: CountingIndex,
    private val playStrategy: BasicStrategy
) : Strategy {

    protected var runningCount: Int = 0
    protected var numberOfCardsObserved = 0

    private val stats: CountStats = CountStats()

    open fun updateRunningCount(observedCard: Card) {
        this.runningCount += index.valueForCard(observedCard)
        numberOfCardsObserved += 1
    }

    override fun reset() {
        this.runningCount = 0
        numberOfCardsObserved = 0
    }

    override fun getBet(): Double {
        //record the true count in stats
        val count = index.getTrueCount(runningCount, numberOfCardsObserved, playStrategy.rules)
        stats.recordIndex(count)
        return betSpread.getBet(count)
    }

    override fun getNumberOfBettingSpots(): Int {
         return 1
    }

    override fun willTakeInsurance(): Boolean {
        return index.getTakeInsurance(runningCount, numberOfCardsObserved, playStrategy.rules)
    }

    override fun getPlayAction(state: HandState): Action {
        val count = index.getTrueCount(runningCount, numberOfCardsObserved, playStrategy.rules)
        val indexes = playStrategy.deviationIndexes.keys
        for (index in indexes) {
            if (index in 0..count) {
                val deviations = playStrategy.deviationIndexes[index]!!
                if (deviations.containsKey(state)) {
                    return deviations[state]!!
                }
            } else if (index in (count + 1)..-1) {
                val deviations = playStrategy.deviationIndexes[index]!!
                if (deviations.containsKey(state)) {
                    return deviations[state]!!
                }
            }
        }
        return playStrategy.getPlayAction(state)
    }
}