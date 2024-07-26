package com.cocoawerks.blackjack.sim.entity

import com.cocoawerks.blackjack.sim.BlackjackGame
import com.cocoawerks.blackjack.sim.cards.Card
import com.cocoawerks.blackjack.sim.cards.DeckShoe
import com.cocoawerks.blackjack.sim.cards.Hand
import com.cocoawerks.blackjack.sim.log.HandBustsEvent
import com.cocoawerks.blackjack.sim.log.HandChangedEvent
import com.cocoawerks.blackjack.sim.log.PlayActionEvent
import com.cocoawerks.blackjack.sim.log.ShuffleEvent
import com.cocoawerks.blackjack.sim.strategy.Action
import com.cocoawerks.blackjack.sim.strategy.DealerStrategy
import com.cocoawerks.blackjack.sim.strategy.HandState

class Dealer(val game: BlackjackGame) :
    Entity(name = "Dealer", strategy = DealerStrategy(game.rules)) {

    val deck = DeckShoe(numberOfDecks = game.rules.numberOfDecks)
    var dealerHand: Hand? = null

    val upCard: Card?
        get() {
            return dealerHand?.cardAt(0)
        }

    val holeCard: Card?
        get() {
            return dealerHand?.cardAt(1)
        }

    fun dealCard(): Card? {
        return deck.drawCard()
    }

    fun shuffle() {
        log(ShuffleEvent())
        deck.shuffle()
    }

    fun shuffleIfNeeded(pen: Double): Boolean {
        if (deck.pen > pen) {
            shuffle()
            return true
        }
        return false
    }

    fun startHand() {
        clearHands()
        val hand = Hand()
        hand.owner = this
        hands.add(hand)
        dealerHand = hand
    }

    fun playHand(): Action? {
        if (dealerHand != null) {
            return playHand(dealerHand!!)
        }
        return null
    }

    override fun playHand(hand: Hand, forGame: BlackjackGame?): Action? {
        if (makeDecision(HandState(dealerHand!!.hash)) == Action.Hit) {
            log(PlayActionEvent(stats = stats.copy(), action = Action.Hit))
            hand.addCard(game.takeVisibleCardFromDealer())
            log(HandChangedEvent(stats = stats.copy(), hand = hand))
            if (dealerHand!!.isBusted) {
                log(HandBustsEvent(stats = stats.copy()))
                return null
            }
            return playHand(hand)
        }
        log(PlayActionEvent(stats = stats.copy(), action = Action.Stand))
        return Action.Stand
    }
}
