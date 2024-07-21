package com.cocoawerks.blackjack.calc.entity

import com.cocoawerks.blackjack.calc.BlackjackGame
import com.cocoawerks.blackjack.calc.BlackjackRules
import com.cocoawerks.blackjack.calc.cards.Card
import com.cocoawerks.blackjack.calc.cards.DeckShoe
import com.cocoawerks.blackjack.calc.cards.Hand
import com.cocoawerks.blackjack.calc.log.HandBustsEvent
import com.cocoawerks.blackjack.calc.log.HandChangedEvent
import com.cocoawerks.blackjack.calc.log.PlayActionEvent
import com.cocoawerks.blackjack.calc.strategy.Action
import com.cocoawerks.blackjack.calc.strategy.DealerStrategy
import com.cocoawerks.blackjack.calc.strategy.HandState

class Dealer(val rules: BlackjackRules) :
    Entity(name = "Dealer", strategy = DealerStrategy(hitSoft17 = rules.dealerHitsSoft17)) {
    val deck = DeckShoe(numberOfDecks = rules.numberOfDecks)
    var dealerHand: Hand? = null

    val upCard: Card?
        get() {
            return dealerHand?.cardAt(0)
        }

    fun dealCard(): Card? {
        return deck.drawCard()
    }

    fun shuffle() {

        deck.shuffle()
    }

    fun shuffleIfNeeded(pen: Double) {
        if (deck.pen > pen) {
            shuffle()
        }
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
            hand.addCard(dealCard())
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
