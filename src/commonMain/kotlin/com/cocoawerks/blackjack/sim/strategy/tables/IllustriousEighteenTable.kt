package com.cocoawerks.blackjack.sim.strategy.tables

import com.cocoawerks.blackjack.sim.cards.Rank
import com.cocoawerks.blackjack.sim.cards.hard
import com.cocoawerks.blackjack.sim.cards.pair
import com.cocoawerks.blackjack.sim.cards.soft
import com.cocoawerks.blackjack.sim.strategy.Action
import com.cocoawerks.blackjack.sim.strategy.CountingStrategy
import com.cocoawerks.blackjack.sim.strategy.HandState

class IllustriousEighteenTable(strategy: CountingStrategy) {

    init {
        strategy.setDeviation(Action.Stand, HandState(hard(16), upCard = Rank.Ten), atIndex = 1)
        strategy.setDeviation(Action.Stand, HandState(hard(15), upCard = Rank.Ten), atIndex = 4)
        strategy.setDeviation(Action.SplitOrStand, HandState(pair(20), upCard = Rank.Five), atIndex = 5)
        strategy.setDeviation(Action.SplitOrStand, HandState(pair(20), upCard = Rank.Six), atIndex = 4)
        strategy.setDeviation(Action.DoubleOrHit, HandState(hard(10), upCard = Rank.Ten), atIndex = 4)
        strategy.setDeviation(Action.Stand, HandState(hard(12), upCard = Rank.Three), atIndex = 2)
        strategy.setDeviation(Action.SplitOrStand, HandState(soft(12), upCard = Rank.Two), atIndex = 3)
        strategy.setDeviation(Action.Stand, HandState(hard(12), upCard = Rank.Two), atIndex = 3)
        strategy.setDeviation(Action.DoubleOrHit, HandState(hard(11), upCard = Rank.Ace), atIndex = 1)
        strategy.setDeviation(Action.DoubleOrHit, HandState(hard(9), upCard = Rank.Two), atIndex = 1)
        strategy.setDeviation(Action.DoubleOrHit, HandState(hard(10), upCard = Rank.Ace), atIndex = 4)
        strategy.setDeviation(Action.DoubleOrHit, HandState(hard(9), upCard = Rank.Seven), atIndex = 3)
        strategy.setDeviation(Action.Stand, HandState(hard(16), upCard = Rank.Nine), atIndex = 5)
        strategy.setDeviation(Action.Hit, HandState(hard(13), upCard = Rank.Two), atIndex = -2)
        strategy.setDeviation(Action.Hit, HandState(hard(12), upCard = Rank.Four), atIndex = -1)
        strategy.setDeviation(Action.Hit, HandState(hard(12), upCard = Rank.Five), atIndex = -3)
        strategy.setDeviation(Action.Hit, HandState(hard(12), upCard = Rank.Six), atIndex = -2)
        strategy.setDeviation(Action.Hit, HandState(hard(13), upCard = Rank.Three), atIndex = -3)
    }
}