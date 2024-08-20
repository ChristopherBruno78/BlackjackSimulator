package com.cocoawerks.blackjack.sim.strategy

import com.cocoawerks.blackjack.sim.BlackjackRules
import com.cocoawerks.blackjack.sim.cards.Card
import com.cocoawerks.blackjack.sim.cards.Rank
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
abstract class CountingIndex {

    private var cardValues: MutableMap<Rank, Int> = mutableMapOf()

    var insurancePivot: Int = Int.MAX_VALUE

    fun valueForCard(card: Card): Int {
        if (cardValues.containsKey(card.rank)) {
            return cardValues[card.rank]!!
        }
        return 0
    }

    fun setValueForRank(rank: Rank, value: Int) {
         this.cardValues[rank] = value
    }

    abstract fun getTrueCount(runningCount: Int, numberOfCardsObserved: Int, rules: BlackjackRules): Int

    fun getTakeInsurance(runningCount: Int, numberOfCardsObserved: Int, rules: BlackjackRules): Boolean {
        return getTrueCount(runningCount, numberOfCardsObserved, rules) > insurancePivot
    }

    fun toJson():String {
        val json = Json {
            allowStructuredMapKeys = true
        }
        return json.encodeToString(this)
    }

}