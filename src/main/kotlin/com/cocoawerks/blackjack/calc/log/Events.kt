package com.cocoawerks.blackjack.calc.log

import com.cocoawerks.blackjack.calc.cards.Card
import com.cocoawerks.blackjack.calc.cards.Hand
import com.cocoawerks.blackjack.calc.entity.Stats
import com.cocoawerks.blackjack.calc.strategy.Action

interface Event {
    val description:String
}

class ShuffleEvent : Event {
    override val description:String
        get() = "Shuffling."
}

class DealingCardsEvent : Event {
    override val description:String = "Dealing cards..."
}

class NewRoundEvent(val index:Int): Event {
    override val description:String = "Round ${index}"
}

class WinEvent(val stats: Stats) : Event {
    override val description:String = "${stats.name} wins. Bankroll: ${stats.bankroll}."
}

class LossEvent(val stats:Stats): Event {
    override val description:String = "${stats.name} loses. Bankroll: ${stats.bankroll}."
}

class PushEvent(val stats:Stats): Event {
    override val description:String = "${stats.name} pushed. Bankroll: ${stats.bankroll}."
}

class SurrenderEvent(val stats:Stats) : Event {
    override val description:String = "${stats.name} surrenders. Bankroll: ${stats.bankroll}."
}

class HasABlackjackEvent(val stats:Stats) : Event {
    override val description:String = "${stats.name} has blackjack!"
}

class BetPlacedEvent(val stats:Stats, val wager:Double): Event {
    override val description:String = "${stats.name} bets ${wager}."
}

class HandBustsEvent(val stats:Stats): Event {
    override val description:String = "${stats.name} busts."
}

class HandChangedEvent(val stats:Stats, val hand:Hand): Event {
    override val description:String = "${stats.name} hand: ${hand}"
}

class PlayActionEvent(val stats:Stats, val action:Action): Event {
    override val description:String = "${stats.name}: ${action.name.uppercase()}."
}

class DealerUpCardEvent(val upCard: Card): Event {
    override val description:String = "Dealer has upcard: ${upCard}"
}
