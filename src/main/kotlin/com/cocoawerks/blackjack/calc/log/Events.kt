package com.cocoawerks.blackjack.calc.log

import com.cocoawerks.blackjack.calc.cards.Card
import com.cocoawerks.blackjack.calc.cards.Hand
import com.cocoawerks.blackjack.calc.entity.Stats
import com.cocoawerks.blackjack.calc.strategy.Action

class ShuffleEvent : Loggable {
    override val description:String
        get() = "Dealer Shuffles."
}

class DealingCardsEvent : Loggable {
    override val description:String = "Dealing cards..."
}

class NewRoundEvent(val index:Int): Loggable {
    override val description:String = "Round ${index}"
}

class EndOfRoundEvent(): Loggable  {
    override val description:String = "------------------------------"
}

class WinEvent(val stats: Stats) : Loggable {
    override val description:String = "${stats.name} wins. Bankroll: ${stats.bankroll}."
}

class LossEvent(val stats:Stats): Loggable {
    override val description:String = "${stats.name} loses. Bankroll: ${stats.bankroll}."
}

class PushEvent(val stats:Stats): Loggable {
    override val description:String = "${stats.name} pushed. Bankroll: ${stats.bankroll}."
}

class SurrenderEvent(val stats:Stats) : Loggable {
    override val description:String = "${stats.name} surrenders. Bankroll: ${stats.bankroll}."
}

class HasABlackjackEvent(val stats:Stats) : Loggable {
    override val description:String = "${stats.name} has blackjack!"
}

class BetPlacedEvent(val stats:Stats, val wager:Double): Loggable {
    override val description:String = "${stats.name} bets ${wager}."
}

class HandBustsEvent(val stats:Stats): Loggable {
    override val description:String = "${stats.name} busts."
}

class HandChangedEvent(val stats:Stats, val hand:Hand): Loggable {
    override val description:String = "${stats.name} hand: ${hand}"
}

class PlayActionEvent(val stats:Stats, val action:Action): Loggable {
    override val description:String = "${stats.name}: ${action.name.uppercase()}."
}

class DealerUpCardEvent(val upCard: Card): Loggable {
    override val description:String = "Dealer has upcard: ${upCard}"
}
