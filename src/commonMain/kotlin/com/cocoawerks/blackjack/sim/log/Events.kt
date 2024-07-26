package com.cocoawerks.blackjack.sim.log

import com.cocoawerks.blackjack.sim.cards.Card
import com.cocoawerks.blackjack.sim.cards.Hand
import com.cocoawerks.blackjack.sim.stats.PlayerStats
import com.cocoawerks.blackjack.sim.strategy.Action

class ShuffleEvent : Loggable {
    override val description: String
        get() = "Dealer Shuffles."
}

class DealingCardsEvent : Loggable {
    override val description: String = "Dealing cards..."
}

class NewRoundEvent(val index: Int) : Loggable {
    override val description: String = "Round ${index}"
}

class EndOfRoundEvent : Loggable {
    override val description: String = "------------------------------"
}

class WinEvent(val stats: PlayerStats) : Loggable {
    override val description: String = "${stats.name} wins. Bankroll: ${stats.bankroll}."
}

class LossEvent(val stats: PlayerStats) : Loggable {
    override val description: String = "${stats.name} loses. Bankroll: ${stats.bankroll}."
}

class PushEvent(val stats: PlayerStats) : Loggable {
    override val description: String = "${stats.name} pushed. Bankroll: ${stats.bankroll}."
}

class SurrenderEvent(val stats: PlayerStats) : Loggable {
    override val description: String = "${stats.name} surrenders. Bankroll: ${stats.bankroll}."
}

class HasABlackjackEvent(val stats: PlayerStats) : Loggable {
    override val description: String = "${stats.name} has blackjack!"
}

class DealerDoesNotHaveBlackjackEvent : Loggable {
    override val description: String = "Dealer does not have a blackjack."
}

class BetPlacedEvent(val stats: PlayerStats, val wager: Double) : Loggable {
    override val description: String = "${stats.name} bets ${wager}."
}

class HandBustsEvent(val stats: PlayerStats) : Loggable {
    override val description: String = "${stats.name} busts."
}

class HandChangedEvent(val stats: PlayerStats, val hand: Hand) : Loggable {
    override val description: String = "${stats.name} hand: ${hand}"
}

class PlayActionEvent(val stats: PlayerStats, val action: Action) : Loggable {
    override val description: String = "${stats.name}: ${action.name.uppercase()}."
}

class DealerUpCardEvent(val upCard: Card) : Loggable {
    override val description: String = "Dealer has upcard: ${upCard}"
}

class DealerOffersInsuranceEvent : Loggable {
    override val description: String = "Dealer offers insurance."
}

class PlayerInsuranceDecisionEvent(val stats: PlayerStats, decision: Boolean) : Loggable {
    override val description: String =
        if (decision) "${stats.name} takes insurance." else "${stats.name} declines insurance."
}

class PlayerPaidInsuranceEvent(val stats: PlayerStats) : Loggable {
    override val description: String = "${stats.name} paid insurance."
}
