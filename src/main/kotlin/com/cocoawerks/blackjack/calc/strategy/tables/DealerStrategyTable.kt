package com.cocoawerks.blackjack.calc.strategy.tables

import com.cocoawerks.blackjack.calc.cards.hard
import com.cocoawerks.blackjack.calc.cards.pair
import com.cocoawerks.blackjack.calc.cards.soft
import com.cocoawerks.blackjack.calc.strategy.Action
import com.cocoawerks.blackjack.calc.strategy.DealerStrategy
import com.cocoawerks.blackjack.calc.strategy.HandState

class DealerStrategyTable(val strategy: DealerStrategy) {

    init {
        for (total in 5..16) {
            strategy.setPlayAction(Action.Hit, HandState(hard(total)))
        }

        for (total in 17..21) {
            strategy.setPlayAction(Action.Stand, HandState(hard(total)))
        }

        for (total in 13..16) {
            strategy.setPlayAction(Action.Hit, HandState(soft(total)))
        }

        strategy.setPlayAction(if (strategy.rules.dealerHitsSoft17) Action.Hit else Action.Stand, HandState(soft(17)))

        for (total in 18..21) {
            strategy.setPlayAction(Action.Stand, HandState(soft(total)))
        }

        //pairs
        for (total in 4..20) {
            if (total % 2 == 0) {
                if (total < 17) {
                    strategy.setPlayAction(Action.Hit, HandState(pair(total)))
                } else {
                    strategy.setPlayAction(Action.Stand, HandState(pair(total)))
                }
            }
        }
        //pair of aces
        strategy.setPlayAction(Action.Hit, HandState(pair(12, aces = true)))

    }
}