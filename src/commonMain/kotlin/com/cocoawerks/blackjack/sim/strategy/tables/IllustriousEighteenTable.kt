package com.cocoawerks.blackjack.sim.strategy.tables

import com.cocoawerks.blackjack.sim.cards.hard
import com.cocoawerks.blackjack.sim.cards.pair
import com.cocoawerks.blackjack.sim.cards.soft
import com.cocoawerks.blackjack.sim.strategy.Action
import com.cocoawerks.blackjack.sim.strategy.BasicStrategy
import com.cocoawerks.blackjack.sim.strategy.HandState

class IllustriousEighteenTable(strategy: BasicStrategy) {

    init {
        strategy.setDeviation(Action.Stand, HandState(hard(16), upCard = 10), atIndex = 1)
        strategy.setDeviation(Action.Stand, HandState(hard(15), upCard = 10), atIndex = 4)
        strategy.setDeviation(Action.Split, HandState(pair(20), upCard = 5), atIndex = 5)
        strategy.setDeviation(Action.Split, HandState(pair(20), upCard = 6), atIndex = 4)
        strategy.setDeviation(Action.DoubleOrHit, HandState(hard(10), upCard = 10), atIndex = 4)
        strategy.setDeviation(Action.Stand, HandState(hard(12), upCard = 3), atIndex = 2)
        strategy.setDeviation(Action.Split, HandState(soft(12), upCard = 2), atIndex = 3)
        strategy.setDeviation(Action.Stand, HandState(hard(12), upCard = 2), atIndex = 3)
        strategy.setDeviation(Action.DoubleOrHit, HandState(hard(11), upCard = 1), atIndex = 1)
        strategy.setDeviation(Action.DoubleOrHit, HandState(hard(9), upCard = 2), atIndex = 1)
        strategy.setDeviation(Action.DoubleOrHit, HandState(hard(10), upCard = 1), atIndex = 4)
        strategy.setDeviation(Action.DoubleOrHit, HandState(hard(9), upCard = 7), atIndex = 3)
        strategy.setDeviation(Action.Stand, HandState(hard(16), upCard = 9), atIndex = 5)
        strategy.setDeviation(Action.Hit, HandState(hard(13), upCard = 2), atIndex = -2)
        strategy.setDeviation(Action.Hit, HandState(hard(12), upCard = 4), atIndex = -1)
        strategy.setDeviation(Action.Hit, HandState(hard(12), upCard = 5), atIndex = -3)
        strategy.setDeviation(Action.Hit, HandState(hard(12), upCard = 6), atIndex = -2)
        strategy.setDeviation(Action.Hit, HandState(hard(13), upCard = 3), atIndex = -3)
    }
}