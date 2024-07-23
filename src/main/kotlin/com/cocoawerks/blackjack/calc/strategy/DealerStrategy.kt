package com.cocoawerks.blackjack.calc.strategy

import com.cocoawerks.blackjack.calc.cards.hard
import com.cocoawerks.blackjack.calc.cards.pair
import com.cocoawerks.blackjack.calc.cards.soft

class DealerStrategy(val hitSoft17: Boolean = false) : Strategy() {

    init {
        for (total in 5..16) {
            setPlayAction(Action.Hit, HandState(hard(total)))
        }

        for (total in 17..21) {
            setPlayAction(Action.Stand, HandState(hard(total)))
        }

        for (total in 13..16) {
            setPlayAction(Action.Hit, HandState(soft(total)))
        }

        setPlayAction(if (hitSoft17) Action.Hit else Action.Stand, HandState(soft(17)))

        for (total in 18..21) {
            setPlayAction(Action.Stand, HandState(soft(total)))
        }

        //pairs
        for(total in 4..20) {
            if(total % 2 == 0){
                if(total < 17) {
                    setPlayAction(Action.Hit, HandState(pair(total)))
                }
                else {
                    setPlayAction(Action.Stand, HandState(pair(total)))
                }
            }
        }

        //pair of aces
        setPlayAction(Action.Hit, HandState(pair(12, aces = true)))

    }
}
